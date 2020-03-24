package com.example.flashchat;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChat extends AppCompatActivity {


    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;

    private ChatAdapter mAdapter;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        setUpDisplayName();
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();

        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String chat=mInputText.getText().toString();
                if(!chat.equals("")){
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat=mInputText.getText().toString();
                if(!chat.equals(""))  sendMessage();
            }
        });



    }

    private void sendMessage() {

        // TODO: Grab the text the user typed in and push the message to Firebase
        Toast.makeText(this, "sent", Toast.LENGTH_SHORT).show();
        String input=mInputText.getText().toString();
        InstantMessage chat=new InstantMessage(input,mDisplayName);

        mDatabaseReference.child("messages").push().setValue(chat);

        mInputText.setText("");

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.

    @Override
    public  void onStart(){
        super.onStart();
        mAdapter=new ChatAdapter(this,mDatabaseReference,mDisplayName);
        mChatListView.setAdapter(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        mAdapter.cleanUp();

        // TODO: Remove the Firebase event listener on the adapter.

    }

    private void setUpDisplayName(){

        SharedPreferences preferences=getSharedPreferences(Register.CHAT_PREFS,MODE_PRIVATE);
        mDisplayName=preferences.getString(Register.DISPLAY_NAME_KEY,null);
        if(mDisplayName==null) mDisplayName="Anonymous";
    }
}
