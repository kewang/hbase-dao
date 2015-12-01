package tw.kewang.hbase;

public class Main {
	public static void main(String[] args) {
		User1 u1 = new User1().setUserId("user0001")
				.setAccessToken("token0001");

		System.out.println(u1.getRowkey());
	}
}