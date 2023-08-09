package org.wm.commons.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 功能描述：<功能描述>
 *     分页返回通用对象
 * @author dove
 * @date 2023/7/19 22:00
 * @since 1.0
 **/
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
