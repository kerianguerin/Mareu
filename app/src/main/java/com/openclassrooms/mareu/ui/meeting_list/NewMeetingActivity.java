package com.openclassrooms.mareu.ui.meeting_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Place;
import com.openclassrooms.mareu.service.MeetingApiService;

import static com.openclassrooms.mareu.utils.Utils.addNewMeeting;
import static com.openclassrooms.mareu.utils.Utils.clr;
import static com.openclassrooms.mareu.utils.Utils.day;
import static com.openclassrooms.mareu.utils.Utils.hours;
import static com.openclassrooms.mareu.utils.Utils.isNotValidTime;
import static com.openclassrooms.mareu.utils.Utils.minutes;
import static com.openclassrooms.mareu.utils.Utils.month;
import static com.openclassrooms.mareu.utils.Utils.validateDate;
import static com.openclassrooms.mareu.utils.Utils.validateParticipants;
import static com.openclassrooms.mareu.utils.Utils.validateSubject;
import static com.openclassrooms.mareu.utils.Utils.validateTime;
import static com.openclassrooms.mareu.utils.Utils.year;

public class NewMeetingActivity extends AppCompatActivity {

    EditText mDate;
    EditText mTime;
    Spinner mPlaceList;
    EditText mParticipants;
    public TextInputLayout lSubject, lPartcipants, lDate, lTime;
    EditText mSubject;
    Button mButton;

    private MeetingApiService mApiService = DI.getMeetingApiService();
    private String placeSelected;
    private String mDateString;

    private final Place[] allPlaces = Place.getAllPlaces();

    public NewMeetingActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initDate();
        initTime();
        initFinnishBtn();
        initViews();
    }

    private void initViews() {
        mPlaceList = findViewById(R.id.place_spinner);
        initPlaceListener();
        lSubject = findViewById(R.id.subject);
        lPartcipants = findViewById(R.id.participants_layout);
        lDate = findViewById(R.id.date_layout);
        lTime = findViewById(R.id.time_layout);
        mSubject = findViewById(R.id.name_input);
        mParticipants = findViewById(R.id.participants_input);
        mSubject.addTextChangedListener(new ValidationTextWatcher(mSubject));
        mDate.addTextChangedListener(new ValidationTextWatcher(mDate));
        mTime.addTextChangedListener(new ValidationTextWatcher(mTime));
        mParticipants.addTextChangedListener(new ValidationTextWatcher(mParticipants));
        initSpinner();
    }

    private void initSpinner() {
        final ArrayAdapter<Place> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allPlaces);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (mPlaceList != null) {
            mPlaceList.setAdapter(adapter);
        }
    }

    private void initPlaceListener() {
        mPlaceList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                placeSelected = mPlaceList.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initFinnishBtn() {
        mButton = findViewById(R.id.form_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place meetingPlace = null;
                if (mPlaceList.getSelectedItem() instanceof Place) {
                    meetingPlace = (Place) mPlaceList.getSelectedItem();
                }
                if (!validateSubject(mSubject.getText().toString(), NewMeetingActivity.this, mSubject) || !validateDate(mDate.getText().toString(), NewMeetingActivity.this, mDate) || !validateDate(mDate.getText().toString(), NewMeetingActivity.this, mDate) || !validateTime(mTime.getText().toString(), NewMeetingActivity.this, mTime, mDateString, mDate.getText().toString(), placeSelected) || isNotValidTime(mApiService.getMeetings(), mDateString, placeSelected) || !validateParticipants(mParticipants.getText().toString(), NewMeetingActivity.this, mParticipants)) {
                    Snackbar.make(v, "Veuillez remplir les champs en rouge correctement", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    addNewMeeting(mApiService.getMeetings().size(), mDateString, meetingPlace.getId(), mSubject.getText().toString(), mParticipants.getText().toString(), NewMeetingActivity.this);
                    Snackbar.make(v, "La réunion a bien été ajoutée!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void initTime() {
        mTime = findViewById(R.id.time_input);
        mTime.setInputType(InputType.TYPE_NULL);
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //time picker dialog
                TimePickerDialog timePicker = new TimePickerDialog(NewMeetingActivity.this, R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int hours, int minutes) {
                        mDateString = mDate.getText().toString() + " " + String.format(hours + ":" + minutes);
                        mTime.setText(String.format("%02dh%02d", hours, minutes));
                    }
                }, hours, minutes, true);
                timePicker.show();
                timePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                timePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                timePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                timePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
    }

    public void initDate() {
        mDate = findViewById(R.id.date_input);
        mDate.setInputType(InputType.TYPE_NULL);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                DatePickerDialog datePicker = new DatePickerDialog(NewMeetingActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dp, int year, int month, int day) {
                        mDateString = String.format("%02d/%02d", day, month + 1) + '/' + year;
                        mDate.setText(mDateString);
                    }
                }, year, month, day);
                datePicker.show();
                datePicker.getDatePicker().setMinDate(clr.getTimeInMillis());
                datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
    }

    private class ValidationTextWatcher implements TextWatcher {
        private View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.name_input:
                    validateSubject(mSubject.getText().toString(), NewMeetingActivity.this, mSubject);
                    break;
                case R.id.date_input:
                    validateDate(mDate.getText().toString(), NewMeetingActivity.this, mDate);
                    break;
                case R.id.time_input:
                    validateTime(mTime.getText().toString(), NewMeetingActivity.this, mTime, mDateString, mDate.getText().toString(), placeSelected);
                    break;
                case R.id.participants_input:
                    validateParticipants(mParticipants.getText().toString(), NewMeetingActivity.this, mParticipants);
                    break;
            }
        }
    }
}
