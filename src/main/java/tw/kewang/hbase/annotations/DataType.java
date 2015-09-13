package tw.kewang.hbase.annotations;

public @interface DataType {
	public enum Type {
		STRING, INTEGER, LONG
	};

	Type dataType() default Type.STRING;
}