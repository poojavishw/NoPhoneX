package com.nophonex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Base  extends AppCompatActivity {
    private SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
    }
    public SharedPreferences getPref(){
        return mPrefs;
    }
    public void clearPref(){
        mPrefs.edit().clear().apply();
    }
    public void todoTasksList(List<String> indicationList) {
        Gson gson = new Gson();
        String json = gson.toJson(indicationList);
        getPref().edit().putString("list", json).apply();
    }

    public List<String> getTodoTasksList() {
        Gson gson = new Gson();
        List<String> indicationList = null;
        String list = getPref().getString("list", "");
        String[] json = gson.fromJson(list, String[].class);
        if (json != null) {
            indicationList = new ArrayList<>(Arrays.asList(json));
        }
        return indicationList;
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void saveTheme(int theme){
        getPref().edit().putInt(Constants.THEME, theme).apply();
    }
    public int getSavedTheme(){
        return getPref().getInt(Constants.THEME,0);
    }
}
