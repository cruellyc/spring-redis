package com.redis.services;

import java.util.List;

import com.redis.entity.Member;

/**
 *
 * @author  liyc
 * @date 2017年8月14日 下午2:32:02
*/
public interface IMember {
	public boolean add(final Member member);
	public boolean add(final List<Member> list);
	public void delete(String key);
	public boolean update(final Member member);
	public Member get(final String keyId);
}
