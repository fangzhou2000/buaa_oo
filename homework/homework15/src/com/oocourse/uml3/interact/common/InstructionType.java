package com.oocourse.uml3.interact.common;

import com.oocourse.uml3.interact.parser.InputArgumentParser;
import com.oocourse.uml3.models.common.MessageSort;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum InstructionType {
    CLASS_COUNT,
    CLASS_OPERATION_COUNT,
    CLASS_ATTR_COUNT,
    CLASS_OPERATION_VISIBILITY,
    CLASS_ATTR_VISIBILITY,
    CLASS_ATTR_TYPE,
    CLASS_OPERATION_PARAM_TYPE,
    CLASS_ASSO_CLASS_LIST,
    CLASS_IMPLEMENT_INTERFACE_LIST,
    CLASS_TOP_BASE,
    CLASS_INFO_HIDDEN,
    STATE_COUNT,
    SUBSEQUENT_STATE_COUNT,
    TRANSITION_TRIGGER,
    PTCP_OBJ_COUNT,
    INCOMING_MSG_COUNT,
    SENT_MESSAGE_COUNT;

    public static final InputArgumentParser COMMON_PARSER
            = InputArgumentParser.newInstance(new Class<?>[]{InstructionType.class}, String.class);
    public static final Map<InstructionType, InputArgumentParser> INSTRUCTION_PARSERS
            = Collections.unmodifiableMap(new HashMap<InstructionType, InputArgumentParser>() {{
                    put(CLASS_COUNT,
                        InputArgumentParser.newInstance(InstructionType.class));
                    put(CLASS_OPERATION_COUNT,
                        InputArgumentParser.newInstance(InstructionType.class, String.class));
                    put(CLASS_ATTR_COUNT,
                        InputArgumentParser.newInstance(InstructionType.class, String.class));
                    put(CLASS_OPERATION_VISIBILITY,
                        InputArgumentParser.newInstance(InstructionType.class, String.class, String.class));
                    put(CLASS_ATTR_VISIBILITY,
                        InputArgumentParser.newInstance(InstructionType.class, String.class, String.class));
                    put(CLASS_ATTR_TYPE,
                        InputArgumentParser.newInstance(InstructionType.class, String.class, String.class));
                    put(CLASS_OPERATION_PARAM_TYPE,
                        InputArgumentParser.newInstance(InstructionType.class, String.class, String.class));
                    put(CLASS_ASSO_CLASS_LIST,
                        InputArgumentParser.newInstance(InstructionType.class, String.class));
                    put(CLASS_IMPLEMENT_INTERFACE_LIST,
                        InputArgumentParser.newInstance(InstructionType.class, String.class));
                    put(CLASS_TOP_BASE,
                        InputArgumentParser.newInstance(InstructionType.class, String.class));
                    put(CLASS_INFO_HIDDEN,
                        InputArgumentParser.newInstance(InstructionType.class, String.class));
                    put(STATE_COUNT,
                        InputArgumentParser.newInstance(InstructionType.class, String.class));
                    put(SUBSEQUENT_STATE_COUNT,
                        InputArgumentParser.newInstance(InstructionType.class, String.class, String.class));
                    put(TRANSITION_TRIGGER,
                        InputArgumentParser.newInstance(InstructionType.class, String.class, String.class,
                                String.class));
                    put(PTCP_OBJ_COUNT,
                        InputArgumentParser.newInstance(InstructionType.class, String.class));
                    put(INCOMING_MSG_COUNT,
                        InputArgumentParser.newInstance(InstructionType.class, String.class, String.class));
                    put(SENT_MESSAGE_COUNT,
                        InputArgumentParser.newInstance(InstructionType.class, String.class, String.class,
                                MessageSort.class));
                }});
}
