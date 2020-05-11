package com.openclassrooms.mareu.ui.meeting_list;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Meeting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MeetingsActivity}
 * in two-pane mode (on tablets) or a {@link MeetingsActivity}
 * on handsets.
 */
public class MeetingDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Meeting meeting;
    private String mDate;
    private String mTime;
    private String mPlace;
    private String mSubject;
    private String mParticipants;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MeetingDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (meeting != null) {
            getMeeting();
        }
        assert getArguments() != null;
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            /*
             Load the dummy content specified by the fragment
             arguments. In a real-world scenario, use a Loader
             to load content from a content provider.
            */
            meeting = (Meeting) this.getArguments().getSerializable(ARG_ITEM_ID);
            getMeeting();
            setMeetingTitle();
        }
    }

    private void setMeetingTitle() {
        Activity activity = this.getActivity();
        assert activity != null;
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setBackgroundColor(meeting.getAvatar());
            appBarLayout.setTitle(mSubject);
        }
    }

    private void getMeeting() {
        Integer mId = meeting.getId();
        Integer mAvatar = meeting.getAvatar();
        Date date = meeting.getDate();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        SimpleDateFormat tf = new SimpleDateFormat("HH:ss", Locale.FRANCE);
        mTime = tf.format(date).replace(":", "h");
        mDate = df.format(date);
        mPlace = meeting.getPlace().getName();
        mSubject = meeting.getSubject();
        mParticipants = meeting.getParticipants().replace(",", "\n");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        if (meeting != null) {
            ((TextView) rootView.findViewById(R.id.place_detail)).setText(mPlace);
            ((TextView) rootView.findViewById(R.id.date_detail)).setText(mDate);
            ((TextView) rootView.findViewById(R.id.time_detail)).setText(mTime);
            ((TextView) rootView.findViewById(R.id.participants_detail)).setText(mParticipants);
        }
        return rootView;
    }
}
