package tw.kewang.hbase;

import junit.framework.TestCase;
import tw.kewang.hbase.dao.DaoManager;
import tw.kewang.hbase.dao.HBaseSettings;

public class DaoManagerTest extends TestCase {
	public void testUser1GetByRowkey() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		User1 u1 = (User1) DaoManager.getInstance(UserDao.class).getByRowkey(
				"xyz_abc");

		assertEquals("xyz", u1.getUserId());
		assertEquals("abc", u1.getAccessToken());
	}

	public void testUser1GetByRowkeyNotFound() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		User1 u1 = (User1) DaoManager.getInstance(UserDao.class).getByRowkey(
				"xyz_a");

		assertNull(u1);
	}
}