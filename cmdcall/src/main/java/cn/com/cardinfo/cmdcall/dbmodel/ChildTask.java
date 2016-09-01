package cn.com.cardinfo.cmdcall.dbmodel;

import cn.com.cardinfo.cmdcall.command.Command;
import cn.com.cardinfo.cmdcall.model.Result;
import com.sun.tools.javac.util.List;

/**
 * Created by Zale on 16/8/31.
 */
public class ChildTask {
    private List<ChildTask> childTasks;
    private Result result;
    private String nodeId;
    private String pNodeId;
    private Command command;
    private String lockBy;
    private int status;
    private int retry;
    private int failedTimes;

    public List<ChildTask> getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(List<ChildTask> childTasks) {
        this.childTasks = childTasks;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getpNodeId() {
        return pNodeId;
    }

    public void setpNodeId(String pNodeId) {
        this.pNodeId = pNodeId;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getLockBy() {
        return lockBy;
    }

    public void setLockBy(String lockBy) {
        this.lockBy = lockBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getFailedTimes() {
        return failedTimes;
    }

    public void setFailedTimes(int failedTimes) {
        this.failedTimes = failedTimes;
    }
}
