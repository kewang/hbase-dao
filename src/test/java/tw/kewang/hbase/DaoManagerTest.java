package tw.kewang.hbase;

import junit.framework.TestCase;
import tw.kewang.hbase.dao.DaoManager;
import tw.kewang.hbase.dao.HBaseSettings;

public class DaoManagerTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		HBaseSettings.setZooKeeperQuorum("localhost");
	}

	public void testUser1GetByRowkey() {
		User1 u1 = (User1) DaoManager.getInstance(UserDao.class).getByRowkey(
				"xyz_abc");

		assertEquals("xyz", u1.getUserId());
		assertEquals("abc", u1.getAccessToken());
	}

	public void testUser1GetByRowkeyNotFound() {
		User1 u1 = (User1) DaoManager.getInstance(UserDao.class).getByRowkey(
				"xyz_a");

		assertNull(u1);
	}
}