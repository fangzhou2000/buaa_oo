package com.oocourse.uml3.interact.exceptions.user;

/**
 * UML021 和 UML022 规则异常
 */
public class UmlRule008Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule008Exception() {
        super("Failed when check R008, An initial vertex has more than one outgoing transition " +
            "or the outgoing transition from an initial vertex has a trigger or guard.");
    }
}
