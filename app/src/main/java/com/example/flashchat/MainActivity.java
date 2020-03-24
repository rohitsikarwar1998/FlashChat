package com.example.flashchat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private FirebaseAuth mFirebaseAuth;
    public static int primarydark;
    public static int coloraccent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login_activity);



        mEmail=findViewById(R.id.login_email);
        mPassword=findViewById(R.id.login_password);
        coloraccent= ContextCompat.getColor(this, R.color.colorAccent);
        primarydark= ContextCompat.getColor(this, R.color.colorPrimaryDark);

        mPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==R.integer.login||keyCode== EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mFirebaseAuth=FirebaseAuth.getInstance();

    }

    public void signInExistingUser(View v){

        attemptLogin();
    }

    public  void registerNewUser(View v){
        Intent intent=new Intent(this,Register.class);
        finish();
        startActivity(intent);
    }

    private void attemptLogin(){

        String email=mEmail.getEditText().getText().toString().trim();
        String password=mPassword.getEditText().getText().toString().trim();

        if(email.equals("")||password.equals("")) return;
        Toast.makeText(this, "Login in progress", Toast.LENGTH_SHORT).show();

        mFirebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            showErrorDialog("There was a problem signing in");
                        }else{
                            Intent intent=new Intent(MainActivity.this,MainChat.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                });
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
