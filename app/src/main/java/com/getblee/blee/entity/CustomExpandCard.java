package com.getblee.blee.entity;


import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getblee.blee.R;

import it.gmariotti.cardslib.library.internal.CardExpand;

public class CustomExpandCard extends CardExpand {
    private String tipstext;
    //Use your resource ID for your inner layout
    public CustomExpandCard(Context context,String s) {
        super(context, R.layout.expand_layout);
        tipstext = s;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view == null) return;
        //Retrieve TextView elements
        TextView tx1 = (TextView) view.findViewById(R.id.foursquaretext);
        tx1.setMovementMethod(LinkMovementMethod.getInstance());
        tx1.setText("");
        tx1.setText(tipstext);
    }
}
