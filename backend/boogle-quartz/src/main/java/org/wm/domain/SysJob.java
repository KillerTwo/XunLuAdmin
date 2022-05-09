package org.wm.domain;

import lombok.Data;
import org.wm.constant.ScheduleConstants;
import org.wm.entity.BaseEntity;


/**
 * 定时任务调度表 sys_job
 */
@Data
public class SysJob extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private Long jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    private String cronExpression;

    /**
     * cron计划策略
     */
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /**
     * 是否并发执行（0允许 1禁止）
     */
    private String concurrent;

    /**
     * 任务状态（0正常 1暂停）
     */
    private String status;
}
