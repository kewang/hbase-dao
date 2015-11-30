package tw.kewang.hbase;

import java.lang.reflect.Field;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Rowkey;

public class Main {
	public static void main(String[] args) {
		User1 u1 = new User1();

		u1.getRowkey();

		Class<User1> clazz = User1.class;

		try {
			Rowkey rowkey = clazz.getAnnotation(Rowkey.class);

			if (rowkey != null) {
				System.out.println(rowkey.value());
			}

			Field field = clazz.getDeclaredField("userId");

			Component component = field.getAnnotation(Component.class);

			if (component != null) {
				System.out.println(component.type());
				System.out.println(component.name());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}