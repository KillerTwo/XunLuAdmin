package org.wm.auth.code.sms.handler;

import java.util.Map;

public interface SmsSendCompleteHandler {

    void complete(String templateI, String[] mobiles,  Map<String, Object> params, String result);

}
