package com.getblee.blee;

/**
 * Created by malhar on 22/4/15.
 */

import java.util.ArrayList;

public interface FinishScanListener {


    /*
      Interface called when the scan method finishes. Network operations should not execute on UI thread
      @param  ArrayList of {@link ClientScanResult}
     */

    public void onFinishScan(ArrayList<ClientScanResult> clients);

}