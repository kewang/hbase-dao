package tw.kewang.hbase.domain;

public abstract class AbstractDomain {
	private Rowkey rowkey;
	private ColumnFamily family;
	private ColumnQualifier qualifier;

	public Rowkey getRowkey() {
		return rowkey;
	}

	public void setRowkey(Rowkey rowkey) {
		this.rowkey = rowkey;
	}

	public ColumnFamily getFamily() {
		return family;
	}

	public void setFamily(ColumnFamily family) {
		this.family = family;
	}

	public ColumnQualifier getQualifier() {
		return qualifier;
	}

	public void setQualifier(ColumnQualifier qualifier) {
		this.qualifier = qualifier;
	}

	public abstract Rowkey composeRowkey();

	public abstract ColumnFamily composeFamily();
}