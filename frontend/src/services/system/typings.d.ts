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
    isFrame?: string;
    target?: string;
  };
  type LoginBody = {
    username?: string;
    password?: string;
    code?: string;
    uuid?: string;
    autoLogin?: boolean;
    type?: string;
    phone?: string;
    uuid?: string;
    code?: string;
    phoneCode?: string;
  }

  type ResetPasswordBody = {
    phone?: string;
    username?: string;
    code?: string;
    uuid?: string;
    phoneCode?: string;
    password?: string;
    rePassword?: string;
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
    phone?: string;
    uuid?: string;
    code?: string;
    phoneCode?: string;
  };

  type PhoneCodeBody = {
    phone?: string;
    uuid?: string;
    code?: string;
  }

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
    status?: string;
    parentName?: string;
    parentId?: number;
    orderNum?: number;
    children?: SysDept[];
  }

  type SysRole = {
    roleId?: number;
    roleName?: string;
    roleKey?: string;
    status?: string;
    createTime?: string;
    remark?: string;
    roleSort?: number;
    menuIds?: number[];
  };

  type PageParams = {
    current?: number | 0;
    pageSize?: number | 10;
  };

  type ResponseResult = {
    msg: string;
    code: number;
    data?: any
  }

  type DeptTree = {
    id: number;
    label: string;
    children?: DeptTree[]
  }

  type SysMenu = {
    menuId?: number;
    menuName?: string;
    parentName?: string;
    parentId?: number;
    orderNum?: number;
    path?: string;
    component?: string;
    query?: string;
    menuType?: string;
    status?: string;
    perms?: string;
    icon?: string;
    children?: SysMenu[];
    isFrame?: string;
    isCache?: string;
    visible?: string;
  }

  type SysPost = {
    postId?: number;
    postName?: string;
    postCode?: string;
    postSort?: string;
    status?: string;
    remark?: string;
  }

  type SysConfig = {
    configId?: number;
    configName?: string;
    configKey?: string;
    configValue?: string;
    configType?: string;
    remark?: string;
  }

  type SysNotice = {
    noticeId?: number;
    noticeTitle?: string;
    noticeType?: string;
    noticeContent?: string;
    status?: string;
    remark?: string;
  }

  type Operlog = {
    operId?: number;
    title?: string;
    businessType?: string;
    method?: string;
    requestMethod?: string;
    operatorType?: number;
    operName?: string;
    deptName?: string;
    operUrl?: string;
    operIp?: string;
    operLocation?: string;
    operParam?: string;
    jsonResult?: string;
    status?: string;
    errorMsg?: string;
    operTime?: string;
  }

  type SysLogininfor = {
    infoId?: number;
    userName?: string;
    status?: string;
    ipaddr?: string;
    loginLocation?: string;
    browser?: string;
    os?: string;
    msg?: string;
    loginTime?: string;
  }

  type SysDictType = {
    dictId?: number;
    dictName?: string;
    dictType?: string;
    status?: string;
    remark?: string;
  }

  type SysDictData = {
    dictCode?: number;
    dictSort?: number;
    dictLabel?: string;
    dictValue?: string;
    dictType?: string;
    cssClass?: string;
    listClass?: string;
    isDefault?: string;
    status?: string;
    remark?: string;
  }

  type SysJob = {
    jobId?: number;
    jobName?: string;
    jobGroup?: string;
    invokeTarget?: string;
    cronExpression?: string;
    misfirePolicy?: string;
    concurrent?: string;
    status?: string;
  }

  type SysJobLog = {
    jobLogId?: number;
    jobName?: string;
    jobGroup?: string;
    invokeTarget?: string;
    jobMessage?: string;
    status?: string;
    exceptionInfo?: string;
    startTime?: string;
    stopTime?: string;
  }



}
