package cn.com.cardinfo.cmdcall.dbmodel;

import cn.com.cardinfo.cmdcall.command.Command;
import cn.com.cardinfo.cmdcall.command.CommandGroup;
import cn.com.cardinfo.cmdcall.model.Rule;
import com.sun.tools.javac.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * Created by Zale on 16/8/29.
 */
public class SchTask {
    @Id
    private String id;
    private String name;
    private String desc;
    private Date lastExeTime;
    private Date nextExeTime;
    private List<ChildTask> childTasks;
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

    public List<ChildTask> getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(List<ChildTask> childTasks) {
        this.childTasks = childTasks;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
