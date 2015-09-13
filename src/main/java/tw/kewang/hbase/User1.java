package tw.kewang.hbase;

import tw.kewang.hbase.annotations.DataType;
import tw.kewang.hbase.annotations.DataType.Type;
import tw.kewang.hbase.domain.AbstractDomain;
import tw.kewang.hbase.domain.ColumnFamily;
import tw.kewang.hbase.domain.ColumnQualifier;
import tw.kewang.hbase.domain.Rowkey;

public class User1 extends AbstractDomain {
	@DataType(dataType = Type.STRING)
	private ColumnQualifier userId;

	@DataType(dataType = Type.STRING)
	private ColumnQualifier accessToken;

	@DataType(dataType = Type.LONG)
	private ColumnQualifier createdTime;

	@Override
	public Rowkey composeRowkey() {
		return null;
	}

	@Override
	public ColumnFamily composeFamily() {
		return null;
	}
}