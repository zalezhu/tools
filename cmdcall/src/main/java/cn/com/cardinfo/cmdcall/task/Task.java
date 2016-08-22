package cn.com.cardinfo.cmdcall.task;

import cn.com.cardinfo.cmdcall.model.Result;

/**
 * Created by Zale on 16/8/20.
 */
public interface Task {
    void initTask();

    boolean canExecute();

    Result execute();

    void afterExecute();
}
