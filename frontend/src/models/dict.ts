import {useState} from "react";

export default () => {
  const [dictType, setDictType] = useState<string>("")
  /*const initDictType = useCallback((dictType: string) => {
    setDictType(dictType);
  }, []);*/
  return {
    dictType, setDictType
  }
}
