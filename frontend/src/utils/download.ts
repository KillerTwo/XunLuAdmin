import {request} from "umi";
import FileSaver, {saveAs} from "file-saver";
import {message} from "antd";

export default {
  zip(url: string, name: string) {
    request(`/api/${url}`, {
      method: "get",
      responseType: "blob"
    }).then(async (res) => {
      const isLogin = await blobValidate(res.data);
      if (isLogin) {
        const blob = new Blob([res.data], { type: 'application/zip' })
        this.saveAs(blob, name)
      } else {
        this.printErrMsg(res.data);
      }
    })
  },
  saveAs(text: Blob | string, name?: string, opts?: FileSaver.FileSaverOptions) {
    saveAs(text, name, opts);
  },
  async printErrMsg(data: BlobPart) {
    // @ts-ignore
    if ("text" in data) {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      message.error(rspObj.error)
    }
  }
}

// 验证是否为blob格式
export async function blobValidate(data: BlobPart) {
  try {
    // @ts-ignore
    if ("text" in data) {
      const text = await data.text();
      JSON.parse(text);
      return false;
    }
    return false;
  } catch (error) {
    return true;
  }
}

