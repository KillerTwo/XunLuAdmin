package org.wm.commons.exception.file;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     文件名称超长限制异常类
 * @author dove
 * @date 2023/7/19 21:55
 * @since 1.0
 **/
public class FileNameLengthLimitExceededException extends FileException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
