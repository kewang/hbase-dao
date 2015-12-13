package tw.kewang.hbase.domain;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Domain;
import tw.kewang.hbase.dao.Constants;

public abstract class AbstractDomain {
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractDomain.class);
	private static final Pattern PATTERN = Pattern.compile("\\{([\\d\\w]+)\\}");

	private ColumnFamily family;
	private ColumnQualifier qualifier;

	public String getRowkey() {
		Class<?> clazz = getClass();

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

	public ColumnFamily getFamily() {
		return family;
	}

	public void setFamily(ColumnFamily family) {
		this.family = family;
	}

	public ColumnQualifier getQualifier() {
		return qualifier;
	}

	public void setQualifier(ColumnQualifier qualifier) {
		this.qualifier = qualifier;
	}

	public Object getRawValues() {
		return null;
	}
}