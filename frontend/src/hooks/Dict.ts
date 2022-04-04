import {SYSTEM} from "@/services/system/typings";
import {useCallback, useEffect, useState} from "react";
import {sysDictDataListByType} from "@/services/system/sysDictData";

export const useDict = (dictType: string): {label: string| undefined, value: string| undefined}[] => {
  const [dictList, setDictList] = useState<SYSTEM.SysDictData[]>([]);

  const execute = useCallback(() => {
    return sysDictDataListByType(dictType).then(res => {
      setDictList(res.data);
    })
  }, [dictType]);

  useEffect(() =>{
    execute();
  }, [])

  const options = dictList.map(ele => {
    return {
      label: ele.dictLabel,
      value: ele.dictValue
    }
  })

  return options
}
