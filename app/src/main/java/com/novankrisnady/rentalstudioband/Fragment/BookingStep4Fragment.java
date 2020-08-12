package com.novankrisnady.rentalstudioband.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.novankrisnady.rentalstudioband.Common.Common;
import com.novankrisnady.rentalstudioband.Model.BookingInformation;
import com.novankrisnady.rentalstudioband.Model.MyNotification;
import com.novankrisnady.rentalstudioband.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    AlertDialog dialog;

    @BindView(R.id.txt_booking_room_text)
    TextView txt_booking_room_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_studio_address)
    TextView txt_studio_address;
    @BindView(R.id.txt_studio_name)
    TextView txt_studio_name;
    @BindView(R.id.txt_studio_open_hours)
    TextView txt_studio_open_hours;
    @BindView(R.id.txt_studio_phone)
    TextView txt_studio_phone;
    @BindView(R.id.txt_studio_website)
    TextView txt_studio_website;

    @OnClick(R.id.btn_confirm)
    void confirmBooking(){

        dialog.show();

        //Process Timestamp
        //Use Timestamp, to filter all booking with date is greater today
        //For only display all future booking
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-"); //Split ex : 9:00 - 10:00
        //Get started time : get 9:00
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); //Get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim()); //Get 00

        Calendar bookingDateWithourHouse = Calendar.getInstance();
        bookingDateWithourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
        bookingDateWithourHouse.set(Calendar.HOUR_OF_DAY,startHourInt);
        bookingDateWithourHouse.set(Calendar.MINUTE,startMinInt);

        //Create timestamp object and apply to BookingForInformation
        Timestamp timestamp = new Timestamp(bookingDateWithourHouse.getTime());


        //Create booking information
        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setCityBook(Common.city);
        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false); //Always FALSE because will use this field to filter for display user
        bookingInformation.setRoomId(Common.currentRoom.getRoomId());
        bookingInformation.setRoomName(Common.currentRoom.getName());
        bookingInformation.setCustomerName(Common.currentUser.getName());
        bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
        bookingInformation.setStudioId(Common.currentStudio.getStudioId());
        bookingInformation.setStudioAddress(Common.currentStudio.getAddress());
        bookingInformation.setStudioName(Common.currentStudio.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(bookingDateWithourHouse.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        //Submit to room document
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllRental")
                .document(Common.city)
                .collection("StudioBand")
                .document(Common.currentStudio.getStudioId())
                .collection("Room")
                .document(Common.currentRoom.getRoomId())
                .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot)); //bookDate is simple format with dd_MM_yyyy = 08_08_2020

        //Write data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Write an function to check here

                        addToUserBooking(bookingInformation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToUserBooking(final BookingInformation bookingInformation) {


        //Create new collection
        final CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.currentUser.getPhoneNumber())
                .collection("Booking");

        //Check if exist document in this collection
        //Get current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);

        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

        //Check if exist document in this collection
        userBooking
                .whereGreaterThanOrEqualTo("timestamp",toDayTimeStamp)
                .whereEqualTo("done",false)
                .limit(1) //Only take 1
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty())
                        {
                            //Set Data
                            userBooking.document()
                                    .set(bookingInformation)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            //Create Notification
                                            MyNotification myNotification = new MyNotification();
                                            myNotification.setUid(UUID.randomUUID().toString());
                                            myNotification.setTitle("New Booking");
                                            myNotification.setContent("You have a new appoiment for customer booking studio!");
                                            myNotification.setRead(false);

                                            //Submit notification no 'Notification' collectin of Room
                                            FirebaseFirestore.getInstance()
                                                    .collection("AllRental")
                                                    .document(Common.city)
                                                    .collection("StudioBand")
                                                    .document(Common.currentStudio.getStudioId())
                                                    .collection("Room")
                                                    .document(Common.currentRoom.getRoomId())
                                                    .collection("Notification")
                                                    .document(myNotification.getUid())
                                                    .set(myNotification)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                                dialog.dismiss();

                                                            addToCalendar(Common.bookingDate,
                                                                    Common.convertTimeSlotToString(Common.currentTimeSlot));
                                                            resetStaticData();
                                                            getActivity().finish(); //Close activity
                                                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else
                        {
                            if (dialog.isShowing())
                                dialog.dismiss();

                            resetStaticData();
                            getActivity().finish(); //Close activity
                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addToCalendar(Calendar bookingDate, String startDate) {
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-"); //Split ex : 9:00 - 10:00
        //Get started time : get 9:00
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); //Get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim()); //Get 00

        String[] endTimeConvert = convertTime[1].split(":");
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim()); //Get 10
        int endMinInt = Integer.parseInt(endTimeConvert[1].trim()); //Get 00

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY,startHourInt); //Set event start hour
        startEvent.set(Calendar.MINUTE,startMinInt); //Set event start min

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY,endHourInt); //Set event start hour
        endEvent.set(Calendar.MINUTE,endMinInt); //Set event start min

        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        String startEventTime = calendarDateFormat.format(startEvent.getTime());
        String endEventTime = calendarDateFormat.format(endEvent.getTime());

        addToDeviceCalendar(startEventTime,endEventTime,"Studio Booking",
                new StringBuilder("Studio from")
        .append(startTime)
        .append(" with ")
        .append(Common.currentRoom.getName())
        .append(" at ")
        .append(Common.currentStudio.getName()).toString(),
                new StringBuilder("Address: ").append(Common.currentStudio.getAddress()).toString());
    }

    private void addToDeviceCalendar(String startEventTime, String endEventTime, String title, String description, String location) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            Date start = calendarDateFormat.parse(startEventTime);
            Date end = calendarDateFormat.parse(endEventTime);

            ContentValues event = new ContentValues();

            //Put
            event.put(CalendarContract.Events.CALENDAR_ID,getCalendar(getContext()));
            event.put(CalendarContract.Events.TITLE,title);
            event.put(CalendarContract.Events.DESCRIPTION,description);
            event.put(CalendarContract.Events.EVENT_LOCATION,location);

            //Time
            event.put(CalendarContract.Events.DTSTART,start.getTime());
            event.put(CalendarContract.Events.DTEND,end.getTime());
            event.put(CalendarContract.Events.ALL_DAY,0);
            event.put(CalendarContract.Events.HAS_ALARM,1);

            String timeZone = TimeZone.getDefault().getID(); //Checked
            event.put(CalendarContract.Events.EVENT_TIMEZONE,timeZone); //Checked

            Uri calendars;
            if(Build.VERSION.SDK_INT >= 8)
                calendars = Uri.parse("content://com.android.calendar/events");
            else
                calendars = Uri.parse("content://calendar/events");

            Uri uri_save = getActivity().getContentResolver().insert(calendars,event); //Checked
            //Save to cache
            Paper.init(getActivity());
            Paper.book().write(Common.EVENT_URI_CACHE,uri_save.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String getCalendar(Context context) {
        //Get default calendar ID of calendar of Gmail
        String gmailIdCalendar = "";
        String projection[]={"_id","calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        //Select all calendar
        Cursor managedCursor = contentResolver.query(calendars,projection,null,null,null);
        if (managedCursor.moveToFirst())
        {
            String calName;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do{
                calName = managedCursor.getString(nameCol);
                if (calName.contains("@gmail.com"))
                {
                    gmailIdCalendar = managedCursor.getString(idCol);
                    break; //Exit as soon as have id
                }
            }while (managedCursor.moveToNext());
            managedCursor.close();
        }
        return gmailIdCalendar;
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentStudio = null;
        Common.currentRoom = null;
        Common.bookingDate.add(Calendar.DATE,0); //Current date added
    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_room_text.setText(Common.currentRoom.getName());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append(" at ")
        .append(simpleDateFormat.format(Common.bookingDate.getTime())));

        txt_studio_address.setText(Common.currentStudio.getAddress());
        txt_studio_website.setText(Common.currentStudio.getWebsite());
        txt_studio_name.setText(Common.currentStudio.getName());
        txt_studio_open_hours.setText(Common.currentStudio.getOpenHours());

    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply format for date display on confirm
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver,new IntentFilter(Common.KEY_CONFIRM_BOOKING));


        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false)
                .build();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        return itemView;
    }
}
