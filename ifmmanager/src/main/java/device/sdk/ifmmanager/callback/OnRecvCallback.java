package device.sdk.ifmmanager.callback;

import java.util.LinkedHashMap;

/**
 * Created by qowog on 2017-12-07.
 */

//public interface OnRecvCallback{
//    void onCallback(LinkedHashMap<String,String> inputMap);
//}

public interface OnRecvCallback{
    void onCallback(byte[] inputByte,LinkedHashMap<String,String> inputMap);
}

/**< end of file */
