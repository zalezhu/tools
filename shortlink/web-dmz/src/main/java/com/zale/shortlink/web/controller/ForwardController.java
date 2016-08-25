package com.zale.shortlink.web.controller;

import com.cardsmart.esbsdk.util.DubboInvoker;
import com.zale.shortlink.redis.RedisCacheDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Zale on 16/8/22.
 */
@Controller
public class ForwardController {
    private Logger logger = LoggerFactory.getLogger(ForwardController.class);
    @Autowired
    private DubboInvoker dubboInvoker;
    @Autowired
    @Qualifier("redisCache")
    private RedisCacheDao redisCache;

    private ExecutorService executorService = Executors.newFixedThreadPool(100);
    @RequestMapping("{id}")
    public String forward(HttpServletRequest request,@PathVariable("id") String id){
        String llink = redisCache.read(id, String.class);
        final String ip = getRemoteIp(request);
        final String slink = id;
        if (StringUtils.isEmpty(llink)) {
            Map<String, String> map = new HashMap<>();
            map.put("seq", id);
            Map<String, Object> rstMap = (Map<String, Object>) dubboInvoker.invoke("SLK_001", "1.0", map);
            if (!"0000".equals(rstMap.get("key"))) {
                return "/error/404.html";
            }else{
                llink = (String) rstMap.get("ext");
            }
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("slink", slink);
                    params.put("hitDate", new Date().getTime());
                    if(!StringUtils.isEmpty(ip)){
                        params.put("ip",ip);
                    }
                    dubboInvoker.invoke("SLK_007", "1.0", params);
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            }
        });
        return "redirect:" + llink;
    }

    private String getRemoteIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
