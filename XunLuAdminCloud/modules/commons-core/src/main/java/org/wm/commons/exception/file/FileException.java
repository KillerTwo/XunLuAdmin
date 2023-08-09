package org.wm.commons.exception.file;


import org.wm.commons.exception.base.BaseException;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     文件信息异常
 *
 * @author dove
 * @date 2023/7/19 21:55
 * @since 1.0
 **/
public class FileException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
