package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.model.Meeting;

public class DeleteMeetingEvent {
    public Meeting meeting;

    public DeleteMeetingEvent(Meeting meeting) {
        this.meeting = meeting;
    }
}
