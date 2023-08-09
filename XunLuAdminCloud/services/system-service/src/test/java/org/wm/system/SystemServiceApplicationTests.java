package org.wm.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wm.commons.dto.MyPageParam;
import org.wm.system.mapper.SysConfigMapper;

@SpringBootTest
class SystemServiceApplicationTests {


    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Test
    void contextLoads() {

        MyPageParam myPageParam = new MyPageParam();
        myPageParam.setPageSize(10);
        myPageParam.setCurrent(1);
        var list = sysConfigMapper.selectConfigListPage(myPageParam);

        System.err.println(list);
    }

}
