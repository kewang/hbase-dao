package tw.kewang.hbase;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;
import tw.kewang.hbase.dao.HBaseSettings;
import tw.kewang.hbase.domain.AbstractDomain.Value;

public class DaoTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(DaoTest.class);

	@Override
	protected void setUp() throws Exception {
		HBaseSettings.setZooKeeperQuorum("localhost");
	}

	public void testUser1GetByRowkey() {
		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_abc");

		assertEquals("xyz", u1.getUserId());
		assertEquals("abc", u1.getAccessToken());
	}

	public void testUser1GetByRowkeyNotFound() {
		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_a");

		assertNull(u1);
	}

	public void testUser1AndUser2GetByRowkey() {
		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_abc");
		User2 u2 = (User2) uDao.getByRowkey("xyz_abc_kewang");

		assertNotNull(u1);
		assertNotNull(u2);
		assertEquals("kewang", u2.getId());
	}

	public void testUser1GetRawValues() {
		UserDao uDao = new UserDao();

		User1 u1 = (User1) uDao.getByRowkey("xyz_abc");

		assertNotNull(u1.getRawValues());

		for (Entry<String, Value> entry : u1.getRawValues().entrySet()) {
			LOG.debug(entry.getValue().toString());
		}

		assertEquals("kewang", u1.getNickname().getValue());
	}
}