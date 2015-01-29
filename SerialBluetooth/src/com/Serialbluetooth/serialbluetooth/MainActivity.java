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


public class MainActivity extends Activity implements InterfaceKeyboard {

	private static final String TAG = "bluetooth2";


	  Handler h;
	    
	  final int RECIEVE_MESSAGE = 1;        // Status  for Handler
	  private BluetoothAdapter btAdapter = null;
	  private BluetoothSocket btSocket = null;
	  private StringBuilder sb = new StringBuilder();
	   
	  private ConnectedThread mConnectedThread;
	    
	  // SPP UUID service
	  private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	  
	  // MAC-address of Bluetooth module (you must edit this line)
	  private static String address = "00:15:FF:F3:9E:40";
	    
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  
	    setContentView(R.layout.activity_main);
	  
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();

			FragmentKeyboard fragmentKeyboard = new FragmentKeyboard();
			fragmentTransaction.add(android.R.id.content, fragmentKeyboard).commit();


		    
			/*************************BLUETOOTH******************************/

	    h = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch (msg.what) {
	            case RECIEVE_MESSAGE:                                                   // if receive massage
	                byte[] readBuf = (byte[]) msg.obj;
	                String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
	                sb.append(strIncom);                                                // append string
	                int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
	                if (endOfLineIndex > 0) {                                            // if end-of-line,
	                    String sbprint = sb.substring(0, endOfLineIndex);               // extract string
	                    sb.delete(0, sb.length());                                      // and clear
	                    
	                    processMessage(sbprint);
	                   
	                }
	                //Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
	                break;
	            }
	        };
	    };
	      
	    btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
	    checkBTState();

	  }
	   
	  private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
	      if(Build.VERSION.SDK_INT >= 10){
	          try {
	              final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
	              return (BluetoothSocket) m.invoke(device, MY_UUID);
	          } catch (Exception e) {
	              Log.e(TAG, "Could not create Insecure RFComm Connection",e);
	          }
	      }
	      return  device.createRfcommSocketToServiceRecord(MY_UUID);
	  }
	    
	  @Override
	  public void onResume() {
	    super.onResume();
	  
	    Log.d(TAG, "...onResume - try connect...");
	    
	    // Set up a pointer to the remote node using it's address.
	    BluetoothDevice device = btAdapter.getRemoteDevice(address);
	    
	    // Two things are needed to make a connection:
	    //   A MAC address, which we got above.
	    //   A Service ID or UUID.  In this case we are using the
	    //     UUID for SPP.
	     
	    try {
	    	btSocket = createBluetoothSocket(device);
	    } catch (IOException e) {
	        errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
	    }
	    
	    // Discovery is resource intensive.  Make sure it isn't going on
	    // when you attempt to connect and pass your message.
	    btAdapter.cancelDiscovery();
	    
	    // Establish the connection.  This will block until it connects.
	    Log.d(TAG, "...Connecting...");
	    try {
	      btSocket.connect();
	      Log.d(TAG, "....Connection ok...");
	    } catch (IOException e) {
	      try {
	        btSocket.close();
	      } catch (IOException e2) {
	        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
	      }
	    }
	      
	    // Create a data stream so we can talk to server.
	    Log.d(TAG, "...Create Socket...");
	    
	    mConnectedThread = new ConnectedThread(btSocket,h);
	    mConnectedThread.start();
	  }
	  
	  @Override
	  public void onPause() {
	    super.onPause();
	  
	    Log.d(TAG, "...In onPause()...");
	   
	    try     {
	      btSocket.close();
	    } catch (IOException e2) {
	      errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
	    }
	  }
	    
	  private void checkBTState() {
	    // Check for Bluetooth support and then check to make sure it is turned on
	    // Emulator doesn't support Bluetooth and will return null
	    if(btAdapter==null) { 
	      errorExit("Fatal Error", "Bluetooth not support");
	    } else {
	      if (btAdapter.isEnabled()) {
	        Log.d(TAG, "...Bluetooth ON...");
	      } else {
	        //Prompt user to turn on Bluetooth
	        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	        startActivityForResult(enableBtIntent, 1);
	      }
	    }
	  }
	  
	  private void errorExit(String title, String message){
	    Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
	    finish();
	  }
	  
//	  private class ConnectedThread extends Thread {
//	        private final InputStream mmInStream;
//	        private final OutputStream mmOutStream;
//	      
//	        public ConnectedThread(BluetoothSocket socket) {
//	            InputStream tmpIn = null;
//	            OutputStream tmpOut = null;
//	      
//	            // Get the input and output streams, using temp objects because
//	            // member streams are final
//	            try {
//	                tmpIn = socket.getInputStream();
//	                tmpOut = socket.getOutputStream();
//	            } catch (IOException e) { }
//	      
//	            mmInStream = tmpIn;
//	            mmOutStream = tmpOut;
//	        }
//	      
//	        public void run() {
//	            byte[] buffer = new byte[256];  // buffer store for the stream
//	            int bytes; // bytes returned from read()
//	 
//	            // Keep listening to the InputStream until an exception occurs
//	            while (true) {
//	                try {
//	                    // Read from the InputStream
//	                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
//	                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
//	                } catch (IOException e) {
//	                    break;
//	                }
//	            }
//	        }
//	      
//	        /* Call this from the main activity to send data to the remote device */
//	        public void write(String message) {
//	            Log.d(TAG, "...Data to send: " + message + "...");
//	            byte[] msgBuffer = message.getBytes();
//	            try {
//	                mmOutStream.write(msgBuffer);
//	            } catch (IOException e) {
//	                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");     
//	              }
//	        }
//	    }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
	    //btnKey1 = (Button) findViewById(R.id.)
		super.onStart();
	}

	
	@Override
	public void pressedKey(char charTosend) {
		// TODO Auto-generated method stub
		mConnectedThread.write(Character.toString(charTosend));
	}

	@Override
	public void writtenString(String stringsTosend) {
		// TODO Auto-generated method stub
		mConnectedThread.write(stringsTosend);
	}
	
	public void processMessage(String MSG){
    	View view = getCurrentFocus();	//get the current view
    	TextView lblTexto = (TextView) findViewById(R.id.txtFromBT);
    	lblTexto.setText( MSG);            // update TextView
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
}



