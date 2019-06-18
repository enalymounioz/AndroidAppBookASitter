package com.example.bookabarber.Common;

import com.example.bookabarber.Model.Store;
import com.example.bookabarber.Model.User;
import com.example.bookabarber.Model.Sitter;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_LOCAL_STORE="STORE_SAVE";
    public static final String KEY_SITTER_LOAD_DONE = "SITTER_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP ="STEP" ;
    public static final String KEY_SITTER_SELECTED ="SITTER_SELECTED" ;
    public static String IS_LOGIN ="IsLogin";
    public static User currentUser;
    public static Store currentStore;
    public static int step= 0;//Init first step is 0
    public static String city="";
    public static Sitter currentSitter;
}
