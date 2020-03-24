package com.example.flashchat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class Register extends AppCompatActivity {

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mUsername;
    private TextInputLayout mConfirmPassword;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail=findViewById(R.id.register_email);
        mUsername=findViewById(R.id.register_username);
        mPassword=findViewById(R.id.register_password);
        mConfirmPassword=findViewById(R.id.register_confirm_password);

        mConfirmPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==R.integer.register_form_finished||keyCode== EditorInfo.IME_NULL)
                {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        mFirebaseAuth=FirebaseAuth.getInstance();

    }

    public void signUp(View v){
        attemptRegister();
    }

    private void attemptRegister(){

        mEmail.setError(null);
        mPassword.setError(null);

        String email=mEmail.getEditText().getText().toString().trim();
        String password=mPassword.getEditText().getText().toString().trim();

        boolean cancel=false;
        View focusView=null;

        if(TextUtils.isEmpty(password)||!isPasswordValid(password)){
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createNewUser();
        }

    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)

        String confirmpassword=mConfirmPassword.getEditText().getText().toString().trim();
        return confirmpassword.equals(password)&&confirmpassword.length()>=6;
    }

    private void createNewUser(){

        String email=mEmail.getEditText().getText().toString().trim();
        String password=mPassword.getEditText().getText().toString().trim();

        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("info","create new user "+task.isSuccessful());
                        if(!task.isSuccessful()){
                            showErrorDialog("Registration attempt failed");
                        }
                        else{
                            saveUserName();
                            Intent intent=new Intent(Register.this,MainChat.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                });
    }

    private void saveUserName(){
        String username=mUsername.getEditText().getText().toString().trim();
        SharedPreferences pref=getSharedPreferences(CHAT_PREFS,0);
        pref.edit().putString(DISPLAY_NAME_KEY,username).apply();
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
