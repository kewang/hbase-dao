package tw.kewang.hbase;

import junit.framework.TestCase;
import tw.kewang.hbase.dao.HBaseSettings;

public class DaoTest extends TestCase {
	public void testUser1NoFields() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_abc");

		System.out.println(u1.getUserId());
		System.out.println(u1.getAccessToken());
	}
}