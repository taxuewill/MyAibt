package aibt.will.com.myaibt.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aibt.will.com.myaibt.R;
import aibt.will.com.myaibt.receiver.BtDeviceReceiver;

/**
 * @author 王晶
 * @date 17-6-21-上午9:14
 * @desc
 */
public class BTActivity extends Activity{

    private static final String TAG = "BTActivity";

    private int REQUEST_ENABLE_BT  =  2;

    Button btnEnableBt;
    Button btnListPair,btnDiscover,btnCreateBond,btnStopDiscover,btnConnect;
//    Button btnTest;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BtDeviceReceiver mBtDeviceReceiver = new BtDeviceReceiver();
    BluetoothHeadset mBluetoothHeadset;

    WindowManager windowManager;

    List<BluetoothDevice> listDevices = new ArrayList<BluetoothDevice>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);
        this.registerReceiver(mBtDeviceReceiver, mBtDeviceReceiver.getFilter());


        if (Build.VERSION.SDK_INT >= 23) {
            if (! Settings.canDrawOverlays(BTActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
            }
        }

        init();
    }

    private void init(){
        btnEnableBt = (Button) findViewById(R.id.btnEnableBt);
        btnListPair = (Button) findViewById(R.id.btnListPaired);
        btnDiscover = (Button) findViewById(R.id.btnDiscover);
        btnStopDiscover = (Button) findViewById(R.id.btnStopDiscover);
        btnCreateBond = (Button) findViewById(R.id.btnCreateBond);
        btnConnect = (Button) findViewById(R.id.btnConnect);

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);;

        btnEnableBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothAdapter == null){
                    Log.i(TAG,"Device does not support Bluetooth");
                }else{
                    if(!mBluetoothAdapter.isEnabled()){
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
                    }
                }

            }
        });

        btnListPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"list paired device...");
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                for(BluetoothDevice bluetoothDevice : pairedDevices){
                    Log.i(TAG,"paired device is "+bluetoothDevice.getName()+", address is "+bluetoothDevice.getAddress());
                }
            }
        });

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = mBluetoothAdapter.startDiscovery();

                Log.i(TAG,"start discover bt device ,flag is " + flag);
            }
        });

        btnStopDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = mBluetoothAdapter.cancelDiscovery();

                Log.i(TAG,"cancel discover bt device ,flag is " + flag);
            }
        });

//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View popView = LayoutInflater.from(BTActivity.this).inflate(R.layout.pop_window,null);
//                WindowManager.LayoutParams mFloatWindowParams = null;
//                mFloatWindowParams = new WindowManager.LayoutParams();
//                mFloatWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//                mFloatWindowParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                popView.setFocusableInTouchMode(true);
//                mFloatWindowParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
//                mFloatWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//                mFloatWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//                mFloatWindowParams.x = 0;
//                mFloatWindowParams.y = 0;
//
//                windowManager.addView(popView, mFloatWindowParams);
//            }
//        });
        btnCreateBond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"create bond");
//                List<BluetoothDevice> bluetoothDevices =  mBtDeviceReceiver.getBluetoothDevices();
                BluetoothDevice bluetoothDevice = mBtDeviceReceiver.getHeadSetDev();
//                for(BluetoothDevice bd : bluetoothDevices){
//                    if("WillPhone".equals(bd.getName())){
//                        bluetoothDevice = bd;
//                        break;
//                    }
//                }

                if(bluetoothDevice != null){
                    Log.i(TAG,"find device ,now create bond");
                    boolean flag = bluetoothDevice.createBond();
                    Log.i(TAG,"create bond flag is " + flag);
                }
            }
        });


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"connect to head set");

                // Establish connection to the proxy.
                mBluetoothAdapter.getProfileProxy(BTActivity.this, mProfileListener, BluetoothProfile.HEADSET);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i(TAG,"onActivityResult request code is "+requestCode+", resultCode is "+resultCode);
        switch (resultCode){
            case RESULT_OK:
                Log.i(TAG,"open successfully");
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                for(BluetoothDevice bluetoothDevice : pairedDevices){
                    Log.i(TAG,"paired device is "+bluetoothDevice.getName()+", address is "+bluetoothDevice.getAddress());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBluetoothHeadset != null) {
            mBluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET,mBluetoothHeadset);
        }
        this.unregisterReceiver(mBtDeviceReceiver);


        Log.i(TAG,"onDestroy");
    }


    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.i(TAG,"onServiceConnected, profile:  " + profile);
            if(profile == BluetoothProfile.HEADSET){
                mBluetoothHeadset = (BluetoothHeadset) proxy;
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            Log.i(TAG,"onServiceDisconnected, profile:  " + profile);
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = null;
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        Log.i(TAG,"dispatchKeyEvent is " + event.toString());
        return super.dispatchKeyEvent(event);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown ,keyCode is " + keyCode+" ,event is "+event.toString() );
        Log.i(TAG,parseKeyCode(keyCode));
        return super.onKeyDown(keyCode,event);
    }

    public String parseKeyCode(int keyCode) {
        String ret = "";
        switch (keyCode) {
            case KeyEvent.KEYCODE_POWER:
                // 监控/拦截/屏蔽电源键 这里拦截不了
                ret = "get Key KEYCODE_POWER(KeyCode:" + keyCode + ")";
                break;
            case KeyEvent.KEYCODE_RIGHT_BRACKET:
                // 监控/拦截/屏蔽返回键
                ret = "get Key KEYCODE_RIGHT_BRACKET";
                break;
            case KeyEvent.KEYCODE_MENU:
                // 监控/拦截菜单键
                ret = "get Key KEYCODE_MENU";
                break;
            case KeyEvent.KEYCODE_HOME:
                // 由于Home键为系统键，此处不能捕获
                ret = "get Key KEYCODE_HOME";
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                // 监控/拦截/屏蔽上方向键
                ret = "get Key KEYCODE_DPAD_UP";
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                // 监控/拦截/屏蔽左方向键
                ret = "get Key KEYCODE_DPAD_LEFT";
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                // 监控/拦截/屏蔽右方向键
                ret = "get Key KEYCODE_DPAD_RIGHT";
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                // 监控/拦截/屏蔽下方向键
                ret = "get Key KEYCODE_DPAD_DOWN";
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                // 监控/拦截/屏蔽中方向键
                ret = "get Key KEYCODE_DPAD_CENTER";
                break;
            case KeyEvent.FLAG_KEEP_TOUCH_MODE:
                // 监控/拦截/屏蔽长按
                ret = "get Key FLAG_KEEP_TOUCH_MODE";
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                // 监控/拦截/屏蔽下方向键
                ret = "get Key KEYCODE_VOLUME_DOWN(KeyCode:" + keyCode + ")";
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                // 监控/拦截/屏蔽中方向键
                ret = "get Key KEYCODE_VOLUME_UP(KeyCode:" + keyCode + ")";
                break;
            case 220:
                // case KeyEvent.KEYCODE_BRIGHTNESS_DOWN:
                // 监控/拦截/屏蔽亮度减键
                ret = "get Key KEYCODE_BRIGHTNESS_DOWN(KeyCode:" + keyCode + ")";
                break;
            case 221:
                // case KeyEvent.KEYCODE_BRIGHTNESS_UP:
                // 监控/拦截/屏蔽亮度加键
                ret = "get Key KEYCODE_BRIGHTNESS_UP(KeyCode:" + keyCode + ")";
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                ret = "get Key KEYCODE_MEDIA_PLAY(KeyCode:" + keyCode + ")";
                break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                ret = "get Key KEYCODE_MEDIA_PAUSE(KeyCode:" + keyCode + ")";
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                ret = "get Key KEYCODE_MEDIA_PREVIOUS(KeyCode:" + keyCode + ")";
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                ret = "get Key KEYCODE_MEDIA_PLAY_PAUSE(KeyCode:" + keyCode + ")";
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                ret = "get Key KEYCODE_MEDIA_NEXT(KeyCode:" + keyCode + ")";
                break;
            default:
                ret = "keyCode: "
                        + keyCode
                        + " (http://developer.android.com/reference/android/view/KeyEvent.html)";
                break;
        }
        return ret;
    }


}
