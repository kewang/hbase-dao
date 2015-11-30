package tw.kewang.hbase.domain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Rowkey;

public abstract class AbstractDomain {
	private ColumnFamily family;
	private ColumnQualifier qualifier;

	public String getRowkey() {
		Class<?> clazz = getClass();

		Rowkey rowkey = clazz.getAnnotation(Rowkey.class);

		if (rowkey != null) {
			String rowkeyPattern = rowkey.value();
		}

		for (Field field : clazz.getDeclaredFields()) {
			Annotation ann = field.getAnnotation(Component.class);
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