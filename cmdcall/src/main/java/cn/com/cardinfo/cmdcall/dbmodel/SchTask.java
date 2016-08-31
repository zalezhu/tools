package cn.com.cardinfo.cmdcall.dbmodel;

import cn.com.cardinfo.cmdcall.command.Command;
import cn.com.cardinfo.cmdcall.command.CommandGroup;
import cn.com.cardinfo.cmdcall.model.Rule;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * Created by Zale on 16/8/29.
 */
public class SchTask {
    @Id
    private String id;
    private Date lastExeTime;
    private Date nextExeTime;
    private Command command;
    private String lockBy;
    private int failedTimes;
    private int status;
    private Rule rule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastExeTime() {
        return lastExeTime;
    }

    public void setLastExeTime(Date lastExeTime) {
        this.lastExeTime = lastExeTime;
    }

    public Date getNextExeTime() {
        return nextExeTime;
    }

    public void setNextExeTime(Date nextExeTime) {
        this.nextExeTime = nextExeTime;
    }

    public String getLockBy() {
        return lockBy;
    }

    public void setLockBy(String lockBy) {
        this.lockBy = lockBy;
    }

    public int getFailedTimes() {
        return failedTimes;
    }

    public void setFailedTimes(int failedTimes) {
        this.failedTimes = failedTimes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
