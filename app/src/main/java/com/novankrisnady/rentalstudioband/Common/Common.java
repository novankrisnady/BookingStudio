package com.novankrisnady.rentalstudioband.Common;

import com.google.firebase.Timestamp;
import com.novankrisnady.rentalstudioband.Model.BookingInformation;
import com.novankrisnady.rentalstudioband.Model.Room;
import com.novankrisnady.rentalstudioband.Model.Studio;
import com.novankrisnady.rentalstudioband.Model.TimeSlot;
import com.novankrisnady.rentalstudioband.Model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_STUDIO_STORE = "STUDIO_SAVE";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_ROOM_LOAD_DONE = "ROOM_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_ROOM_SELECTED = "ROOM_SELECTED";
    public static final int TIME_SLOT_TOTAL = 24;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static final String EVENT_URI_CACHE = "URI_EVENT_SAVE";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Studio currentStudio;
    public static int step = 0; //Init first step is 0
    public static String city = "";
    public static Room currentRoom;
    public static int currentTimeSlot = -1;
    public static Calendar bookingDate=Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
    public static BookingInformation currentBooking;
    public static String currentBookingId="";

    public static String convertTimeSlotToString(int slot) {
        switch (slot)
        {
            case 0:
                return "01:00-02:00";
            case 1:
                return "02:00-03:00";
            case 2:
                return "03:00-04:00";
            case 3:
                return "04:00-05:00";
            case 4:
                return "05:00-06:00";
            case 5:
                return "06:00-07:00";
            case 6:
                return "07:00-08:00";
            case 7:
                return "08:00-09:00";
            case 8:
                return "09:00-10:00";
            case 9:
                return "10:00-11:00";
            case 10:
                return "11:00-12:00";
            case 11:
                return "12:00-13:00";
            case 12:
                return "13:00-14:00";
            case 13:
                return "14:00-15:00";
            case 14:
                return "15:00-16:00";
            case 15:
                return "16:00-17:00";
            case 16:
                return "17:00-18:00";
            case 17:
                return "18:00-19:00";
            case 18:
                return "19:00-20:00";
            case 19:
                return "20:00-21:00";
            case 20:
                return "21:00-22:00";
            case 21:
                return "22:00-23:00";
            case 22:
                return "23:00-24:00";
            case 23:
                return "24:00-01:00";
            default:
                return "Closed";
        }
    }

    public static String convertTimeStampToStringKey(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        return simpleDateFormat.format(date);
    }
}
