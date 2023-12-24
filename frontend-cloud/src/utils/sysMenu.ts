/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
import type {SYSTEM} from "@/services/system/typings";

export function handleTree(data: SYSTEM.SysMenu[], id?: string, parentId?: string, children?: string): SYSTEM.SysMenu[] {
  const config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    children: children || 'children'
  };

  const childrenListMap = {};
  const nodeIds = {};
  const tree = [];

  for (const d of data) {
    const parentIdData = d[config.parentId];
    if (childrenListMap[parentIdData] == null) {
      childrenListMap[parentIdData] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentIdData].push(d);
  }

  for (const d of data) {
    const parentIdData: string = d[config.parentId];
    if (nodeIds[parentIdData] == null) {
      tree.push(d);
    }
  }

  for (const t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o: SYSTEM.SysMenu) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.children] = childrenListMap[o[config.id]];
    }
    if (o[config.children]) {
      for (const c of o[config.children]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}
