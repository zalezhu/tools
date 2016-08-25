package com.zale.shortlink.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by Zale on 16/8/25.
 */
public class ShortLinkHitHistory {
    @Indexed
    private String slink;
    private Long hitDate;
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSlink() {
        return slink;
    }

    public void setSlink(String slink) {
        this.slink = slink;
    }

    public Long getHitDate() {
        return hitDate;
    }

    public void setHitDate(Long hitDate) {
        this.hitDate = hitDate;
    }
}
