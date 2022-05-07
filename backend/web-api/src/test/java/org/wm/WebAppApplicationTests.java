package org.wm;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wm.service.ISysDictDataService;

@SpringBootTest
class WebAppApplicationTests {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Test
    void contextLoads() {
    }


    @Test
    public void testTrasaction() throws Exception {
        sysDictDataService.testMain();
    }

}
