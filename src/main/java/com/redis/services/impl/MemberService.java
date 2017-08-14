package com.redis.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.redis.entity.Member;
import com.redis.services.IMember;
import com.redis.services.RedisGenerator;

/**
 * spring-data-redis的序列化类有下面这几个:
 * GenericToStringSerializer: 可以将任何对象泛化为字符串并序列化
 * Jackson2JsonRedisSerializer: 跟JacksonJsonRedisSerializer实际上是一样的
 * JacksonJsonRedisSerializer: 序列化object对象为json字符串
 * JdkSerializationRedisSerializer: 序列化java对象
 * StringRedisSerializer: 简单的字符串序列化
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
				//可以将对象序列化，还可以将对象转换为json字符串并保存到redis中
				JacksonJsonRedisSerializer<Member> serializer= new JacksonJsonRedisSerializer<Member>(Member.class);
				byte[] key = serializer.serialize(member.getId());
				byte[] name = serializer.serialize(member);
				
				//保存String
				/*RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(member.getId());
				byte[] name = serializer.serialize(member.getNickname());*/
				
				return connection.setNX(key, name);
			}
		});
		return result;
	}

	@Override
	public boolean add(final List<Member> list) {
		Assert.notEmpty(list);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				for (Member member : list) {
					byte[] key = serializer.serialize(member.getId());
					byte[] name = serializer.serialize(member.getNickname());
					connection.setNX(key, name);
				}
				return true;
			}
		}, false, true);
		return result;
	}

	@Override
	public void delete(String key) {
		List<String> list = new ArrayList<String>();
		list.add(key);
		delete(list);
	}

	/**
	 * 删除集合 ,依赖key集合
	 */
	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
	}

	@Override
	public boolean update(final Member member) {
		String key = member.getId();
		if (get(key) == null) {
			throw new NullPointerException("数据行不存在, key = " + key);
		}
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(member.getId());
				byte[] name = serializer.serialize(member.getNickname());
				connection.set(key, name);
				return true;
			}
		});
		return result;
	}

	@Override
	public Member get(final String keyId) {
		Member result = redisTemplate.execute(new RedisCallback<Member>() {
			public Member doInRedis(RedisConnection connection) throws DataAccessException {
				//获取json对象
				JacksonJsonRedisSerializer<Member> serializer= new JacksonJsonRedisSerializer<Member>(Member.class);
				byte[] key = serializer.serialize(keyId);
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}
				return serializer.deserialize(value);
				
				/*RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyId);
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}
				String nickname = serializer.deserialize(value);
				return new Member(keyId, nickname);*/
			}
		});
		return result;
	}

}
