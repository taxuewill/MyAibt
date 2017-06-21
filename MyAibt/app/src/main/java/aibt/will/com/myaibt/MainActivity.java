package aibt.will.com.myaibt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import aibt.will.com.myaibt.view.BTActivity;

public class MainActivity extends Activity {

    private static final String TAG = "BTActivity";

    protected BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, BTActivity.class);
        startActivity(intent);
//        BroadcastReceiver deviceFound = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context content, Intent intent) {
//                String action = intent.getAction();
//                if(BluetoothDevice.ACTION_FOUND.equals(action)){
//                    //BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothAdapter.EXTRA_DEVICE);
//                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    Log.i(TAG,"find bluetooth device " +device.getName()+",device address is "+ device.getAddress());
////                    boolean isA2dpDevice =
//                }
//
//            }
//        };
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
//        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//
//        this.registerReceiver(deviceFound,intentFilter);
//
//                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                if(!mBluetoothAdapter.isEnabled()){
//                    Log.i(TAG,"bt is not avalible");
//                }
//                mBluetoothAdapter.startDiscovery();

        Log.i(TAG,"start discovery");


    }

}
