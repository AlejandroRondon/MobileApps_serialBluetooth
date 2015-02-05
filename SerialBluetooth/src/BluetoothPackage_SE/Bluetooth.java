package BluetoothPackage_SE;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Bluetooth {
	final int RECIEVE_MESSAGE = 1;        // Status  for Handler
	//To use in the LOG
	
	public String TAG;
	
	//To communicate the class bluetooth with the main activity
	InterfaceCommunication communicator;

	//Bluetooth components
	Handler h;
	public String addressDevice;
	public BluetoothAdapter btAdapter;
	public BluetoothSocket btSocket;
	
	//object used to construct the message that come from the Device
	public StringBuilder sb;

	public ConnectedThread mConnectedThread;

	// SPP UUID service
	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	//Used to receive parameters from the main Activity
	public Activity activity;
	public Context context;


	@SuppressLint("HandlerLeak") public Bluetooth(String TAG,String address,Activity activity,Context context) {
		// TODO Auto-generated constructor stub

		this.activity = activity;
		this.context = context;

		btAdapter = null;
		btSocket = null;
		sb = new StringBuilder();

		this.TAG = TAG;
		this.addressDevice = address;


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

						communicator.receivedString(sbprint);
					}
					Log.d("Bluetooth receive", "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
					break;
				}
			};
		};

		btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
		checkBTState();
		onAttach();
		
		//bluetoothOnResume();

	}

	public BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
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

	public void bluetoothOnResume(){
		onAttach();
		Log.d(TAG, "...onResume - try connect...");

		// Set up a pointer to the remote node using it's address.
		BluetoothDevice device = btAdapter.getRemoteDevice(addressDevice);

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

	public void bluetoothOnPause(){
		onDetach();
		Log.d(TAG, "...In onPause()...");

		try     {
			btSocket.close();
		} catch (IOException e2) {
			errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
		}
	}

	public void checkBTState() {
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
				activity.startActivityForResult(enableBtIntent, 1);
			}
		}
	}

	public void errorExit(String title, String message){
		Toast.makeText(context, title + " - " + message, Toast.LENGTH_LONG).show();
		activity.finish();
	}


	public void sendChar(char charTosend) {
		// TODO Auto-generated method stub
		mConnectedThread.write(Character.toString(charTosend));
	}

	public void SendString(String stringsTosend) {
		// TODO Auto-generated method stub
		mConnectedThread.write(stringsTosend);
	}


	public void onAttach() {
		communicator = (InterfaceCommunication) activity;
	}


	public void onDetach() {
		communicator = null;
	}


}
