package cn.com.cardinfo.splittools.entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsPool {
	public static ExecutorService FIXED_EXECUTORS = Executors.newFixedThreadPool(10);
	public static ExecutorService CACHED_EXECUTORS = Executors.newCachedThreadPool();
}
