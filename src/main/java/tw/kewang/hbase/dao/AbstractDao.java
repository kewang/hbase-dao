package tw.kewang.hbase.dao;

import tw.kewang.hbase.schema.AbstractSchema;

public class AbstractDao {
	public AbstractSchema get(String rowkey) {
		return null;
	}

	public AbstractSchema get(byte[] rowkey) {
		return null;
	}

	public AbstractSchema scan(String rowkey) {
		return null;
	}

	public AbstractSchema scan(byte[] rowkey) {
		return null;
	}
}