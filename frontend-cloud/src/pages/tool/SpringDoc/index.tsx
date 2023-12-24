import React, {useEffect, useState} from 'react';
import { PageContainer } from '@ant-design/pro-layout';


const SpringDoc: React.FC = () => {

  const [height, setHeight] = useState<string>(() => document.documentElement.clientHeight - 94.5 + "px");

  useEffect(() => {
    window.onresize = function temp() {
      setHeight(document.documentElement.clientHeight - 94.5 + "px");
    };
  }, [])
  console.log("height: ", height);
  return (
    <PageContainer>
      <div id="iframe-container" style={{height}}>
        <iframe
          src={"http://localhost:8001/swagger-ui/index.html"}
          frameBorder="no"
          style={{width: "100%", height: "100%"}}
          scrolling="auto"
        />
      </div>
    </PageContainer>
  );
};

export default SpringDoc;
