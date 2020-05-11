package com.openclassrooms.mareu.ui.meeting_list.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.events.DeleteMeetingEvent;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.Place;
import com.openclassrooms.mareu.ui.meeting_list.MeetingDetailActivity;
import com.openclassrooms.mareu.ui.meeting_list.MeetingDetailFragment;
import com.openclassrooms.mareu.ui.meeting_list.MeetingsActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MeetingAdapter
        extends RecyclerView.Adapter<MeetingListViewHolder> {

    private final MeetingsActivity mParentActivity;
    public List<Meeting> mMeetings;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Meeting meeting = (Meeting) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putSerializable(MeetingDetailFragment.ARG_ITEM_ID, meeting);
                MeetingDetailFragment fragment = new MeetingDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Bundle arguments = new Bundle();
                arguments.putSerializable(MeetingDetailFragment.ARG_ITEM_ID, meeting);
                Intent intent = new Intent(context, MeetingDetailActivity.class);
                intent.putExtras(arguments);
                context.startActivity(intent);
            }
        }
    };

    public MeetingAdapter(MeetingsActivity parent,
                          List<Meeting> meetings,
                          boolean twoPane) {
        mMeetings = meetings;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public MeetingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new MeetingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MeetingListViewHolder holder, int position) {
        final Meeting meeting = mMeetings.get(position);
        Date date = mMeetings.get(position).getDate();
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String formattedDate = "";
        String formattedTime = "";
        if (date != null) {
            formattedDate = df.format(date);
            formattedTime = tf.format(date);

        }
        final Place meetingPlace = meeting.getPlace();
        assert meetingPlace != null;
        String Title = mMeetings.get(position).getSubject() + " - " + formattedDate + " - " + formattedTime.replace(":", "h") + " - " + meetingPlace.getName();
        String Subtitle = meeting.getParticipants();
        GradientDrawable drawable = (GradientDrawable) holder.mAvatarView.getDrawable();
        drawable.setColor(meeting.getAvatar());
        holder.mSubtitleView.setText(Subtitle);
        holder.mTitleView.setText(Title);
        holder.itemView.setTag(mMeetings.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }
}
