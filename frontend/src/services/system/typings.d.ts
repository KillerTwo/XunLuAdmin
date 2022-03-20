import React from "react";

declare namespace SYSTEM {
  type Router = {
    name?: string;
    path?: string;
    hideInMenu?: boolean;
    redirect?: string;
    component?: string;
    routes?: Router[];
    icon?: React.ReactNode;
    iconName?: string;
  };
  type LoginBody = {
    username?: string;
    password?: string;
    code?: string;
    uuid?: string;
    autoLogin?: boolean;
    type?: string;
  }
  type LoginResult = {
    code?: number;
    msg?: string
    data?: {
      token?: string
    }
  }
  type LoginParams = {
    username?: string;
    password?: string;
    autoLogin?: boolean;
    type?: string;
  };

  type SysUser = {
    userId?: number;
    userName?: string;
    nickName?: string;
    email?: string;
    phonenumber?: string;
    sex?: string;
    status?: string;
    createTime?: string;
    dept?: SysDept;
    remark?: string;
    deptId?: any;
    postId?: any;
    roleId?: any;
    roleIds?: number[];
    postIds?: number[];
  }

  type SysDept = {
    deptId?: number;
    deptName?: string;
    leader?: string;
    phone?: string;
    email?: string;
    status: string;
    parentName?: string;
  }

  type SysRole = {
    roleId: number;
    roleName: string;
    roleKey: string;
    status: string;
  };

  type PageParams = {
    current?: number;
    pageSize?: number;
  };

  type ResponseResult = {
    msg: string;
    code: number;
    data?: any
  }

}
