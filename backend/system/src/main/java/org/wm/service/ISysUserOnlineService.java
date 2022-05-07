package org.wm.service;

import org.wm.domain.SysUserOnline;
import org.wm.entity.vo.LoginUser;

/**
 * @创建人 sk
 * @创建时间 2022/1/25
 * @描述 在线用户 服务层
 */
public interface ISysUserOnlineService {
    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    SysUserOnline loginUserToUserOnline(LoginUser user);
}
