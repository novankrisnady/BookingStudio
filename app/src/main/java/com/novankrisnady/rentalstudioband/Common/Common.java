package com.novankrisnady.rentalstudioband.Common;

import com.novankrisnady.rentalstudioband.Model.Studio;
import com.novankrisnady.rentalstudioband.Model.User;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_STUDIO_STORE = "STUDIO_SAVE";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_ROOM_LOAD_DONE = "ROOM_LOAD_DONE";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Studio currentStudio;
    public static int step = 0; //Init first step is 0
    public static String city = "";
}
