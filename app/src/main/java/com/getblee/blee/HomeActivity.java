package com.getblee.blee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.getblee.blee.R;
import com.getblee.blee.dialog.SetBeatPatternDialog;
import com.getblee.blee.listener.BeatPatternListener;
import com.getblee.blee.util.PreferenceConnector;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, BeatPatternListener {
    private static final int REQUEST_CONNECT = 1;
    private Context mContext;
    private CircularProgressBar circularProgressBar;
    private TextView textViewConnect, textViewNoOfBeats;
    private ImageView imgImportBeatPattern, imgImportAudioFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = HomeActivity.this;
        inItUiViews();
    }

    private void inItUiViews() {
        circularProgressBar = (CircularProgressBar)findViewById(R.id.circularProgressBar);
        circularProgressBar.setProgress(30);
        textViewConnect = (TextView)findViewById(R.id.textViewConnect);
        textViewConnect.setOnClickListener(this);

        textViewNoOfBeats = (TextView)findViewById(R.id.textViewNoOfBeats);
        textViewNoOfBeats.setText(PreferenceConnector.readInteger(mContext, PreferenceConnector.LAST_BEAT_COUNT, 0)+" Beats");

        imgImportBeatPattern = (ImageView)findViewById(R.id.imgImportBeatPattern);
        imgImportBeatPattern.setOnClickListener(this);
        imgImportAudioFile = (ImageView)findViewById(R.id.imgImportAudioFile);
        imgImportAudioFile.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewConnect:
                startActivityForResult(new Intent(mContext, MainActivity.class), REQUEST_CONNECT);
            break;
            case R.id.imgImportBeatPattern:
                SetBeatPatternDialog dialog = new SetBeatPatternDialog(mContext, this);
                dialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CONNECT:

                    break;
            }
        }
    }

    @Override
    public void onBitPatternSet(int beats) {
        textViewNoOfBeats.setText(beats+" Beats");
    }
}
