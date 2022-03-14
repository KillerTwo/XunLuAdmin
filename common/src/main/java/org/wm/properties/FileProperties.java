package org.wm.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述
 */
@Data
@Component
public class FileProperties {

    private WmPath path;

    @Data
    public static class WmPath {
        private String avatar;

        private String path;
    }

}
