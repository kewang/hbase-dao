package tw.kewang.hbase.domain;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Rowkey;

public abstract class AbstractDomain {
	private static final String REGEX = "\\{([\\d\\w]+)\\}";

	private ColumnFamily family;
	private ColumnQualifier qualifier;

	public String getRowkey() {
		StringBuffer sb = new StringBuffer();

		Class<?> clazz = getClass();

		Rowkey rowkey = clazz.getAnnotation(Rowkey.class);

		if (rowkey != null) {
			Pattern pattern = Pattern.compile(REGEX);

			String rowkeyPattern = rowkey.value();

			Matcher matcher = pattern.matcher(rowkeyPattern);

			while (matcher.find()) {
				for (Field field : clazz.getDeclaredFields()) {
					field.setAccessible(true);

					Component component = field.getAnnotation(Component.class);

					if (component.name().equals(matcher.group(1))) {
						try {
							String s = (String) field.get(this);

							matcher.appendReplacement(sb, s);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			matcher.appendTail(sb);

			return sb.toString();
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
}