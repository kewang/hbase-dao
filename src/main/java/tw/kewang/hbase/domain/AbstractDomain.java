package tw.kewang.hbase.domain;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.kewang.hbase.annotations.Column;
import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Domain;
import tw.kewang.hbase.dao.Constants;

public abstract class AbstractDomain {
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractDomain.class);
	private static final Pattern PATTERN = Pattern.compile("\\{([\\d\\w]+)\\}");

	private Class<?> clazz = getClass();

	@SuppressWarnings("rawtypes")
	private HashMap<String, Value> rawValues;

	public String getRowkey() {
		Domain domain = clazz.getAnnotation(Domain.class);

		if (domain != null) {
			return buildRowkey(clazz, domain.rowkey());
		}

		return null;
	}

	private String buildRowkey(Class<?> clazz, String rowkeyPattern) {
		Matcher matcher = PATTERN.matcher(rowkeyPattern);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);

				Component component = field.getAnnotation(Component.class);

				if (component.name().equals(matcher.group(1))) {
					try {
						String value = castValue(field);

						if (value != null) {
							matcher.appendReplacement(sb, value);
						}
					} catch (Exception e) {
						LOG.error(Constants.EXCEPTION_PREFIX, e);
					}
				}
			}
		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	private String castValue(Field field) {
		Class<?> fieldClass = field.getType();

		try {
			if (fieldClass.isAssignableFrom(String.class)) {
				return (String) field.get(this);
			} else if (fieldClass.isAssignableFrom(Long.class)) {
				return String.valueOf(field.get(this));
			} else if (fieldClass.isAssignableFrom(Integer.class)) {
				return String.valueOf(field.get(this));
			} else {
				return null;
			}
		} catch (Exception e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	public HashMap<String, Value> getRawValues() {
		return rawValues;
	}

	@SuppressWarnings("rawtypes")
	public void setRawValues(Cell[] cells) {
		rawValues = new HashMap<String, Value>();

		try {
			for (Cell cell : cells) {
				String qualifier = Bytes
						.toString(CellUtil.cloneQualifier(cell));

				buildRawValue(cell, qualifier);
			}
		} catch (Exception e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		}
	}

	private void buildRawValue(Cell cell, String qualifier) {
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);

			if (field.getType() == Value.class) {
				Column column = field.getAnnotation(Column.class);

				if (column == null || !qualifier.equals(column.name())) {
					continue;
				}

				ParameterizedType pType = (ParameterizedType) field
						.getGenericType();
				Type type = pType.getActualTypeArguments()[0];

				if (type == String.class) {
					Value<String> v = new Value<String>();

					v.value = Bytes.toString(CellUtil.cloneValue(cell));

					rawValues.put(qualifier, v);
				}
			}
		}
	}

	public class Value<T> {
		private String family;
		private String qualifier;
		private T value;

		public String getFamily() {
			return family;
		}

		public String getQualifier() {
			return qualifier;
		}

		@SuppressWarnings("unchecked")
		public T getValue() {
			return (T) rawValues.get(qualifier).value;
		}
	}
}