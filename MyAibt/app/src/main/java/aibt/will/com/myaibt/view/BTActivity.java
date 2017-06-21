package aibt.will.com.myaibt.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Set;

import aibt.will.com.myaibt.R;

/**
 * @author 王晶
 * @date 17-6-21-上午9:14
 * @desc
 */
public class BTActivity extends Activity{

    private static final String TAG = "BTActivity";

    private int REQUEST_ENABLE_BT  =  2;

    Button btnEnableBt;
    Button btnListPair;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);

        init();
    }

    private void init(){
        btnEnableBt = (Button) findViewById(R.id.btnEnableBt);

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




}
