package com.redis.services.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.redis.entity.Member;
import com.redis.services.IMember;
import com.redis.services.RedisGenerator;

/**
 *
 * @author liyc
 * @date 2017年8月14日 下午2:34:14
 */
@Service
public class MemberService extends RedisGenerator<String, Member> implements IMember {

	@Override
	public boolean add(final Member member) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(member.getId());
				byte[] name = serializer.serialize(member.getNickname());
				return connection.setNX(key, name);
			}
		});
		return result;
	}

	@Override
	public boolean add(final List<Member> list) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean update(final Member member) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Member get(final String keyId) {
		Member result = redisTemplate.execute(new RedisCallback<Member>() {
			public Member doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyId);
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}
				String nickname = serializer.deserialize(value);
				return new Member(keyId, nickname);
			}
		});
		return result;
	}

}
