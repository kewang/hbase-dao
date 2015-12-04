package tw.kewang.hbase;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Rowkey;
import tw.kewang.hbase.domain.AbstractDomain;

@Rowkey("aabb_ui__{ui}ddd_{at}__{ct}")
public class User1 extends AbstractDomain {
	@Component(name = "ui")
	private String userId;

	@Component(name = "at")
	private String accessToken;

	@Component(classType = Long.class, name = "ct")
	private long createdTime;

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

	public long getCreatedTime() {
		return createdTime;
	}

	public User1 setCreatedTime(long createdTime) {
		this.createdTime = createdTime;

		return this;
	}
}