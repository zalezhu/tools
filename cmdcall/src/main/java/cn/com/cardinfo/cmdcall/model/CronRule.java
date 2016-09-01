package cn.com.cardinfo.cmdcall.model;

import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;

/**
 * Created by Zale on 16/8/31.
 */
public class CronRule extends  AbstractRule{
    private String cron;

    @Override
    public RuleType getRuleType() {
        return RuleType.cron;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public Date nextExeTime(Date lastRunTime) {
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
        return cronSequenceGenerator.next(lastRunTime);
    }
}
