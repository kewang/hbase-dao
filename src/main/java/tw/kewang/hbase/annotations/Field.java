package tw.kewang.hbase.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	public enum ComponentType {
		UNDEFINED, ROWKEY, FAMILY, COLUMN
	};

	public enum DataType {
		UNDEFINED, STRING, INTEGER, LONG
	};

	String component() default "";

	DataType dataType() default DataType.UNDEFINED;
}