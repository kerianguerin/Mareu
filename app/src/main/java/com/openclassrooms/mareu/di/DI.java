package com.openclassrooms.mareu.di;

import com.openclassrooms.mareu.service.DummyMeetingApiService;
import com.openclassrooms.mareu.service.MeetingApiService;

public class DI {
    private static MeetingApiService service;

    static {
        service = new DummyMeetingApiService();
    }

    /**
     * Get a new instance on ]{@Link MeetingApiService}
     */
    public static MeetingApiService getMeetingApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link MeetingApiService}. Useful for tests, so we ensure the context is clean.
     *
     * @return
     */
    public static MeetingApiService getNewInstanceApiService() {
        return new DummyMeetingApiService();
    }
}
