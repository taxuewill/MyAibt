package aibt.will.com.myaibt.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aibt.will.com.myaibt.R;
import aibt.will.com.myaibt.util.threadutil.ThreadUtil;

import static aibt.will.com.myaibt.view.WifiActivity.WifiCipherType.WIFICIPHER_NOPASS;
import static android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION;

/**
 * @author 王晶
 * @date 17-6-6-下午4:42
 * @desc
 */
public class WifiActivity extends Activity{

    private static final String TAG = "WifiActivity";
    Button btnScan,btnConnect,btnGetConfig;
    Context mContext;
    WifiManager wifiManager;

    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    List<ScanResult> mWifiList;
    String wifiName = "HudControl_AP11";

    ConnectStateReceiver connectStateReceiver = new ConnectStateReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_wifi);
        btnScan = (Button) findViewById(R.id.btnGetResult);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnGetConfig = (Button) findViewById(R.id.btnGetConfig);
        wifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mFilter.addAction(SCAN_RESULTS_AVAILABLE_ACTION);
        mContext.registerReceiver(connectStateReceiver,mFilter);
        init();
    }

    private void init(){
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager.startScan();
//                mWifiList =  removeDuplicateWF(wifiManager.getScanResults());


//                for(int i =0 ; i< mWifiList.size();i++){
//                    Log.i(TAG,"wifi["+i+"] is " +mWifiList.get(i).SSID);
//                }
            }
        });



        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"begin ConnectWifi");
                for(ScanResult scanResult : mWifiList){
                    if(scanResult.SSID.equals(wifiName)){
                        WifiConfiguration config = createWifiInfo(scanResult.SSID,"",WIFICIPHER_NOPASS);

                        int netID = wifiManager.addNetwork(config);
                        if (netID == -1) {
                            Log.e(TAG, "wifiManager.addNetwork failed!");
                        }
                        boolean bRet = wifiManager.enableNetwork(netID, true);
                        if (!bRet) {
                            Log.e(TAG, "wifiManager.enableNetwork failed!");
                        }
                    }
                }
            }
        });

        btnGetConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
                        for (WifiConfiguration config : configs) {
                            Log.i(TAG, "---------------");
                            Log.i(TAG, config.SSID + ",id is " + config.networkId);
                            if("HudControl_AP11".equals(config.SSID)){
                                Log.i(TAG,"find " + config.SSID);
                                boolean flag = wifiManager.removeNetwork(config.networkId);
                                Log.i(TAG,"remove flag " + flag);
                            }
                        }
                    }
                };
                ThreadUtil.getInstance().getThreadPool().submit(runnable);

            }
        });


    }


    private WifiConfiguration createWifiInfo(String SSID, String Password,
                                             WifiCipherType Type) {

        WifiConfiguration config = new WifiConfiguration();
        config.status = WifiConfiguration.Status.CURRENT;
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "\"" + "\"";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
        }
        return config;
    }


    class ConnectStateReceiver extends BroadcastReceiver {
        boolean connectFlag = false;
        boolean pwdWrongFlag = false;


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"ConnectStateReceiver onReceive " + intent);
            if(intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if(wifiInfo != null){
                    Log.i(TAG,"current ssid is "+wifiInfo.getSSID());
                    SupplicantState state = wifiInfo.getSupplicantState();
                    String str = "";
                    int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                    if(linkWifiResult == WifiManager.ERROR_AUTHENTICATING){
                        Log.i(TAG,"passsword not correct！！！！");
                        pwdWrongFlag = true;
                    }
                    if(state == SupplicantState.ASSOCIATED) {
                        str = "关联AP成功";
                    }else if(state.toString().equals("AUTHENTICATING")) {
                        str = "正在验证";
                    }else if(state == SupplicantState.ASSOCIATING) {
                        str = "正在关联AP...";
                    }else if(state == SupplicantState.COMPLETED) {
                        str = "连接成功";
                        connectFlag = true;
                    }else if(state == SupplicantState.INACTIVE) {
                        str = "inactive";
                    }
                    else if(state.toString().equals("DISCONNECTED")){
                        str = "断开连接";
                        connectFlag = false;
                    }
                    Log.i(TAG,"str is " + str);
                }
            }else if(intent.getAction().equals(SCAN_RESULTS_AVAILABLE_ACTION)){
                List<ScanResult> wireless = wifiManager.getScanResults();
                if(wireless != null){
                    for(int i = 0; i < wireless.size(); i++){
                        Log.i(TAG,"wireless["+i+"] is "+wireless.get(i).SSID);
                    }
                }
            }
        }

        public void resetFlags(){
            connectFlag = false;
            pwdWrongFlag = false;
        }

        public boolean getConnectFlag(){
            return connectFlag;
        }

        public boolean getWpdWrongFlag(){
            return pwdWrongFlag;
        }
    }

    private List<ScanResult> removeDuplicateWF(List<ScanResult> oldList){
        List<ScanResult> results = new ArrayList<ScanResult>();
        Set<String> nameSet = new HashSet<String>();
        for(ScanResult result : oldList){
            if(nameSet.contains(result.SSID)){
                continue;
            }else{
                results.add(result);
                nameSet.add(result.SSID);
            }
        }

        return results;

    }
}
