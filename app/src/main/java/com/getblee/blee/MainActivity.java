package com.getblee.blee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class MainActivity extends ActionBarActivity {
    public static int selectionFlag=0;
    TextView textView1;
    WifiApManager wifiApManager;
    public static ArrayList<ClientScanResult> clientList;
    ImageButton refresh_list_button;
    public boolean network_state = false;
    ImageButton groupMenuButton;
    public static LinearLayout buttonPanel;
    public static LinearLayout menuPanel;
    public static ArrayList<String> groupUserList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);

        selectionFlag=0;
        groupUserList=new ArrayList<String>();
        buttonPanel=(LinearLayout) this.findViewById(R.id.create_group_layout);

        menuPanel=(LinearLayout) this.findViewById(R.id.menu_panel_layout);

        wifiApManager = new WifiApManager(this);
        wifiApManager.setWifiApEnabled(null, false);
        network_state = false;
        getSupportActionBar().setIcon(R.drawable.blee_logo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.switch_action_bar);
        final Switch ToggleAP = (Switch) findViewById(R.id.switchForActionBar);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText("Please Switch On Blee-Network for devices to connect");
        textView1.setVisibility(View.VISIBLE);

        groupMenuButton=(ImageButton) findViewById(R.id.group_menu_button);
        groupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupMenuIntent=new Intent(MainActivity.this,GroupActivity.class);
                startActivity(groupMenuIntent);
            }
        });

        ImageButton createGroupButton=(ImageButton) findViewById(R.id.create_group_button);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectionFlag>0)
                {
                    EditText editText=(EditText)findViewById(R.id.group_name);
                    String groupName=editText.getText().toString();
                    if(groupName.length()<=0)
                    {
                        Toast.makeText(MainActivity.this, "Please enter Valid Group Name", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new PreferencesAccesser(MainActivity.this).addGroup(groupName, groupUserList);
                        groupUserList = new ArrayList<String>();
                        editText.setText("");
                        buttonPanel.setVisibility(View.GONE);
                        menuPanel.setVisibility(View.VISIBLE);
                        selectionFlag = 0;
                        populateDeviceList();
                        Intent groupMenuIntent=new Intent(MainActivity.this,GroupActivity.class);
                        startActivity(groupMenuIntent);
                    }
                }
            }

        });

        refresh_list_button = (ImageButton)findViewById(R.id.refresh_list_button);


        ToggleAP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if(isChecked) {
                    wifiApManager.setWifiApEnabled(null, true);
                    Toast.makeText(MainActivity.this, "Switching on Blee Network", Toast.LENGTH_SHORT).show();
                    network_state = true;
                    textView1.setVisibility(View.GONE);
                }
                else{
                    wifiApManager.setWifiApEnabled(null, false);
                    Toast.makeText(MainActivity.this, "Blee-Network disabled", Toast.LENGTH_SHORT).show();
                    network_state = false;
                    textView1.setText("Please Switch On Blee-Network for devices to connect");
                    textView1.setVisibility(View.VISIBLE);
                }
            }
    }) ;


        refresh_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(network_state) {
                    textView1.setVisibility(View.GONE);
                    scan();
                }
                else{
                    textView1.setText("Please Switch On Blee-Network for devices to connect");
                    textView1.setVisibility(View.VISIBLE);
                    scan();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.About:
                Toast.makeText(MainActivity.this, "About Blee Activity Opening to be added", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        if(selectionFlag>0) {
            selectionFlag = 0;
            populateDeviceList();
            buttonPanel.setVisibility(View.GONE);
            menuPanel.setVisibility(View.VISIBLE);
        }
        else
            super.onBackPressed();
    }

    private void scan() {
        wifiApManager.getClientList(true, new FinishScanListener() {

            @Override
            public void onFinishScan(final ArrayList<ClientScanResult> clients) {
                
                clientList=clients;   //copy to variable in mainActivity
                //textView1.setText("WifiApState: " + wifiApManager.getWifiApState());
                populateDeviceList();
            }
        });
    }



    public void populateDeviceList() {
            int i = 0;
            ArrayList<Card> cards = new ArrayList<Card>();
        PreferencesAccesser preferencesAccesser=new PreferencesAccesser(this);

        for (ClientScanResult clientScanResult : clientList) {

                // Create a Card
                String name=preferencesAccesser.getName(clientScanResult.getHWAddr());
                if(name.equals(""))
                {
                    i++;
                    name="Ring Bearer "+i;
                }

                DeviceCard card = new DeviceCard(this, clientScanResult.getIpAddr(), clientScanResult.getHWAddr(), name, clientScanResult.isReachable());

                // Create a CardHeader
                //CardHeader header = new CardHeader(this);
                //header.setTitle("Bday Party");

                // Add Header to card
                //card.addCardHeader(header);


                //CardThumbnail thumb = new CardThumbnail(getActivity());
                //thumb.setDrawableResource(R.drawable.ic_category_food);
                //card.addCardThumbnail(thumb);

                //card.setBackgroundResourceId(R.drawable.card_selector);
                CustomExpandCard expandp = new CustomExpandCard(MainActivity.this.getApplicationContext(),"\nList of Songs on this device:\n 1. Kal Ho Na Ho");
                card.addCardExpand(expandp);

                cards.add(card);

            }
            if (cards.size() == 0) {
                if (network_state) {
                    textView1.setText("No devices Connected");
                    textView1.setVisibility(View.VISIBLE);
                } else {
                    textView1.setText("Blee Network disabled");
                    textView1.setVisibility(View.VISIBLE);
                }
            }
            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
            //mCardArrayAdapter.setInnerViewTypeCount(1);


            CardListView listView = (CardListView) this.findViewById(R.id.device_list);

            AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(mCardArrayAdapter);
            animCardArrayAdapter.setAbsListView(listView);


            if (listView != null) {
//            listView.setAdapter(mCardArrayAdapter);

                listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);

            }

        }


    public String getLocalIpAddress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        Log.d("ring", inetAddress.getHostAddress().toString());
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

}

