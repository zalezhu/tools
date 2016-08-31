package cn.com.cardinfo.cmdcall.command;

import cn.com.cardinfo.cmdcall.model.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zale on 16/8/29.
 */
public class CommandGroup implements Command {

    private List<Command> commands;

    public CommandGroup(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public Result toDo() throws IOException {
        Result<List> rst = new Result<>();
        List<Result> list = new ArrayList<>();
        rst.setObj(list);
        if (commands != null && !commands.isEmpty()) {
            for (Command command : commands) {
                list.add(command.toDo());
            }
        }
        return rst;
    }
}
