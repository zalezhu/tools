package cn.com.cardinfo.cmdcall.model;

/**
 * Created by Zale on 16/8/29.
 */
public abstract class AbstractRule implements Rule{
    private RuleType ruleType;

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }
}
