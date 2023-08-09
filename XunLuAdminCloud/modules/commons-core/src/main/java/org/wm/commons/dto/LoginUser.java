package org.wm.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 功能描述：<功能描述>
 *     登录用户信息封装
 *
 * @author dove
 * @date 2023/7/12 23:44
 * @since 1.0
 **/
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 权限列表
     */
    private Set<String> permissions;



    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码  TODO 需要处理密码传输问题
     */
    // @JsonIgnore
    private String password;

    /**
     * 盐加密
     */
    private String salt;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;


    /**
     *  角色key列表
     */
    private Set<String> roles;

    public LoginUser() {
    }

    public LoginUser(TransferDataMap dataMap) {
        this.userId = dataMap.getLong("userId");
        this.deptId = dataMap.getLong("deptId");
        this.permissions = dataMap.getSetString("permissions");
        this.username = dataMap.getString("userName");
        this.nickName = dataMap.getString("nickName");
        this.email = dataMap.getString("email");
        this.phonenumber = dataMap.getString("phonenumber");
        this.sex = dataMap.getString("sex");
        this.avatar = dataMap.getString("avatar");
        this.status = dataMap.getString("status");
        this.delFlag = dataMap.getString("delFlag");
        this.roleIds = dataMap.getLongArray("roleIds");
        this.postIds = dataMap.getLongArray("postIds");
        this.roles = dataMap.getSetString("roles");
        this.password = dataMap.getString("password");
    }

    public LoginUser(Long userId, Long deptId, String token, Long loginTime, Long expireTime, String ipaddr, String loginLocation, String browser, String os, Set<String> permissions, String username, String nickName, String email, String phonenumber, String sex, String avatar, String password, String salt, String status, String delFlag, String loginIp, Date loginDate, Long[] roleIds, Long[] postIds, Set<String> roles) {
        this.userId = userId;
        this.deptId = deptId;
        this.token = token;
        this.loginTime = loginTime;
        this.expireTime = expireTime;
        this.ipaddr = ipaddr;
        this.loginLocation = loginLocation;
        this.browser = browser;
        this.os = os;
        this.permissions = permissions;
        this.username = username;
        this.nickName = nickName;
        this.email = email;
        this.phonenumber = phonenumber;
        this.sex = sex;
        this.avatar = avatar;
        this.password = password;
        this.salt = salt;
        this.status = status;
        this.delFlag = delFlag;
        this.loginIp = loginIp;
        this.loginDate = loginDate;
        this.roleIds = roleIds;
        this.postIds = postIds;
        this.roles = roles;
    }
}
