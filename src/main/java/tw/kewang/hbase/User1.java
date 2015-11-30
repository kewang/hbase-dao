package tw.kewang.hbase;

import tw.kewang.hbase.annotations.Field;
import tw.kewang.hbase.annotations.Field.DataType;
import tw.kewang.hbase.annotations.Rowkey;
import tw.kewang.hbase.domain.AbstractDomain;

@Rowkey("{ui}_{at}")
public class User1 extends AbstractDomain {
	@Field(dataType = DataType.STRING, component = "ui")
	private String userId;

	@Field(dataType = DataType.STRING, component = "at")
	private String accessToken;

	public String getUserId() {
		return userId;
	}

	public User1 setUserId(String userId) {
		this.userId = userId;

		return this;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public User1 setAccessToken(String accessToken) {
		this.accessToken = accessToken;

		return this;
	}
}