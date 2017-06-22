package aibt.will.com.myaibt.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

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
    Button btnListPair,btnDiscover,btnConnect,btnStopDiscover;
    Button btnTest;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BtDeviceReceiver mBtDeviceReceiver = new BtDeviceReceiver();

    WindowManager windowManager;


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
        btnTest = (Button) findViewById(R.id.btnTest);

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

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popView = LayoutInflater.from(BTActivity.this).inflate(R.layout.pop_window,null);
                WindowManager.LayoutParams mFloatWindowParams = null;
                mFloatWindowParams = new WindowManager.LayoutParams();
                mFloatWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
                mFloatWindowParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_FULLSCREEN;
                popView.setFocusableInTouchMode(true);
                mFloatWindowParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
                mFloatWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                mFloatWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                mFloatWindowParams.x = 0;
                mFloatWindowParams.y = 0;

                windowManager.addView(popView, mFloatWindowParams);
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
        this.unregisterReceiver(mBtDeviceReceiver);
        Log.i(TAG,"onDestroy");
    }



}
