package tw.kewang.hbase.domain;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Domain;
import tw.kewang.hbase.dao.Constants;

public abstract class AbstractDomain {
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractDomain.class);
	private static final Pattern PATTERN = Pattern.compile("\\{([\\d\\w]+)\\}");

	private HashMap<String, HashMap<String, HashMap<Long, byte[]>>> rawMaps;

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

	public HashMap<String, HashMap<String, HashMap<Long, byte[]>>> getRawValues() {
		return rawMaps;
	}

	public void setRawValues(Cell[] cells) {
		rawMaps = new HashMap<String, HashMap<String, HashMap<Long, byte[]>>>();

		for (Cell cell : cells) {
			HashMap<String, HashMap<Long, byte[]>> qualifierMaps = buildQualifierMaps(cell);

			byte[] qualifierArray = cell.getQualifierArray();
			String qualifier = Bytes.toString(qualifierArray);

			HashMap<Long, byte[]> timestampMaps = buildTimestampMaps(cell,
					qualifierMaps, qualifier);

			timestampMaps.put(cell.getTimestamp(), qualifierArray);
		}
	}

	private HashMap<String, HashMap<Long, byte[]>> buildQualifierMaps(Cell cell) {
		String family = Bytes.toString(cell.getFamilyArray());

		HashMap<String, HashMap<Long, byte[]>> qualifierMaps = rawMaps
				.get(family);

		if (qualifierMaps == null) {
			qualifierMaps = new HashMap<String, HashMap<Long, byte[]>>();

			rawMaps.put(family, qualifierMaps);
		}

		return qualifierMaps;
	}

	private HashMap<Long, byte[]> buildTimestampMaps(Cell cell,
			HashMap<String, HashMap<Long, byte[]>> qualifierMaps,
			String qualifier) {
		HashMap<Long, byte[]> timestampMaps = qualifierMaps.get(qualifier);

		if (timestampMaps == null) {
			timestampMaps = new HashMap<Long, byte[]>();

			qualifierMaps.put(qualifier, timestampMaps);
		}

		return timestampMaps;
	}
}