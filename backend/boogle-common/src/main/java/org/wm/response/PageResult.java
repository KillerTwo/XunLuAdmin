package org.wm.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @创建人 sk
 * @创建时间 2022/3/11
 * @描述
 */
@AllArgsConstructor
@Data
public class PageResult<T> {
    private Long total;

    private List<T> data;

    private Boolean success;

    public static <T> PageResult<T> success(List<T> data, Long total) {
        return new PageResult<>(total, data, true);
    }

    public static <T> PageResult<T> error() {
        return new PageResult<>(0L, Collections.emptyList(), false);
    }
}
