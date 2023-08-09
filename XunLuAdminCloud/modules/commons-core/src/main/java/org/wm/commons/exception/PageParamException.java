package org.wm.commons.exception;

import org.wm.commons.exception.base.BaseException;

/**
 * 功能描述：<功能描述>
 *     分页参数异常
 *
 * @author dove 
 * @date 2023/07/19 22:48
 * @since 1.0
**/
public class PageParamException extends BaseException {


    public PageParamException(String module, String code, Object[] args, String defaultMessage) {
        super(module, code, args, defaultMessage);
    }

    public PageParamException(String module, String code, Object[] args) {
        super(module, code, args);
    }

    public PageParamException(String module, String defaultMessage) {
        super(module, defaultMessage);
    }

    public PageParamException(String code, Object[] args) {
        super(code, args);
    }

    public PageParamException(String defaultMessage) {
        super(defaultMessage);
    }
}
