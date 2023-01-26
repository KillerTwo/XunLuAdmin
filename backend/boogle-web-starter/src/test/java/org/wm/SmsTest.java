package org.wm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wm.authentication.sms.context.CodeSmsStrategy;

@SpringBootTest
public class SmsTest {


    @Test
    public void testSmsSend() {
        CodeSmsStrategy.TEN_CENT.smsProcess();
    }

}
