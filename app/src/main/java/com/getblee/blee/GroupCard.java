package com.getblee.blee;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import at.markushi.ui.CircleButton;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by rahul on 28/4/15.
 */
public class GroupCard extends Card {

    String name;
    String members;

    MediaPlayer mediaPlayer = null;
    public GroupCard(Context context) {
        this(context, R.layout.group_card);
    }

    public GroupCard(Context context,String name,String members){

        this(context, R.layout.group_card);
        this.name=name;
        this.members=members;
        mediaPlayer = MediaPlayer.create(context , R.raw.mtvplitsvilla);
    }



    public GroupCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
        Log.d("myTag", "Init called");
    }


    private void init(){


        //}
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, final View view) {
        Log.i("myTag","setupInnerView");

        TextView namebox=(TextView) view.findViewById(R.id.group_name);
        TextView membersbox=(TextView) view.findViewById(R.id.group_members);

        namebox.setText(name);
        membersbox.setText(members);

        CircleButton testDevice= (CircleButton) view.findViewById(R.id.test_group_button);
        CircleButton playSong= (CircleButton) view.findViewById(R.id.play_group_button);

        testDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memberList=members.substring(1,members.length()-1);
                for (String member:memberList.split(", ")) {
                    String IP=GroupActivity.RARP(member);
                    Log.d("RingRing trying this device", IP + " " + member);
                    if(!IP.equals("")) {
                        Log.d("RingRing", IP + " " + member);
                        new Thread(new ClientThread(IP, "t")).start();
                    }
                }
                Toast.makeText(getContext(), "Play message sent to group", Toast.LENGTH_SHORT).show();
            }
        });


        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memberList=members.substring(1,members.length()-1);
                mediaPlayer.start();
                for (String member:memberList.split(", ")) {
                    String IP=GroupActivity.RARP(member);
                    Log.d("RingRing trying device", IP + " " + member);
                    if(!IP.equals("")) {
                        Log.d("RingRing", IP + " " + member);
                        new Thread(new ClientThread(IP, "p")).start();
                    }
                }

                //playMedia();

                Toast.makeText(getContext(), "Play message sent to group", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
