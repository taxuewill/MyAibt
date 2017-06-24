package aibt.will.com.myaibt.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王晶
 * @date 17-6-21-下午2:06
 * @desc
 */
public class BtDeviceReceiver extends BroadcastReceiver {

    private static final String TAG = "BtDeviceReceiver";

    List<BluetoothDevice> bluetoothDevices = new ArrayList<>();


    public IntentFilter getFilter(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        return filter;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(BluetoothDevice.ACTION_FOUND.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(!bluetoothDevices.contains(device)){
                bluetoothDevices.add(device);
            }
            String deviceName = device.getName();
            String deviceHardwareAds = device.getAddress();
            Log.i(TAG,"find device:"+deviceName+", address is "+deviceHardwareAds);
        }

    }


    public List<BluetoothDevice> getBluetoothDevices(){
        return bluetoothDevices;
    }


}
