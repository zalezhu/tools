package cn.com.cardinfo.cmdcall.command;

import cn.com.cardinfo.cmdcall.model.Result;

import java.io.IOException;

/**
 * Created by Zale on 16/8/20.
 */
public interface Command {
    Result toDo() throws IOException;
}
