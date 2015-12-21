package tw.kewang.hbase;

import tw.kewang.hbase.annotations.Component;
import tw.kewang.hbase.annotations.Domain;
import tw.kewang.hbase.domain.AbstractDomain;

@Domain(rowkey = "{ui}_{at}_{id}")
public class User2 extends AbstractDomain {
	@Component(name = "ui")
	private String userId;

	@Component(name = "at")
	private String accessToken;

	@Component(name = "id")
	private String id;

	@Component(name = "ct")
	private long createdTime;

	public String getUserId() {
		return userId;
	}

	public User2 setUserId(String userId) {
		this.userId = userId;

		return this;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public User2 setAccessToken(String accessToken) {
		this.accessToken = accessToken;

		return this;
	}

	public String getId() {
		return id;
	}

	public User2 setId(String id) {
		this.id = id;

		return this;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public User2 setCreatedTime(long createdTime) {
		this.createdTime = createdTime;

		return this;
	}
}