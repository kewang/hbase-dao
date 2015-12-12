package tw.kewang.hbase;

import junit.framework.TestCase;
import tw.kewang.hbase.dao.HBaseSettings;

public class DaoTest extends TestCase {
	public void testUser1NoFields() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		UserDao uDao = new UserDao();

		uDao.getByRowkey("xyz_abc");
	}
}