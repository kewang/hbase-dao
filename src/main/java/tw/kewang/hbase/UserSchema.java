package tw.kewang.hbase;

import tw.kewang.hbase.domain.AbstractDomain;
import tw.kewang.hbase.schema.AbstractSchema;

public class UserSchema extends AbstractSchema {
	@Override
	public AbstractDomain separate() {
		return null;
	}
}