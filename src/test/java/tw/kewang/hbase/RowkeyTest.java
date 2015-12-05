package tw.kewang.hbase;

import junit.framework.TestCase;

public class RowkeyTest extends TestCase {
	public void testUser1NoFields() {
		User1 u1 = new User1();

		assertEquals("{ui}_{at}", u1.getRowkey());
	}

	public void testUser1HasUserId() {
		User1 u1 = new User1().setUserId("u001");

		assertEquals("u001_{at}", u1.getRowkey());
	}

	public void testUser1HasAccessToken() {
		User1 u1 = new User1().setAccessToken("a001");

		assertEquals("{ui}_a001", u1.getRowkey());
	}

	public void testUser1HasUserIdAndAccessToken() {
		User1 u1 = new User1().setUserId("u001").setAccessToken("a001");

		assertEquals("u001_a001", u1.getRowkey());
	}
}