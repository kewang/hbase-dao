package tw.kewang.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.kewang.hbase.annotations.Table;
import tw.kewang.hbase.dao.Constants;
import tw.kewang.hbase.dao.HConnectionHolder;

@Table(name = "User")
public class UserDao {
	private static final Table TABLE = getTableAnnotation();
	private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);
	private static final HConnection CONNECTION = HConnectionHolder
			.getInstance().getHConnection();

	public User1 getByRowkey(String rowkey) {
		HTableInterface hTableInterface = null;

		try {
			hTableInterface = CONNECTION.getTable(TABLE.name());

			Result results = hTableInterface.get(buildGet(rowkey));

			if (!results.isEmpty()) {

			}
		} catch (Exception e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		} catch (Error e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		} finally {
			closeHTable(hTableInterface);
		}

		return null;
	}

	private static Table getTableAnnotation() {
		Class<?> clazz = UserDao.class;

		Table table = clazz.getAnnotation(Table.class);

		if (table == null) {
			throw new RuntimeException("No table annotation");
		}

		return table;
	}

	private Get buildGet(String rowkey) {
		Get get = new Get(Bytes.toBytes(rowkey));

		try {
			get.setMaxVersions(TABLE.maxVersions());
		} catch (IOException e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		}

		return get;
	}

	private void closeHTable(HTableInterface hTableInterface) {
		if (hTableInterface != null) {
			try {
				hTableInterface.close();
			} catch (Exception e) {
				LOG.error(Constants.EXCEPTION_PREFIX, e);
			} catch (Error e) {
				LOG.error(Constants.EXCEPTION_PREFIX, e);
			}
		}
	}
}