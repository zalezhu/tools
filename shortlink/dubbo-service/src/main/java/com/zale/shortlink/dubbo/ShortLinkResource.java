package com.zale.shortlink.dubbo;

import com.cardsmart.inf.entity.RespEntity;

import java.util.Date;

/**
 * Created by Zale on 16/8/23.
 */
public interface ShortLinkResource {
    RespEntity createShortLink(String url, Long expireDate);

    RespEntity removeShortLink(String surl);

    RespEntity getLLink(String surl);

    RespEntity getShortLinks(String slink,String llink,Integer startRecord,Integer maxRecords);

    RespEntity initShortSeq();

    RespEntity updateShortLink(String slink,String llink,Long expireDate);

    RespEntity recordHistory(String slink,Long hitDate,String ip);
}
