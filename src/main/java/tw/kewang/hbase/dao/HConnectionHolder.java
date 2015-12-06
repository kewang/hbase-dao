package tw.kewang.hbase.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HConnectionHolder {
	private static final Logger LOG = LoggerFactory
			.getLogger(HConnectionHolder.class);
	private static final Configuration HBASE_CONFIG = HBaseSettings
			.getHBaseConf();
	private static final HConnectionHolder INSTANCE = new HConnectionHolder();

	private static HConnection CONNECTION;

	public static HConnectionHolder getInstance() {
		return INSTANCE;
	}

	private HConnectionHolder() {
		try {
			CONNECTION = HConnectionManager.createConnection(HBASE_CONFIG);
		} catch (Exception e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		}
	}

	public HConnection getHConnection() {
		try {
			return CONNECTION;
		} catch (Exception e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		}

		return null;
	}
}