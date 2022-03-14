package org.wm.response;

import lombok.Data;

import java.util.List;

/**
 * @创建人 sk
 * @创建时间 2022/3/11
 * @描述
 */
@Data
public class PageResult<T> {
    private Long total;

    private List<T> rows;
}
