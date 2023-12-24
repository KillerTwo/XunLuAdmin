import { Tree, Input } from 'antd';
import {SYSTEM} from "@/services/system/typings";
import React, {useState} from "react";

const { Search } = Input;

type DeptTreeProps = {
  data: SYSTEM.DeptTree[] | [];
  onSelect: (value: any) => void;
}


const loop = (data: any[], searchValue: string = ""): any =>
  data.map(item => {
    const index = item.label.indexOf(searchValue);
    const beforeStr = item.label.substring(0, index);
    const afterStr = item.label.slice(index + searchValue.length);
    const title =
      index > -1 ? (
        <span>
              {beforeStr}
          <span className="site-tree-search-value">{searchValue}</span>
          {afterStr}
            </span>
      ) : (
        <span>{item.label}</span>
      );
    if (item.children) {
      return { title, key: item.key, children: loop(item.children, searchValue) };
    }

    return {
      title: item.label,
      key: item.id,
      children: []
    };
  });


const DeptTree: React.FC<DeptTreeProps> = (props) => {
  const [searchValue, setSearchValue] = useState<string>("");
  // const [expandedKeys, setExpandedKeys] = useState<[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
  // const { data } = props;
  // const treeData = data.map(ele => { return {key: ele.id.toString(), title: ele.label, children: ele.children};})

  const onChange = (e: { target: { value: any; }; }) => {
    const { value } = e.target;
    setSearchValue(value);
    setAutoExpandParent(true);
    console.log(autoExpandParent, searchValue);
  };

  const onSelect = (value: any) => {
    console.log(value[0]);
    props.onSelect(value[0]);
  }

  return (
    <div>
      <Search style={{ marginBottom: 8 }} placeholder="Search" onChange={onChange} />
      <Tree
        defaultExpandAll={true}
        onSelect={onSelect}
        fieldNames={{
          title: "label", key: "id", children: "children"
        }}
        /*treeData={data}*/
      />
    </div>
  );
}
export default DeptTree;
