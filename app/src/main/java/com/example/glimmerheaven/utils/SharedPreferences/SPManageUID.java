package com.example.glimmerheaven.utils.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SPManageUID {
    private final String FILE_NAME = "sp_uid";
    private final String KEY = "currentUid";
    private SharedPreferences sharedPreferences;
    private Context context;

    public SPManageUID(Context context) {
        this.context = context;
    }

    private void createPreferenceObject(){
        if(context != null){
            sharedPreferences = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        }
    }

    public boolean saveUid(String uid){
        createPreferenceObject();
      if(uid != null && !uid.equals("") && sharedPreferences != null){
          return sharedPreferences.edit().putString(KEY,uid).commit();
      }else{
          return false;
      }
    }

    public String getUid(){
        createPreferenceObject();
        if(sharedPreferences != null){
            return sharedPreferences.getString(KEY,null);
        }else{
            return null;
        }
    }

    public boolean removeUid(){
        createPreferenceObject();
        if(sharedPreferences != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY);
            boolean result = editor.commit();
            sharedPreferences = null;
            return result;
        }else{
            return false;
        }
    }
}
