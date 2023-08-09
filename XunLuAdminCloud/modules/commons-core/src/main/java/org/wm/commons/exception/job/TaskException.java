package org.wm.commons.exception.job;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     计划策略异常
 * @author dove
 * @date 2023/7/19 21:56
 * @since 1.0
 **/
public class TaskException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    private Code code;

    public TaskException(String msg, Code code) {
        this(msg, code, null);
    }

    public TaskException(String msg, Code code, Exception nestedEx) {
        super(msg, nestedEx);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public enum Code {
        TASK_EXISTS, NO_TASK_EXISTS, TASK_ALREADY_STARTED, UNKNOWN, CONFIG_ERROR, TASK_NODE_NOT_AVAILABLE
    }
}