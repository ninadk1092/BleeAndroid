package com.getblee.blee;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class GroupActivity extends ActionBarActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.drawable.blee_logo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);



        populateGroupCards();
    }


    void populateGroupCards()
    {

        int i = 0;
        JSONArray groupArray=new PreferencesAccesser(GroupActivity.this).getAllGroups();
        ArrayList<Card> cards = new ArrayList<Card>();
        for (i=0;i<groupArray.length();i++) {
            JSONObject group = null;
            try {
                group = groupArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String groupName= null;
            String groupMembers = null;
            try {
                groupName = group.getString("name");
                groupMembers = group.getString("memberList");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            GroupCard card = new GroupCard(this, groupName, groupMembers);

            cards.add(card);

        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
        //mCardArrayAdapter.setInnerViewTypeCount(1);


        CardListView listView = (CardListView) this.findViewById(R.id.group_list);

        AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(mCardArrayAdapter);
        animCardArrayAdapter.setAbsListView(listView);


        if (listView != null) {
//            listView.setAdapter(mCardArrayAdapter);

            listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);

        }




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }


    public static void playMedia() {
        //
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static String RARP(String MAC){

        playMedia();
        for (ClientScanResult clientScanResult : MainActivity.clientList) {
            Log.d("RingRing","In the Rarp " + clientScanResult.getHWAddr() +"...."+ MAC+"....");
            if(clientScanResult.getHWAddr().equals(MAC)) {
                Log.d("RingRing","Matched"+clientScanResult.getIpAddr());
                return clientScanResult.getIpAddr();
            }
        }
        return "";
    }
}
