package tw.kewang.hbase;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Component.Type;
import tw.kewang.hbase.annotations.Rowkey;
import tw.kewang.hbase.domain.AbstractDomain;

@Rowkey("aabb_ui__{ui}ddd_{at}__")
public class User1 extends AbstractDomain {
	@Component(type = Type.STRING, name = "ui")
	private String userId;

	@Component(type = Type.STRING, name = "at")
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