package com.redis.entity;

import java.io.Serializable;

/**
 *
 * @author liyc
 * @date 2017年8月14日 下午2:27:55
 */
public class Member implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1959528436584592183L;

	private String id;
	private String nickname;
	private String password;

	public Member() {
	}

	public Member(String id, String nickname) {
		this.setId(id);
		this.setNickname(nickname);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
