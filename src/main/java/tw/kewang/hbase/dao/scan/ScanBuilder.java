package tw.kewang.hbase.dao.scan;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.kewang.hbase.dao.Constants;

public class ScanBuilder {
	private static final Logger LOG = LoggerFactory
			.getLogger(ScanBuilder.class);

	private Scan scan = new Scan();

	public ScanBuilder setCaching(int caching) {
		scan.setCaching(caching);

		return this;
	}

	public ScanBuilder setBatch(int batch) {
		scan.setBatch(batch);

		return this;
	}

	public ScanBuilder setMaxVersions(int maxVersions) {
		scan.setMaxVersions(maxVersions);

		return this;
	}

	public ScanBuilder setStartRow(byte[] startRow) {
		scan.setStartRow(startRow);

		return this;
	}

	public ScanBuilder setStopRow(byte[] stopRow) {
		scan.setStopRow(stopRow);

		return this;
	}

	public ScanBuilder setStartRow(String startRow) {
		return setStartRow(Bytes.toBytes(startRow));
	}

	public ScanBuilder setStopRow(String stopRow) {
		return setStopRow(Bytes.toBytes(stopRow));
	}

	public ScanBuilder setStartRow(byte[] startRow, boolean isStop) {
		if (isStop) {
			byte[] startRowBytes = startRow;
			byte[] stopRowBytes = Bytes.copy(startRowBytes);
			int endIndex = stopRowBytes.length - 1;

			stopRowBytes[endIndex] = (byte) (startRowBytes[endIndex] + 1);

			return setStartRow(startRowBytes).setStopRow(stopRowBytes);
		} else {
			return setStartRow(startRow);
		}
	}

	public ScanBuilder setStartRow(String startRow, boolean isStop) {
		return setStartRow(Bytes.toBytes(startRow), isStop);
	}

	public ScanBuilder setReversed(boolean reversed) {
		scan.setReversed(reversed);

		return this;
	}

	public ScanBuilder setFilter(Filter filter) {
		scan.setFilter(filter);

		return this;
	}

	public ScanBuilder setTimeRange(long minStamp, long maxStamp) {
		try {
			scan.setTimeRange(minStamp, maxStamp);
		} catch (Exception e) {
			LOG.error(Constants.EXCEPTION_PREFIX, e);
		}

		return this;
	}

	public ScanBuilder setTimeStamp(long timestamp) {
		scan.setTimeStamp(timestamp);

		return this;
	}

	public Scan create() {
		return scan;
	}
}