package aibt.will.com.myaibt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import aibt.will.com.myaibt.view.BTActivity;

public class MainActivity extends Activity {

    private static final String TAG = "BTMainActivity";

    protected BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, BTActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(this, WifiActivity.class);
//        startActivity(intent);


        Log.i(TAG,"start discovery");


    }

}
