package com.getblee.blee.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getblee.blee.R;
import com.getblee.blee.listener.BeatPatternListener;
import com.getblee.blee.util.PreferenceConnector;

/**
 * Created by ceroroot on 29/8/15.
 */
public class SetBeatPatternDialog extends Dialog implements View.OnClickListener {
    private static final int MIN_BIT_COUNT = 1;
    private static final int MAX_BIT_COUNT = 16;
    private Context mContext;
    private BeatPatternListener bpListener;
    private TextView textViewBeatCount;
    private int lastBeatCount;
    private ImageView imgCountPlus, imgCountMinus, imgCloseDialog;

    public SetBeatPatternDialog(Context context, BeatPatternListener listener) {
        super(context);
        this.mContext = context;
        this.bpListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_set_beat_pattern);
        inItUiViews();
    }

    private void inItUiViews() {
        lastBeatCount = PreferenceConnector.readInteger(mContext, PreferenceConnector.LAST_BEAT_COUNT, 0);
        textViewBeatCount = (TextView)findViewById(R.id.textViewBeatCount);
        textViewBeatCount.setText(""+lastBeatCount);

        imgCountPlus = (ImageView)findViewById(R.id.imgCountPlus);
        imgCountPlus.setOnClickListener(this);
        imgCountMinus = (ImageView)findViewById(R.id.imgCountMinus);
        imgCountMinus.setOnClickListener(this);
        imgCloseDialog = (ImageView)findViewById(R.id.imgCloseDialog);
        imgCloseDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgCountPlus:
                if(lastBeatCount < MAX_BIT_COUNT) {
                    lastBeatCount++;
                    textViewBeatCount.setText(""+lastBeatCount);
                }else {
                    Toast.makeText(mContext, "Max beat count is "+MAX_BIT_COUNT, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgCountMinus:
                if(lastBeatCount > MIN_BIT_COUNT) {
                    lastBeatCount--;
                    textViewBeatCount.setText(""+lastBeatCount);
                }else {
                    Toast.makeText(mContext, "Min beat count is "+MIN_BIT_COUNT, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgCloseDialog:
                PreferenceConnector.writeInteger(mContext, PreferenceConnector.LAST_BEAT_COUNT, lastBeatCount);
                bpListener.onBitPatternSet(lastBeatCount);
                dismiss();
                break;
        }
    }
}
