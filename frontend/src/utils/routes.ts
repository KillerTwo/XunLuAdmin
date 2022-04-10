import {SYSTEM} from "@/services/system/typings";
import React from "react";
// import iconMapping from "@/utils/iconMap";
// import {dynamic} from "umi";

import * as allIcons from '@ant-design/icons';

export function handleIconAndComponent(routes: SYSTEM.Router[]) {
  return routes.map(ele => {
    if (ele.iconName) {
      // const iconNode: React.ReactNode = iconMapping[ele.iconName];
      const iconNode: React.ReactNode = React.createElement(allIcons[ele.iconName]);
      if (iconNode) {
        ele.icon = iconNode;
      }
      if (ele.isFrame === "0") {
        ele.target = "_blank";
      }
    }
    delete ele.component
    /*if (ele.component) {
      // const component = (ele?.routes||[])[0].component;
      ele.component = dynamic({
        loader: async function() {
          const { default: TableList } = await import('@/pages/TableList');
          return TableList;
        },
      });
    }*/
    if (ele.routes && ele.routes.length > 0) {
      handleIconAndComponent(ele.routes);
    }
    return ele;
  })
}


export function iconSelect() {
  // const iconNode: React.ReactNode = React.createElement(allIcons[ele.iconName]);
  const enums = {};
  const showType = typeof allIcons["IdcardOutlined"];
  for (const allIconsKey in allIcons) {
    const item = allIcons[allIconsKey];
    if (typeof item === showType && item.displayName !== 'AntdIcon') {
      const iconNode: React.ReactNode = React.createElement(allIcons[allIconsKey]);
      if (iconNode) {
        enums[allIconsKey] = iconNode
      }
    }
  }
  return enums;
}
