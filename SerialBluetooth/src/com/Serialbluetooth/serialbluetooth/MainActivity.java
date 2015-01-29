package com.Serialbluetooth.serialbluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements InterfaceKeyboard,InterfaceCommunication {

//	private static final String TAG = "bluetooth2";


//	  Handler h;
//	    
//	  final int RECIEVE_MESSAGE = 1;        // Status  for Handler
//	  private BluetoothAdapter btAdapter = null;
//	  private BluetoothSocket btSocket = null;
//	  private StringBuilder sb = new StringBuilder();
//	   
//	  private ConnectedThread mConnectedThread;
//	    
//	  // SPP UUID service
//	  private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//	  
//	  // MAC-address of Bluetooth module (you must edit this line)
//	  private static String address = "00:15:FF:F3:9E:40";
//	    
	  /** Called when the activity is first created. */
	 
	public Bluetooth bluetooth;
	public ConnectedThread mConnectedThread;
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  
	    setContentView(R.layout.activity_main);
	  
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();

			FragmentKeyboard fragmentKeyboard = new FragmentKeyboard();
			fragmentTransaction.add(android.R.id.content, fragmentKeyboard).commit();


		    
			/*************************BLUETOOTH******************************/

			bluetooth = new Bluetooth(this, getBaseContext());
	  }
	   

	    
	  @Override
	  public void onResume() {
	    super.onResume();
	
	   bluetooth.bluetoothOnResume();
	  }
	  
	  @Override
	  public void onPause() {
	    super.onPause();
	    bluetooth.bluetoothOnPause();

	  }
	    
	  

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	
    
    
    
    
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



	@Override
	public void pressedKey(char charTosend) {
		// TODO Auto-generated method stub
		bluetooth.sendChar(charTosend);
	}



	@Override
	public void writtenString(String stringsTosend) {
		// TODO Auto-generated method stub
		bluetooth.SendString(stringsTosend);
	}



	@Override
	public void receivedString(String received) {
		// TODO Auto-generated method stub
    	View view = getCurrentFocus();	//get the current view
    	TextView lblTexto = (TextView) findViewById(R.id.txtFromBT);
    	
    	lblTexto.setText( received);            // update TextView
		
    	Log.i("jueptuta","maricaaaaaaaaaaaaaaaaaaaa");
	}
}



