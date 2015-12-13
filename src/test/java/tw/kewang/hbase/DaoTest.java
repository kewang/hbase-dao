package tw.kewang.hbase;

import junit.framework.TestCase;
import tw.kewang.hbase.dao.HBaseSettings;

public class DaoTest extends TestCase {
	public void testUser1GetByRowkey() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_abc");

		assertEquals("xyz", u1.getUserId());
		assertEquals("abc", u1.getAccessToken());
	}

	public void testUser1GetByRowkeyNotFound() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_a");

		assertNull(u1);
	}

	public void testUser1AndUser2GetByRowkey() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_abc");
		User2 u2 = (User2) uDao.getByRowkey("xyz_abc_kewang");

		assertNotNull(u1);
		assertNotNull(u2);
		assertEquals("kewang", u2.getId());
	}
}