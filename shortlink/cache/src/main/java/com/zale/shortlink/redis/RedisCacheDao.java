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
public interface RedisCacheDao<T> {
	public void save(final String key, final T obj);
	/**
	 * 
	 * @param key 
	 * @param obj
	 * @param expires 单位秒
	 */
	public void save(final String key, final T obj, Long expires);

	public void pushSetItem(final String key, final T obj);
	/**
	 * 保存到列表
	 * @param key
	 * @param obj
	 * @param expires 单位秒
	 */
	public void pushSetItem(final String key, final T obj, Long expires);

	public void removeSetItem(final String key, final T obj);

	T read(String key, Type type);
	void delete(String key, Type type);
	Set<T> readSet(String key, Type type);
	void pushLisItem(String key, T obj);
	void pushListItem(String key, T obj, Long expires);
	void removeListItem(String key, T obj);
	List<T> readList(String key, Type type);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	Long incr(final String key, final Type type);
	
	/**
	 * @param key
	 * @param integer
	 * @return
	 */
	Long incrBy(final String key, final Long integer, final Type type);
}
