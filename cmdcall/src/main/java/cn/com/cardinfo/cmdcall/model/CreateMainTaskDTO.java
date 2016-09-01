package cn.com.cardinfo.cmdcall.model;

import cn.com.cardinfo.cmdcall.command.Command;

import java.util.Date;
import java.util.List;

/**
 * Created by Zale on 16/8/29.
 */
public class CreateMainTaskDTO {
    private RuleType ruleType;

    private Date startTime;
    private String cron;
    private Long delay;
    private String desc;
    private String name;


    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }
}
