package com.zale.shortlink.dubbo.impl;

import cn.com.cardinfo.sdk.entity.RequestEntity;
import cn.com.cardinfo.sdk.util.OpenApiUtil;
import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Service;
import com.cardsmart.inf.entity.RespEntity;
import com.zale.shortlink.dubbo.ShortLinkResource;
import com.zale.shortlink.entity.Page;
import com.zale.shortlink.exception.ParamValueException;
import com.zale.shortlink.mongodb.entity.ShortLink;
import com.zale.shortlink.mongodb.entity.ShortLinkHitHistory;
import com.zale.shortlink.mongodb.entity.ShortSeq;
import com.zale.shortlink.mongodb.repo.ShortLinkHitHistoryRepo;
import com.zale.shortlink.mongodb.repo.ShortLinkMongoRepo;
import com.zale.shortlink.redis.RedisCacheDao;
import com.zale.shortlink.util.SystemUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zale on 16/8/22.
 */
@Service(version = "1.0", interfaceName = "com.zale.shortlink.dubbo.ShortLinkResource")
public class ShortLinkResourceImpl implements ShortLinkResource {
    @Autowired
    private ShortLinkMongoRepo shortLinkMongoRepo;
    @Autowired
    private ShortLinkHitHistoryRepo shortLinkHitHistoryRepo;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    @Qualifier("redisCache")
    private RedisCacheDao redisCache;
    @Value("${short.domain}")
    private String shortDomain;
    @Value("${openapi.domain}")
    private String openapiDomain;

    @Override
    public RespEntity createShortLink(String url, Long expireDate) {
        try {
            if(StringUtils.isEmpty(url)){
                throw new ParamValueException("长裢不能为空");
            }
            ShortLink sl = new ShortLink();
            sl.setLlink(url);
            Example<ShortLink> example = Example.of(sl);
            ShortLink rst = shortLinkMongoRepo.findOne(example);
            if (rst == null) {
                ShortSeq seq = mongoTemplate
                        .findAndModify(new Query(Criteria.where("seq").ne(null)), new Update().inc("seq", 1), ShortSeq.class);
                sl.setSlink(SystemUtil.DecimalToSystem64(seq.getSeq()));
                DateTime now = new DateTime();
                sl.setCreateDate(now.toDate());
                sl.setExpireDate(expireDate == null ? now.plusYears(50).toDate() : new Date(expireDate));
                shortLinkMongoRepo.save(sl);
                redisCache.save(sl.getSlink(), sl.getLlink());
                rst = sl;
            }
            return new RespEntity("0000", "创建成功", shortDomain + rst.getSlink());
        } catch (Exception e) {
            return new RespEntity("9999", e.getMessage());
        }
    }


    @Override
    public RespEntity createShortLinkSpecial(String code, String params) {
        try {
            if (StringUtils.isEmpty(params)) {
                throw new ParamValueException("参数不能为空");
            }
            if (StringUtils.isEmpty(code)) {
                throw new ParamValueException("服务编码不能为空");
            }
            Map paramMap = JSON.parse(params, HashMap.class);
            RequestEntity entity = new RequestEntity("SMF007", "1.0", "kayou", "short_link", "02f5315f8d8241c6ade55add8608a80a",
                    paramMap);
            entity.setTimestamp(null);
            String llink = OpenApiUtil.genOpenAPiGetUrl(entity, openapiDomain, "74f1c98e74e744ddb809b4931089e663");
            return createShortLink(llink, null);
        } catch (Exception e) {
            return new RespEntity("9999", e.getMessage());
        }
    }

    @Override
    public RespEntity removeShortLink(String surl) {
        shortLinkMongoRepo.delete(surl.substring(surl.lastIndexOf("/") + 1, surl.length()).trim());
        return new RespEntity("0000", "删除成功");
    }

    @Override
    public RespEntity getLLink(String surl) {
        try {
            String llink = null;
            ShortLink sl = shortLinkMongoRepo.findOne(surl);
            if (sl != null) {
                if (sl.getExpireDate().before(new Date())) {
                    throw new RuntimeException("连接已经过期");
                }
                llink = sl.getLlink();
                redisCache.save(surl, llink, 60 * 60L);
            }
            if (!StringUtils.isEmpty(llink)) {
                return new RespEntity("0000", "", llink);
            } else {
                throw new RuntimeException("没有找到对应的连接");
            }
        } catch (Exception e) {
            return new RespEntity("9999", e.getMessage());
        }
    }

    @Override
    public RespEntity getShortLinks(String slink, String llink, Integer startRecord, Integer maxRecords) {
        if (slink == null) {
            slink = "";
        }
        if (llink == null) {
            llink = "";
        }
        Page<ShortLink> page = new Page<ShortLink>();
        List<ShortLink> rst = shortLinkMongoRepo
                .findBySlinkLikeAndLlinkLike(slink, llink, new PageRequest(startRecord/20, maxRecords));
        Integer count = shortLinkMongoRepo.countBySlinkLikeAndLlinkLike(slink, llink);
        page.setResult(rst);
        page.setTotal(count);
        return new RespEntity("0000", "", page);
    }

    @Override
    public RespEntity initShortSeq() {
        List<ShortSeq> dbseq = mongoTemplate.findAll(ShortSeq.class);
        if (dbseq == null || dbseq.isEmpty()) {
            ShortSeq seq = new ShortSeq();
            seq.setSeq(0L);
            mongoTemplate.save(seq);
        }
        return new RespEntity("0000", "初始化成功");
    }

    @Override
    public RespEntity updateShortLink(String slink, String llink, Long expireDate) {
        ShortLink sl = shortLinkMongoRepo.findOne(slink);
        if (llink != null) {
            sl.setLlink(llink);
        }
        if (expireDate != null) {
            sl.setExpireDate(new Date(expireDate));
        }
        shortLinkMongoRepo.save(sl);
        redisCache.save(slink, sl.getLlink(), 60 * 60L);
        return new RespEntity("0000", "更新成功");
    }

    @Override
    public RespEntity recordHistory(String slink, Long hitDate,String ip) {
        if(StringUtils.isEmpty(slink)){
            throw new ParamValueException("短裢不能为空");
        }
        if(hitDate==null){
            throw new ParamValueException("点击时间不能为空");
        }
        ShortLinkHitHistory slhh = new ShortLinkHitHistory();
        slhh.setSlink(slink);
        slhh.setHitDate(hitDate);
        if(!StringUtils.isEmpty(ip)){
            slhh.setIp(ip);
        }
        shortLinkHitHistoryRepo.save(slhh);
        return new RespEntity("0000", "成功");
    }

}
