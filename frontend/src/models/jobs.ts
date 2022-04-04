import {useState} from "react";

export default () => {
  const [jobId, setJobId] = useState<number>(0);

  return {jobId, setJobId}
}
