package com.example.taximap;

import com.example.taximap.R;
import com.example.taximap.db.QueryDatabaseLogin;

import android.accounts.AccountManager;
import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener{
   private EditText userNameEditableField;
   private EditText passwordEditableField;
   private AccountManager mAccountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        mAccountManager = AccountManager.get(this);
        
        //Check if account exists
        if (!(savedAccount())){
	    	setContentView(R.layout.login);
	    	//initialize instance variables
	        userNameEditableField=(EditText)findViewById(R.id.username_text);
	        passwordEditableField=(EditText)findViewById(R.id.password_text);
	
	        View btnLogin=(Button)findViewById(R.id.login_button);
	        btnLogin.setOnClickListener(this);		//Defined in the onclick function below
	
	        View btnNewUser=(Button)findViewById(R.id.new_user_button);
	        btnNewUser.setOnClickListener(this);
        }
     }
    
	public static Boolean exitStatus=false;
    public void onResume(){
    	if(exitStatus==true){
    		finish();
    	}
    	super.onResume();
    	//Log.i(TAG, "onResume()");
    }

    // The commented functions are for due 3
    /*public void onStart(){
    	super.onStart();
    	Log.i(TAG, "onStart()");
    }



    public void onPause(){
    	super.onPause();
    	Log.i(TAG, "onPause()");
    }

    public void onStop(){
    	super.onStop();
    	Log.i(TAG, "onStop()");
    }

    public void onDestroy(){
    	super.onDestroy();
    	Log.i(TAG, "onDestroy()");
    }

    public void onRestart(){
    	super.onRestart();
    	Log.i(TAG, "onRestart()");
    }*/
    
    //Check if account exists on device
    private boolean savedAccount() {
    	boolean result = false;
    	String username, password, logout;
    	//Grab all accounts for this application on this device
    	Account[] accounts = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
    	//If any exist
    	if (accounts.length > 0) {
    		//There maybe more than one account, so the last one created is used
    		Account userAccount = accounts[accounts.length - 1];
    		//Grab username form the account's user info
    		username = mAccountManager.getUserData(userAccount, Constants.USER_DATA_KEY);
    		//Get password
        	password = mAccountManager.getPassword(userAccount);
        	//get log out variable
    		logout = mAccountManager.getUserData(userAccount, Constants.LOGOUT);
    		//if log out variable is null, then the user has not previously logged out
        	if (logout == null) {
        		checkLogin(username, password);
        		//Once user logs in, they will automatically be logged in until they log out
				mAccountManager.setUserData(userAccount, Constants.LOGOUT, null);
        		result = true;
        	}
    	}
		return result;
    }
    
    private void checkLogin(String username, String password){
        new QueryDatabaseLogin(this).execute(username, password);
      }

    //Login activity's click event
    public void onClick(View v) {
		switch (v.getId()) {
  		case R.id.login_button:
  			//Get username from screen
  			String username = this.userNameEditableField.getText().toString();
  			//get password from screen and hash it
  			String password = Hash.hashString(this.passwordEditableField.getText().toString());
  			//login
		    checkLogin(username, password);
		    break;
    	case R.id.new_user_button:
    	    startActivity(new Intent(this, AccountActivity.class));		// start a new activity Direct invocation 
    	    break;
		}
    }
}
