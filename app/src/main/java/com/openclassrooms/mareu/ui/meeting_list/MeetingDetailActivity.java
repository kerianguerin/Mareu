package com.openclassrooms.mareu.ui.meeting_list;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Meeting;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MeetingsActivity}.
 */
public class MeetingDetailActivity extends AppCompatActivity {
    private Meeting meeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            meeting = (Meeting) getIntent().getSerializableExtra(MeetingDetailFragment.ARG_ITEM_ID);
            assert meeting != null;
            assert actionBar != null;
            actionBar.setBackgroundDrawable(new ColorDrawable(meeting.getAvatar()));
            arguments.putSerializable(MeetingDetailFragment.ARG_ITEM_ID, meeting);
            MeetingDetailFragment fragment = new MeetingDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MeetingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
