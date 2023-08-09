package org.wm.commons.dto;

import lombok.Data;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/07/19 22:42
 * @since 1.0
**/
@Data
public class MyPageParam {

    /** 当前页 */
    private Integer current = 1;


    /** 每页数量 */
    private Integer pageSize = 10;


}
