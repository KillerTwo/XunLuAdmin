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
}
