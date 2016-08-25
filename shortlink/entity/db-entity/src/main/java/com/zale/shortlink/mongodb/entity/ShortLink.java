package com.zale.shortlink.mongodb.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by Zale on 16/8/22.
 */
public class ShortLink {
    @Id
    private String slink;
    private String llink;
    private Date createDate;
    private Date expireDate;

    public String getSlink() {
        return slink;
    }

    public void setSlink(String slink) {
        this.slink = slink;
    }

    public String getLlink() {
        return llink;
    }

    public void setLlink(String llink) {
        this.llink = llink;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
