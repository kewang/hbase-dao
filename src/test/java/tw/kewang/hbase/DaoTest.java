package tw.kewang.hbase;

import junit.framework.TestCase;
import tw.kewang.hbase.dao.HBaseSettings;

public class DaoTest extends TestCase {
	public void testUser1NoFields() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_abc");

		assertEquals("xyz", u1.getUserId());
		assertEquals("abc", u1.getAccessToken());
	}
}