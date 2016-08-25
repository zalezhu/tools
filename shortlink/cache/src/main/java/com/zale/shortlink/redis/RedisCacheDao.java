package com.zale.shortlink.redis;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * redis缓存接口
 * 
 * @author Jim
 * 
 * @param <T>
 */
public interface RedisCacheDao {
	public  <T> void save(final String key, final T obj);
	/**
	 * 
	 * @param key 
	 * @param obj
	 * @param expires 单位秒
	 */
	public <T> void save(final String key, final T obj, Long expires);

	public <T> void pushSetItem(final String key, final T obj);
	/**
	 * 保存到列表
	 * @param key
	 * @param obj
	 * @param expires 单位秒
	 */
	public <T> void pushSetItem(final String key, final T obj, Long expires);

	public <T> void removeSetItem(final String key, final T obj);

	<T> T read(String key, Class<T> type);
	<T> void delete(String key, Class<T> type);
	<T> Set<T> readSet(String key, Class<T> type);
	<T> void pushLisItem(String key, T obj);
	<T> void pushListItem(String key, T obj, Long expires);
	<T> void removeListItem(String key, T obj);
	<T> List<T> readList(String key, Class<T> type);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	<T> Long incr(final String key, final Class<T> type);
	
	/**
	 * @param key
	 * @param integer
	 * @return
	 */
	<T> Long incrBy(final String key, final Long integer, final Class<T> type);
}
