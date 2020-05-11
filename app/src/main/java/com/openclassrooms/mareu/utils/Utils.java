package com.openclassrooms.mareu.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.Place;
import com.openclassrooms.mareu.service.MeetingApiService;
import com.openclassrooms.mareu.ui.meeting_list.NewMeetingActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final Calendar clr = Calendar.getInstance();
    public static int minutes = clr.get(Calendar.MINUTE);
    public static int hours = clr.get(Calendar.HOUR_OF_DAY);
    public static int day = clr.get(Calendar.DAY_OF_MONTH);
    public static int month = clr.get(Calendar.MONTH);
    public static int year = clr.get(Calendar.YEAR);
    private static Date convertedDate = null;
    private static MeetingApiService mApiService = DI.getMeetingApiService();
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH);
    public static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);

    private static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private static boolean isValideDate(String date) {
        try {
            convertedDate = formatter.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isNotValidTime(List<Meeting> meetings, String mDateString, String placeSelected) {
        boolean validTime = false;
        if (meetings.size() > 0) {
            for (Meeting meeting : meetings) {
                Date date2 = null;
                try {
                    date2 = sdf.parse(mDateString);
                } catch (ParseException e) {
                    validTime = false;
                    e.printStackTrace();
                    break;
                }
                Date date1 = meeting.getDate();
                final Place meetingPlace = meeting.getPlace();
                assert meetingPlace != null;
                String mPlace = meetingPlace.getName();
                assert date2 != null;
                long differenceinMn = Math.abs((date2.getTime() - date1.getTime()) / 60000);
                if (mPlace.equals(placeSelected)) {
                    if (differenceinMn >= 45) {
                        validTime = true;
                    } else {
                        validTime = false;
                        break;
                    }
                } else {
                    validTime = true;
                }
            }
        } else {
            validTime = true;
        }
        return !validTime;
    }

    public static boolean isValidEmail(String mParticipants) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        StringTokenizer email = new StringTokenizer(mParticipants, ",");
        boolean isValidEmail = false;
        while (email.hasMoreElements()) {
            Matcher matcher = pattern.matcher(email.nextToken());
            if (!matcher.matches()) {
                isValidEmail = false;
                break;
            } else {
                isValidEmail = true;
            }
        }
        return isValidEmail;
    }

    public static void addNewMeeting(int mSize, String mDateString, int placeId, String subject, String participants, Context context) {
        int id = mSize + 1;
        int avatar = getRandomColor();
        formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        try {
            convertedDate = sdf.parse(mDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = convertedDate;
        Meeting meeting = new Meeting(id, avatar, date, placeId, subject, participants);
        mApiService.addMeeting(meeting);
        ((NewMeetingActivity) context).finish();
    }

    private static void requestFocus(View view, Context context) {
        if (view.requestFocus()) {
            ((NewMeetingActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static boolean validateSubject(String mSubject, Context context, EditText view) {
        if (mSubject.trim().isEmpty()) {
            ((NewMeetingActivity) context).lSubject.setError("Ce champ est requis!");
            requestFocus(view, context);
            return false;
        } else if (mSubject.length() < 1) {
            ((NewMeetingActivity) context).lSubject.setError("Ce champ doit êter supérieur à 1 caractères!");
            requestFocus(view, context);
            return false;
        } else {
            ((NewMeetingActivity) context).lSubject.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validateDate(String mDate, Context context, EditText view) {
        if (mDate.trim().isEmpty()) {
            ((NewMeetingActivity) context).lDate.setError("Ce champ est requis");
            return false;
        } else if (!Utils.isValideDate(mDate)) {
            ((NewMeetingActivity) context).lDate.setError("Ce champ doit être au format date!");
            requestFocus(view, context);
            return false;
        } else {
            ((NewMeetingActivity) context).lDate.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validateParticipants(String mParticipants, Context context, EditText view) {
        if (mParticipants.trim().isEmpty()) {
            ((NewMeetingActivity) context).lPartcipants.setError("Ce champ est requis!");
            requestFocus(view, context);
            return false;
        } else if (!isValidEmail(mParticipants)) {
            ((NewMeetingActivity) context).lPartcipants.setError("Ce champ doit être au format email (monemail@mail.com,monemail2@mail.com)!");
            requestFocus(view, context);
            return false;
        } else {
            ((NewMeetingActivity) context).lPartcipants.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validateTime(String mTime, Context context, EditText view, String mDateString, String mDate, String placeSelected) {
        Date time = null;

        if (mDateString != null) {
            try {
                time = sdf.parse(mDateString);
            } catch (ParseException e) {
                ((NewMeetingActivity) context).lTime.setError("Ce champ est requis. Vous devez d'abord sélectionner une date. IL doit être de type 10h20!");
                e.printStackTrace();
                return false;
            }
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date now = calendar.getTime();

        if (mTime.trim().isEmpty()) {
            ((NewMeetingActivity) context).lTime.setError("Ce champ est requis!");
            return false;
        } else if (mDate.trim().isEmpty()) {
            ((NewMeetingActivity) context).lDate.setError("Ce champ est requis!");
            return false;
        } else if (time.before(now)) {
            ((NewMeetingActivity) context).lTime.setError("Vous ne pouvez pas organiser de réunion avant l'heure actuelle!");
            return false;
        } else if (!mTime.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3])h[0-5][0-9]$")) {
            ((NewMeetingActivity) context).lTime.setError("Ce champ doit être au format Heure (13:00)!");
            requestFocus(view, context);
            return false;
        } else if (isNotValidTime(mApiService.getMeetings(), mDateString, placeSelected)) {
            ((NewMeetingActivity) context).lTime.setError("Une réunion est déjà prévu dans la même salle dans un intervalle de 45mn!");
            requestFocus(view, context);
            return false;
        } else {
            ((NewMeetingActivity) context).lTime.setErrorEnabled(false);
            return true;
        }
    }

}
