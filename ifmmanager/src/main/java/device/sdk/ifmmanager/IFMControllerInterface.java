package device.sdk.ifmmanager;

import android.content.Context;
import android.util.Log;

import java.util.LinkedHashMap;

/**
 * Created by olivia on 17. 12. 6.
 */
public class IFMControllerInterface {
    public interface OnIFMControllerInterface {

        //CallBackList
        int HANDLER_RESULT_CODE_OK = 18;
        int HANDLER_RESULT_CODE_FAIL = 19;

        int MSG_RECEIVE_IC_READER_DATA_OK = 20;
        int MSG_RECEIVE_IC_READER_DATA_FAIL = 21;
        int MSG_RECEIVE_IC_READER_DATA_TIMEOUT = 22;


        /**
         * 리더기 초기화
         *
         * @param context    : context
         * @param recvCallback : OnRecvCallback
         */
        void initController(Context context, OnRecvCallback recvCallback);

        /**
         * 리더기 power On
         */
        void powerOnRdi();

        /**
         * 리더기 power Down
         */
        void powerDownRdi();

        /**
         * 리더기 On 상태 여부
         *
         * @return On 상태 여부
         */
        boolean isOpendRdi();

        /**
         * 리더기 정보 요청 command
         *
         * @param inMap   : 요청시 필요한 prameter
         * @param trdType : 구분코드
         */

        void reqIFM(LinkedHashMap<String, String> inMap, byte trdType);
    }


    /**
     * 리더기 return callback
     */
    public interface OnRecvCallback{
        void onCallback(int callbackMsg, byte[] inputByte, LinkedHashMap<String, String> inputMap);
    }

}
