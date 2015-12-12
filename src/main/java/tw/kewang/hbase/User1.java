package tw.kewang.hbase;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Domain;
import tw.kewang.hbase.domain.AbstractDomain;

@Domain(rowkey = "{ui}_{at}")
public class User1 extends AbstractDomain {
	@Component(name = "ui")
	private String userId;

	@Component(name = "at")
	private String accessToken;

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