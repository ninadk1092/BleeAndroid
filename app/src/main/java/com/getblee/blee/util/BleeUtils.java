package com.getblee.blee.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Padmanabh on 16-03-2015.
 */
public class BleeUtils {
    public interface URL {
        String BASE = "http://192.168.0.4:8081/mandoc/services/";
        String LOGIN = BASE + "secure/login";
        String SIGNUP = BASE + "unsecure/signup";
        String FORGOT = BASE + "secure/forgot";
        String DOCUMENT_LIST = BASE + "secure/document/list";
        String DOCUMENT_ADD = BASE + "secure/document/add";
        String DOCUMENT_SYNC = BASE + "secure/synch";
        String DOCUMENT_DOWNLOAD = BASE + "secure/document/download";
        String DOCUMENT_UPLOAD = BASE + "secure/upload";
        String FILE_UPLOAD = BASE + "secure/uploadIt";
    }

    public static String CURRENT_USERNAME = "";
    public static List<String> PREVIOUS_CONTACTS = new ArrayList<String>();
    public static final long CODE_SUCCESS = 200L;
    public static final Long RE_LOGIN = 190l;
    public static final Long USER_ALREADY_EXISTS = 191l;
    public static final Long USER_ALREADY_REGISTERED = 192l;
    public static final Long                                                                                                                                                                                                                                LOGIN_FAILED = 192l;
    public static final Long OK = 200l;

    public static final String MAIN_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mandoc/";
    public static final String DATABASE_FOLDER = MAIN_FOLDER + "Database/";
    public static final String APPLICATION_UPDATES = MAIN_FOLDER + "ApplicationUpdates/";
    public static final String DOWNLOADED_DOCUMENTS = MAIN_FOLDER + "Downloaded_Documents/";
    public static final String CREATED_DOCUMENTS = MAIN_FOLDER + "Created_Documents/";

    public static final String[] arrFolders = {DATABASE_FOLDER, APPLICATION_UPDATES, DOWNLOADED_DOCUMENTS, CREATED_DOCUMENTS};

    public static void createAllFoldersIfNotExists(Context mContext) {
        for (int i = 0; i < arrFolders.length; i++) {
            File file = new File(arrFolders[i]);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    public static void hideNotificationBar(Activity aContext) {
        WindowManager.LayoutParams attrs = aContext.getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        aContext.getWindow().setAttributes(attrs);
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
//        Toast.makeText(context, context.getString(R.string.warning_enable_dc_or_wifi), Toast.LENGTH_SHORT).show();
        return false;
    }

    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    public static void previewFile(Context context, File file){
        try{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            if(file.getName().endsWith("pdf")){
                intent.setDataAndType(uri, "application/pdf");
            }else if(file.getName().endsWith("csv")){
                intent.setDataAndType(uri, "text/csv");
            }else if(file.getName().endsWith("docx") || file.getName().endsWith("doc")){
                intent.setDataAndType(uri, "application/msword");
            }else{
                intent.setDataAndType(uri, "application/*");
            }
            context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Viewer Application Found", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public static boolean isDeviceSupportsSIM(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean deviceSupportsSIM = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        return deviceSupportsSIM;
    }

    public static String getMobileNumber(Context context) {
        TelephonyManager t = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String number = t.getLine1Number();
        return number;
    }

}
