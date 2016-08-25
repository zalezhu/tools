package com.zale.shortlink.web.interceptor;

import com.zale.shortlink.redis.RedisCacheDao;
import com.zale.shortlink.web.constants.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import  com.zale.shortlink.exception.NoPermissionException;

public class CustomInterceptorAdapter extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(CustomInterceptorAdapter.class);

	private RedisCacheDao redisCacheDao;

	public CustomInterceptorAdapter(RedisCacheDao redisCacheDao) {
		this.redisCacheDao = redisCacheDao;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug("pre handle===================");
		String token = request.getHeader(WebConstants.TOKEN);
		String aid = request.getHeader(WebConstants.AID);
		if(StringUtils.isEmpty(token)) {
			throw new NoPermissionException("登录信息无效");
		}
		if(StringUtils.isEmpty(aid)) {
			throw new NoPermissionException("登录信息无效");
		}
		String cacheAId = redisCacheDao.read(token,String.class);
		if(cacheAId==null||!cacheAId.equals(aid)){
			throw new NoPermissionException("登录信息无效");
		}
		return true;
	}

}
