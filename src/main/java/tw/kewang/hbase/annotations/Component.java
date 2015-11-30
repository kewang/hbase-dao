package tw.kewang.hbase.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
	public enum Type {
		UNDEFINED, STRING, INTEGER, LONG
	};

	String name() default "";

	Type type() default Type.UNDEFINED;
}