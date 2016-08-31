package cn.com.cardinfo.cmdcall.model;

import cn.com.cardinfo.cmdcall.command.Command;
import java.util.List;

/**
 * Created by Zale on 16/8/29.
 */
public class CreateSchTaskDTO {
    private Rule rule;
    private Command command;

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
