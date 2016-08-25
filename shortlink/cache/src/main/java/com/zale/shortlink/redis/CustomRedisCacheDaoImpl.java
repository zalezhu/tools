package com.zale.shortlink.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("redisCache")
public class CustomRedisCacheDaoImpl implements RedisCacheDao{
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	private RedisSerializer defaultSerializer;
	@Autowired
	public CustomRedisCacheDaoImpl(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(
			RedisTemplate<Serializable, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	public void init() throws Exception {
		this.defaultSerializer = this.redisTemplate.getDefaultSerializer();
	}
	
	@Override
	public <T> void  save(final String key, final T obj, final Long expires) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				connection.set(k, defaultSerializer.serialize(obj));
				if(expires != null && expires > 0 ){
					connection.expire(k, expires);
				}
				return null;
			}
		});
	}

	@Override
	public <T>  void save(final String key, final T obj) {
		this.save(key, obj, null);
	}

	public <T>  T read(final String key) {
		return redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				if (connection.exists(k)) {
					return (T) defaultSerializer.deserialize(connection.get(k));
				}
				return null;
			}
		});
	}

	public void delete(final String key) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				connection.expire(k, 0);
				return null;
			}
		});
	}

	@Override
	public <T>  void pushSetItem(final String key, final T obj) {
		this.pushSetItem(key, obj, null);
	}

	@Override
	public <T>  void pushSetItem(final String key, final T obj, final Long expires) {
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				byte[] v = defaultSerializer.serialize(obj);
				if(!connection.sIsMember(k, v)){
					connection.sAdd(k, v);
				}
				if(expires != null && expires > 0 ){
					connection.expire(k, expires);
				}
				return null;
			}
		});
		
	}

	@Override
	public <T>  void removeSetItem(final String key, final T obj) {
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				byte[] v = defaultSerializer.serialize(obj);
				if(!connection.sIsMember(k, v)){
					connection.sRem(k, v);
				}
				return null;
			}
		});
	}

	public <T>  Set<T> readSet(final String key) {
		return redisTemplate.execute(new RedisCallback<Set<T>>() {
			@Override
			public Set<T> doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				if (connection.exists(k)) {
					Set<byte[]> value = connection.sMembers(k);
					Set<T> rst = new HashSet<T>();
					for(byte[] b:value){
						rst.add((T) defaultSerializer.deserialize(b));
					}
					return rst;
				}
				return null;
			}
		});
	}

	@Override
	public <T>  T read(String key, Class<T> type) {
		return this.read(key);
	}

	@Override
	public <T>  void delete(String key, Class<T> type) {
		this.delete(key);
	}

	@Override
	public <T>  Set<T> readSet(String key, Class<T> type) {
		return this.readSet(key);
	}
	
	@Override
	public <T>  void pushLisItem(final String key, final T obj) {
		this.pushListItem(key, obj, null);
	}

	@Override
	public <T>  void pushListItem(final String key, final T obj, final Long expires) {
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				byte[] v = defaultSerializer.serialize(obj);
					connection.lPush(k, v);
				if(expires != null && expires > 0 ){
					connection.expire(k, expires);
				}
				return null;
			}
		});
		
	}

	@Override
	public <T>  void removeListItem(final String key, final T obj) {
		redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				byte[] v = defaultSerializer.serialize(obj);
				connection.lRem(k,1, v);
				return null;
			}
		});
	}
	@Override
	public <T>  List<T> readList(final String key,final Class<T> type) {
		return redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				if (connection.exists(k)) {
					byte[] value = connection.get(k);
					return (List<T>)defaultSerializer.deserialize(value);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
				}
				return null;
			}
		});
	}

	@Override
	public <T>  Long incr(final String key,final Class<T> type) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				return connection.incr(k);
			}
		});
	}

	@Override
	public <T>  Long incrBy(final String key, final Long integer,final Class<T> type) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] k = defaultSerializer.serialize(key);
				return connection.incrBy(k, integer);
			}
		});
	}
}
