package tw.kewang.hbase.domain;

public abstract class Rowkey {
	private byte[] rowkey;

	public abstract void compose();
}