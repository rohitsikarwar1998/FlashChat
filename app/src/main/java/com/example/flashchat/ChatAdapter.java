package com.example.flashchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private Activity mActivity;
    private String mDisplayName;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mDataSnapshots;

    private ChildEventListener mListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mDataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatAdapter(Activity activity,DatabaseReference databaseReference,String displayName){
        mActivity=activity;
        mDatabaseReference=databaseReference.child("messages");
        mDisplayName=displayName;
        mDatabaseReference.addChildEventListener(mListener);
        mDataSnapshots=new ArrayList<>();
    }

    static class ViewHolder
    {
        TextView author;
        TextView message;
        LinearLayout.LayoutParams mParams;
    }

    @Override
    public int getCount() {
        return mDataSnapshots.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot dataSnapshot=mDataSnapshots.get(position);
        return dataSnapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater inflater= (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.chat_msg_row,parent,false);
            final ViewHolder holder=new ViewHolder();
            holder.author=convertView.findViewById(R.id.author);
            holder.message=convertView.findViewById(R.id.message);
            holder.mParams= (LinearLayout.LayoutParams) holder.author.getLayoutParams();
            convertView.setTag(holder);
        }

        final InstantMessage message=getItem(position);
        final ViewHolder holder= (ViewHolder) convertView.getTag();

        boolean isme=message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isme,holder);

        holder.author.setText(message.getAuthor());
        holder.message.setText(message.getMessage());

        return convertView;
    }

    private void setChatRowAppearance(boolean isItMe,ViewHolder holder)
    {
        if(isItMe){
            holder.mParams.gravity= Gravity.END;
            holder.author.setTextColor(MainActivity.coloraccent);
            holder.message.setBackgroundResource(R.drawable.bubble2);
        }else{
            holder.mParams.gravity=Gravity.START;
            holder.message.setBackgroundResource(R.drawable.bubble1);
            holder.author.setTextColor(MainActivity.primarydark);
        }

        holder.author.setLayoutParams(holder.mParams);
        holder.message.setLayoutParams(holder.mParams);
    }

    public void cleanUp(){
        mDatabaseReference.removeEventListener(mListener);
    }
}
