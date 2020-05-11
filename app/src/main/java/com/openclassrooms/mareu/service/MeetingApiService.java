package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.Place;

import java.util.Date;
import java.util.List;

/**
 * Meeting Api Service
 */
public interface MeetingApiService {

    /**
     * @return {@Link List}
     */
    List<Meeting> getMeetings();


    /**
     * Reset meeting List
     */
    void resetMeetings();

    /**
     * Add a meeting
     *
     * @param  meeting
     */
    void addMeeting(Meeting meeting);

    /**
     * Delete a meeting
     *
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

    List<Meeting> getFilteredMeetings(Date fDate, List<Place> fPlaces);

}

