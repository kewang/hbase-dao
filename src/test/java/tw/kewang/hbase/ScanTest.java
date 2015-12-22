package tw.kewang.hbase;

import java.util.ArrayList;

import junit.framework.TestCase;
import tw.kewang.hbase.dao.HBaseSettings;
import tw.kewang.hbase.dao.scan.ScanBuilder;
import tw.kewang.hbase.domain.AbstractDomain;

public class ScanTest extends TestCase {
	public void testUser1Scan() {
		HBaseSettings.setZooKeeperQuorum("localhost");

		UserDao uDao = new UserDao();

		ArrayList<AbstractDomain> domains = uDao.scan(new ScanBuilder()
				.create());

		for (AbstractDomain domain : domains) {
			System.out.println("domain: " + domain.getRowkey());
		}
	}
}