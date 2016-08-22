package com.zale.shortlink.redis;

public interface RedisCacheService<T> {
	public void saveCache(final String key, final T obj);

	public void saveCache(final String key, final T obj, Long expires);

	public T readCache(final String key);

	public void deleteCache(final String key);
}
