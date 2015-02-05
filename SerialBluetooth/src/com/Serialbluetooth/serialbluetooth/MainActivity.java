package com.Serialbluetooth.serialbluetooth;

import BluetoothPackage_SE.Bluetooth;
import BluetoothPackage_SE.InterfaceCommunication;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity implements InterfaceKeyboard,InterfaceCommunication {
	    
	  /** Called when the activity is first created. */
	 
	public Bluetooth bluetooth;

	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  
	    setContentView(R.layout.activity_main);
	  
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();

			FragmentKeyboard fragmentKeyboard = new FragmentKeyboard();
			fragmentTransaction.add(android.R.id.content, fragmentKeyboard).commit();


		    
			/*************************BLUETOOTH******************************/
			// MAC-address of Bluetooth module (you must edit this line)
		    String nameTag = "Bluetooth: Moviles";
			String address = "00:15:FF:F3:9E:40";
			
			bluetooth = new Bluetooth(nameTag,address,this, getBaseContext());
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
		/**ACA SE IMPLEMENTA LO QUE SE QUIERA HACER UNA VEZ SE RECIBA UNA TRAMA**/
		// TODO Auto-generated method stub
    	@SuppressWarnings("unused")
		View view = getCurrentFocus();	//get the current view
    	TextView lblTexto = (TextView) findViewById(R.id.txtFromBT);
    	lblTexto.setText(received);            // update TextView
		
	}
}



