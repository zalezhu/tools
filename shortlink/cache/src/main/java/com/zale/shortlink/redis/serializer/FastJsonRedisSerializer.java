package com.zale.shortlink.redis.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.ThreadLocalCache;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.lang.reflect.Type;

public class FastJsonRedisSerializer<T> implements RedisSerializer<T>{

	private final Type javaType;
	
	
	public FastJsonRedisSerializer(Type javaType) {
		super();
		this.javaType = javaType;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		return JSON.toJSONBytes(t, SerializerFeature.WriteNullListAsEmpty);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if((bytes == null || bytes.length == 0)){
			return null;
		}
		return (T)JSON.parseObject(bytes,0, bytes.length, ThreadLocalCache.getUTF8Decoder(),javaType,Feature.IgnoreNotMatch);
	}

}
