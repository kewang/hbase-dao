package tw.kewang.hbase.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class HBaseSettings {
	/* Java main method args keys */
	public static final String ARGS_KEY_ZOOKEEPER = "zookeeper";
	public static final String ARGS_KEY_HDFS = "hdfs";

	/* HBase ENV keys */
	public static final String HBASE_ENV_KEY_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	public static final String HBASE_ENV_ROOT_DIR = "hbase.root.dir";

	private static String zooKeeperQuorum;
	private static String hdfsRootDir;
	private static Configuration hbaseConf;

	public static String getZooKeeperQuorum() {
		return zooKeeperQuorum;
	}

	public static void setZooKeeperQuorum(String zooKeeperQuorum) {
		HBaseSettings.zooKeeperQuorum = zooKeeperQuorum;
	}

	public static String getHdfsRootdir() {
		return hdfsRootDir;
	}

	public static void setHdfsRootdir(String hdfsRootdir) {
		HBaseSettings.hdfsRootDir = hdfsRootdir;
	}

	public static synchronized Configuration getHBaseConf() {
		if (null == hbaseConf) {
			hbaseConf = HBaseConfiguration.create();

			// Setting hbase, if data is null, use hbase-site.xml
			if (zooKeeperQuorum != null && !"".equals(zooKeeperQuorum)) {
				hbaseConf.set(HBASE_ENV_KEY_ZOOKEEPER_QUORUM, zooKeeperQuorum);
			} else {
				hbaseConf.set(HBASE_ENV_KEY_ZOOKEEPER_QUORUM, "localhost");
			}

			// Setting hbase.root.dir
			if (hdfsRootDir != null && !"".equals(hdfsRootDir)) {
				hbaseConf.set(HBASE_ENV_ROOT_DIR, hdfsRootDir);
			}
		}

		return hbaseConf;
	}
}