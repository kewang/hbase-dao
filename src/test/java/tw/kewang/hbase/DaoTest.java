package tw.kewang.hbase;

import junit.framework.TestCase;

public class DaoTest extends TestCase {
	public void testUserDao() {
		String rowkey = "u001_a001";

		UserDao uDao = new UserDao();

		User1 u1 = uDao.getByRowkey(rowkey);

		assertEquals(rowkey, u1.getRowkey());
	}
}