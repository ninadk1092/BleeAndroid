package com.getblee.blee;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rahul on 28/4/15.
 */
public class PreferencesAccesser {

    SharedPreferences prefs;

    public PreferencesAccesser(Context context) {
        prefs=context.getSharedPreferences("Blee",Context.MODE_PRIVATE);
    }

    public void addGroup(String name,ArrayList<String> userList) {
        String groupNameList=prefs.getString("groupNameList","");
        groupNameList+=name+",";
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString(name,userList.toString());
        editor.putString("groupNameList",groupNameList);
        editor.commit();
    }

    public void changeName(String name,String mac) {
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString(mac,name);
        editor.commit();
    }

    public String getName(String mac){
        return prefs.getString(mac,"");
    }

    public JSONArray getAllGroups() {
        String groupNameList=prefs.getString("groupNameList","");
        JSONArray groupArray=new JSONArray();
        for( String groupName:groupNameList.split(",")) {
            try {
                String group = prefs.getString(groupName, "");
                JSONObject groupObject = new JSONObject();
                groupObject.put("name", groupName);
                groupObject.put("memberList", group);
                groupArray.put(groupObject);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return groupArray;
    }


}
