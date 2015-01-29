package com.Serialbluetooth.serialbluetooth;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



public class FragmentKeyboard extends Fragment  implements View.OnClickListener {
	
	private InterfaceKeyboard keyCommunicator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	
	}
 
	EditText stringTosend;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View viewFragment = inflater.inflate(R.layout.fragment_keyboard, container, false);
		
		stringTosend = (EditText) viewFragment.findViewById(R.id.txtStringToSend);
		
		Button key1 = (Button) viewFragment.findViewById(R.id.key1);
		Button key2 = (Button) viewFragment.findViewById(R.id.key2);		
		Button key3 = (Button) viewFragment.findViewById(R.id.key3);		
		Button keyA = (Button) viewFragment.findViewById(R.id.keyA);		

		Button key4 = (Button) viewFragment.findViewById(R.id.key4);		
		Button key5 = (Button) viewFragment.findViewById(R.id.key5);		
		Button key6 = (Button) viewFragment.findViewById(R.id.key6);		
		Button keyB = (Button) viewFragment.findViewById(R.id.keyB);		

		Button key7 = (Button) viewFragment.findViewById(R.id.key7);		
		Button key8 = (Button) viewFragment.findViewById(R.id.key8);		
		Button key9 = (Button) viewFragment.findViewById(R.id.key9);		
		Button keyC = (Button) viewFragment.findViewById(R.id.keyC);		

		Button keyAst= (Button) viewFragment.findViewById(R.id.keyAst);		
		Button key0 = (Button) viewFragment.findViewById(R.id.key0);		
		Button keyNum= (Button) viewFragment.findViewById(R.id.keyNum);		
		Button keyD = (Button) viewFragment.findViewById(R.id.keyD);		
		Button send = (Button) viewFragment.findViewById(R.id.btnSend);
		
		
		key1.setOnClickListener(this);
		key2.setOnClickListener(this);
		key3.setOnClickListener(this);
		keyA.setOnClickListener(this);
		
		key4.setOnClickListener(this);
		key5.setOnClickListener(this);
		key6.setOnClickListener(this);
		keyB.setOnClickListener(this);
		
		key7.setOnClickListener(this);
		key8.setOnClickListener(this);
		key9.setOnClickListener(this);
		keyC.setOnClickListener(this);
		
		keyAst.setOnClickListener(this);
		key0.setOnClickListener(this);
		keyNum.setOnClickListener(this);
		keyD.setOnClickListener(this);
	
		send.setOnClickListener(this);
		return viewFragment ;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        keyCommunicator = (InterfaceKeyboard) activity;
    }
    

    @Override
    public void onDetach() {
        super.onDetach();
        keyCommunicator = null;
    }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
        {
            case R.id.key1:
            	keyCommunicator.pressedKey('1');
            break;
            case R.id.key2:
            	keyCommunicator.pressedKey('2');
            break;
            case R.id.key3:
            	keyCommunicator.pressedKey('3');
            break;
            case R.id.keyA:
            	keyCommunicator.pressedKey('A');
            break;
            
            case R.id.key4:
            	keyCommunicator.pressedKey('4');
            break;
            case R.id.key5:
            	keyCommunicator.pressedKey('5');
            break;
            case R.id.key6:
            	keyCommunicator.pressedKey('6');
            break;
            case R.id.keyB:
            	keyCommunicator.pressedKey('B');
            break;
            
            case R.id.key7:
            	keyCommunicator.pressedKey('7');
            break;
            case R.id.key8:
            	keyCommunicator.pressedKey('8');
            break;
            case R.id.key9:
            	keyCommunicator.pressedKey('9');
            break;
            case R.id.keyC:
            	keyCommunicator.pressedKey('C');
            break;
            
            case R.id.keyAst:
            	keyCommunicator.pressedKey('*');
            break;
            case R.id.key0:
            	keyCommunicator.pressedKey('0');
            break;
            case R.id.keyNum:
            	keyCommunicator.pressedKey('#');
            break;
            case R.id.keyD:
            	keyCommunicator.pressedKey('D');
            break;
            case R.id.btnSend:
            	keyCommunicator.writtenString(stringTosend.getText().toString());
            break;
        }
		
	}
	
	

}
