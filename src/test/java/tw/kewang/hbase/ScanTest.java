package tw.kewang.hbase;

import java.util.ArrayList;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.kewang.hbase.dao.HBaseSettings;
import tw.kewang.hbase.dao.scan.ScanBuilder;
import tw.kewang.hbase.domain.AbstractDomain;
import tw.kewang.hbase.domain.AbstractDomain.Value;

public class ScanTest extends TestCase {
	private static final Logger LOG = LoggerFactory.getLogger(ScanTest.class);

	@Override
	protected void setUp() throws Exception {
		HBaseSettings.setZooKeeperQuorum("localhost");
	}

	public void testUser1Scan() {
		UserDao uDao = new UserDao();

		ArrayList<AbstractDomain> domains = uDao.scan(new ScanBuilder()
				.create());

		for (AbstractDomain domain : domains) {
			LOG.debug("{}: {}", domain.getClass().getName(), domain.getRowkey());

			for (Entry<String, Value> entry : domain.getRawValues().entrySet()) {
				LOG.debug("{}: {}", entry.getKey(), entry.getValue());
			}
		}
	}
}