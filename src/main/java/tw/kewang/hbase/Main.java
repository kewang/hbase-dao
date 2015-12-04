package tw.kewang.hbase;

public class Main {
	public static void main(String[] args) {
		User1 u1 = new User1().setUserId("user0001")
				.setAccessToken("token0001")
				.setCreatedTime(System.currentTimeMillis());

		User1 u2 = new User1().setUserId("user0003")
				.setAccessToken("token0004")
				.setCreatedTime(System.currentTimeMillis());

		System.out.println(u1.getRowkey());
		System.out.println(u2.getRowkey());
	}
}