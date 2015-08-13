package com.getblee.blee;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import at.markushi.ui.CircleButton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;

/**
 * Created by malhar on 25/4/15.
 */


public class DeviceCard extends Card {

    private String IP;
    private String MAC;
    private String name;
    private Boolean reachable;
    private Boolean editable;
    Boolean markedFlag;

    public DeviceCard(Context context) {
        this(context, R.layout.device_card);
    }

    public DeviceCard(Context context,String IP, String MAC, String name,Boolean reachable) {

        this(context, R.layout.device_card);
        this.IP = IP;
        this.MAC = MAC;
        this.name = name;
        this.reachable = reachable;
        this.markedFlag=false;
        this.editable=false;
        Log.d("myTag", "Friend Card constructor");
    }



    public DeviceCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
        Log.d("myTag", "Init called");
    }


    private void init(){

        //Set a OnClickListener listener
        //if(MainActivity.selectionFlag>0) {
          /*
            setOnLongClickListener(new OnLongCardClickListener() {
                @Override
                public boolean onLongClick(Card card, View view) {
                    Toast.makeText(getContext(),"Long!",Toast.LENGTH_SHORT).show();
                    if(MainActivity.selectionFlag==0) {
                        MainActivity.selectionFlag++;
                        markedFlag=true;
                        card.notifyDataSetChanged();
                    }
                    return false;
                }
            });
         */
        /*
            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getContext(),"In the listener!",Toast.LENGTH_SHORT).show();
                    if(markedFlag==false) {
                        MainActivity.selectionFlag++;
                        markedFlag=true;
                    }
                    else {
                        MainActivity.selectionFlag--;
                        markedFlag=false;
                    }

                    //Call refresh
                    //card.notifyDataSetChanged();
                }
            });
            */
        //}

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, final View view) {
        Log.i("myTag","setupInnerView");

        final TextView nameBox = (TextView)view.findViewById(R.id.name);
        final CircleButton editName  = (CircleButton)view.findViewById(R.id.edit_name_button);
        CircleButton testDevice  = (CircleButton)view.findViewById(R.id.test_device_button);
        final ImageButton mark=(ImageButton) view.findViewById(R.id.mark);
        LinearLayout l = (LinearLayout)view.findViewById(R.id.parentView);
        final EditText namechange = (EditText)view.findViewById(R.id.edit_name);

        if(editable==true) {
            editable = true;
            editName.setImageResource(R.drawable.save_name);
            Log.i("ringring",nameBox.getText().toString());
            namechange.setText(nameBox.getText().toString());

            nameBox.setVisibility(View.GONE);
            namechange.setVisibility(View.VISIBLE);
            namechange.requestFocus();
            namechange.setSelectAllOnFocus(true);

        }
        else {
            editable = false;
            editName.setImageResource(R.drawable.edit_name);

            nameBox.setText(namechange.getText().toString());
            namechange.setVisibility(View.GONE);
            nameBox.setVisibility(View.VISIBLE);

        }
        nameBox.setText(name);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(markedFlag==true) {
                    MainActivity.selectionFlag--;
                    markedFlag=false;
                    mark.setImageResource(R.drawable.empty_mark);
                    MainActivity.groupUserList.remove(MAC);

                    if(MainActivity.selectionFlag==0) {
                        //change button
                        MainActivity.buttonPanel.setVisibility(View.GONE);
                        MainActivity.menuPanel.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    MainActivity.selectionFlag++;
                    markedFlag=true;
                    mark.setImageResource(R.drawable.filled_mark);
                    MainActivity.groupUserList.add(MAC);
                    if(MainActivity.selectionFlag==1) {
                        //change button
                        MainActivity.buttonPanel.setVisibility(View.VISIBLE);
                        MainActivity.menuPanel.setVisibility(View.GONE);
                    }

                }

            }
        });

            ViewToClickToExpand viewToClickToExpand =
                    ViewToClickToExpand.builder()
                            .setupView(l);
            setViewToClickToExpand(viewToClickToExpand);

        //final CircleButton editnameicon = (CircleButton)view.findViewById(R.id.edit_name_button);

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editable==false)
                {
                    Log.i("ringring","Editable was false");
                    editable = true;
                    editName.setImageResource(R.drawable.save_name);
                    Log.i("ringring",nameBox.getText().toString());
                    namechange.setText(nameBox.getText().toString());

                    nameBox.setVisibility(View.GONE);
                    namechange.setVisibility(View.VISIBLE);
                    namechange.requestFocus();


                }
                else {
                    Log.i("ringring","Editable was true");
                    editable = false;
                    editName.setImageResource(R.drawable.edit_name);
                    nameBox.setText(namechange.getText().toString());
                    namechange.setVisibility(View.GONE);
                    nameBox.setVisibility(View.VISIBLE);
                    name=namechange.getText().toString();
                    new PreferencesAccesser(getContext()).changeName(name,MAC);
                }
            }
        });

        testDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new ClientThread(IP,"t")).start();
                Toast.makeText(getContext(), "Test Message sent to the device", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
