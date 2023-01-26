package org.wm.authentication.sms.handler;

import java.util.Map;

public interface SmsSendCompleteHandler {

    void complete(String templateI, String[] mobiles,  Map<String, Object> params, String result);

}
