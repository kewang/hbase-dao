package tw.kewang.hbase;

import java.lang.annotation.Annotation;

public class Main {
	public static void main(String[] args) {
		User1 u1 = new User1();

		Class<User1> clazz = User1.class;

		for (Annotation ann : clazz.getAnnotations()) {
			System.out.println("1");
			System.out.println(ann.toString());
			System.out.println("2");
		}
	}
}