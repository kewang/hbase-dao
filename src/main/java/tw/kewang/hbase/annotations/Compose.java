package tw.kewang.hbase.annotations;

public @interface Compose {
	String rowkey() default "";
}