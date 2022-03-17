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
