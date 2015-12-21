package tw.kewang.hbase.dao.scan;

import org.apache.hadoop.hbase.client.Scan;

public class ScanBuilder {
	private Scan scan = new Scan();

	public ScanBuilder setCaching(int caching) {
		scan.setCaching(caching);

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

	public Scan create() {
		return scan;
	}
}