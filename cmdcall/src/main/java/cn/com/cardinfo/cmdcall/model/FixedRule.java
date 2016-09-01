package cn.com.cardinfo.cmdcall.model;

import java.util.Date;

/**
 * Created by Zale on 16/8/31.
 */
public class FixedRule extends AbstractRule{
    private Long delay;
    @Override
    public Date nextExeTime(Date lastRunTime) {
        return new Date(lastRunTime.getTime()+delay);
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    @Override
    public RuleType getRuleType() {
        return RuleType.fixed;
    }
}
