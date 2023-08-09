package org.wm.commons.exception.file;

import java.io.Serial;

/**
 * 功能描述：<功能描述>
 *     文件名大小限制异常类
 * @author dove
 * @date 2023/7/19 21:55
 * @since 1.0
 **/
public class FileSizeLimitExceededException extends FileException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
