package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.Place;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.openclassrooms.mareu.utils.Utils.formatter;

public class DummyMeetingApiService implements MeetingApiService {
    private List<Meeting> meetings = new ArrayList<>();

    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public void resetMeetings() {
        meetings = new ArrayList<>();
    }

    /**
     * add a {@Link Meeting} to the meeting list
     *
     * @param meeting
     */
    @Override
    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    /**
     * remove a {@Link Meeting} to the meting list
     *
     * @param meeting
     */
    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    @Override
    public List<Meeting> getFilteredMeetings(Date fDate, List<Place> fPlaces) {
        List<Meeting> fMeetings = new ArrayList<>();
        String mDate1 = null;
        if (fDate != null) {
            mDate1 = formatter.format(fDate);
        }
        if (fPlaces.size() > 0 && fDate == null) {
            for (Meeting meeting : getMeetings()) {
                for (Place place : fPlaces)
                    if (meeting.getPlace().getId() == place.getId()) {
                        fMeetings.add(meeting);
                    }
            }
        } else if (fPlaces.size() > 0) {
            for (Meeting meeting : getMeetings()) {
                String mDate2 = formatter.format(meeting.getDate());
                for (Place place : fPlaces) {
                    if (meeting.getPlace().getId() == place.getId() && mDate2.equals(mDate1)) {
                        fMeetings.add(meeting);
                    }
                }
            }
        } else {
            // If fPlaces.size() = 0
            for (Meeting meeting : getMeetings()) {
                String mDate2 = formatter.format(meeting.getDate());
                if (mDate2.equals(mDate1)) {
                    fMeetings.add(meeting);
                }
            }
        }
        return fMeetings;
    }
}