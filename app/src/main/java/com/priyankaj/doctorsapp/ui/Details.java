package com.priyankaj.doctorsapp.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.priyankaj.doctorsapp.R;
import com.priyankaj.doctorsapp.model.AboutDetails;
import com.priyankaj.doctorsapp.model.AppointmentDetailsRequest;
import com.priyankaj.doctorsapp.model.CategoryDetails;
import com.priyankaj.doctorsapp.model.City;
import com.priyankaj.doctorsapp.model.Doctors;
import com.priyankaj.doctorsapp.model.VisionDetails;
import com.priyankaj.doctorsapp.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class Details extends AppCompatActivity implements DoctorAppContract.View{
    static EditText DateEdit;
    static EditText timeEdit;
    private Button btnBook;
    private EditText edtName;
    private EditText edtContact;
    private DoctorAppContract.Presenter presenter;
    private static final int RC_SIGN_IN = 1;
    private boolean isUserSignedin;
    private MaterialProgressBar progress;
    private boolean isValidName,isValidPhone,isValidDate,isValidPhoneLength,isValidTime;
    private static Activity detailsActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        View view = LayoutInflater.from(this).inflate(R.layout.abs_layout,null);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        TextView txtName = view.findViewById(R.id.mytext);
        txtName.setText("Book");
        getSupportActionBar().setCustomView(view);
        detailsActivity = Details.this;

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();

//         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        PresenterInjector.injectDoctorAppPresenter(this);

        DateEdit = (EditText) findViewById(R.id.et1);
        timeEdit = (EditText) findViewById(R.id.et2);
        timeEdit.setEnabled(false);
        edtName = findViewById(R.id.edt_name);
        edtContact = findViewById(R.id.edt_contact);
        progress = findViewById(R.id.progress);

        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
            }
        });
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonTimePickerDialog(v);
            }
        });

        btnBook = findViewById(R.id.btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!isUserSignedin){
//                    signIn();
//                }

                if(Utils.isNetworkConnected(Details.this))
                {
                    if(validateInput()){
                        AppointmentDetailsRequest appointmentDetailsRequest = new AppointmentDetailsRequest();
                        appointmentDetailsRequest.setMobile(edtContact.getText().toString());
                        appointmentDetailsRequest.setDate(DateEdit.getText().toString());
                        appointmentDetailsRequest.setTime(timeEdit.getText().toString());
                        appointmentDetailsRequest.setName(edtName.getText().toString());
                        appointmentDetailsRequest.setDoctorId(getIntent().getStringExtra("doctor_id"));
                        appointmentDetailsRequest.setRegdate(getIntent().getStringExtra("reg_date"));
                        appointmentDetailsRequest.setRemarks("Booked through app");
                        progress.setVisibility(View.VISIBLE);
                        presenter.sendFormData(Details.this,appointmentDetailsRequest);
                    }
                }else
                {
                    Toast.makeText(Details.this, getResources().getString(R.string.internet_error_message).toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private static int mYear,  mMonth,  mDay;
    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {

            try {
                mYear = year;

                mDay = day;
                month = month+1;
                mMonth = month;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date selectedDate =  dateFormat.parse(day + "/" + month + "/" + year);
                Date date = new Date();
                Date dateCurrent = dateFormat.parse(dateFormat.format(date));
                if(selectedDate.before(dateCurrent)){
                    showToast("Please select a future date",detailsActivity);
                }else
                {
                    DateEdit.setText(day + "/" + (month + 1) + "/" + year);
                    timeEdit.setEnabled(true);
                }
            }catch (ParseException e){

            }
            // Do something with the date chosen by the user

        }
    }

    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
                Date selectedDate =  dateFormat.parse(mDay + "/" + mMonth + "/" + mYear+" "+hourOfDay+":"+minute);
                Date date = new Date();
                Date dateCurrent = dateFormat.parse(dateFormat.format(date));
                if(selectedDate.before(dateCurrent)){
                    showToast("Please select a future time",detailsActivity);
                }else
                {
                    SimpleDateFormat dateFormatAmpm = new SimpleDateFormat("hh:mm a",Locale.getDefault());
                    timeEdit.setText(dateFormatAmpm.format(selectedDate));
                }
            }catch (ParseException e){

            }
        }
    }

    @Override
    public void setPresenter(DoctorAppContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayCategoryDetails(ArrayList<CategoryDetails.Category> categoryDetailsList) {

    }

    @Override
    public void displayVisionDetails(ArrayList<VisionDetails.Vision> visionDetailsList) {

    }

    @Override
    public void displayAboutDetails(ArrayList<AboutDetails.AboutUs> aboutDetailsList) {

    }

    @Override
    public void displayDoctorDetails(ArrayList<Doctors> doctorDetailsList) {

    }

    @Override
    public void showformDisplaySuccess(String appointments) {

        progress.setVisibility(View.GONE);
        Toast.makeText(this,"Appointment scheduled successfully!!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Details.this,Slider.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private boolean validateInput(){
        if(edtName.getText()!=null && !TextUtils.isEmpty(edtName.getText().toString())){
            isValidName = true;
        }else
        {
            isValidName = false;
        }

        if(edtContact.getText()!=null &&!TextUtils.isEmpty(edtContact.getText().toString())){
            isValidPhone = true;
            if(edtContact.getText().toString().length()!=10){
                isValidPhoneLength = false;
            }else
            {
                isValidPhoneLength = true;
            }
        }else
        {
            isValidPhone = false;
        }

        if(DateEdit.getText()!=null &&!TextUtils.isEmpty(DateEdit.getText().toString())){
            isValidDate = true;
        }else
        {
            isValidDate = false;
        }

        if(timeEdit.getText()!=null &&!TextUtils.isEmpty(timeEdit.getText().toString())){
            isValidTime = true;
        }else
        {
            isValidTime = false;
        }

        if(!isValidName){
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return false;
        } if(!isValidPhone){
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return false;
        }  if(!isValidPhoneLength){
            Toast.makeText(this, "Phone number should be 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        } if(!isValidDate){
            Toast.makeText(this, "Please enter a date", Toast.LENGTH_SHORT).show();
            return false;
        }if(!isValidTime){
            Toast.makeText(this, "Please enter a time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(isValidName&&isValidPhone&&isValidDate&&isValidTime){
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if(account!=null){
//            isUserSignedin = true;
//        }

    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if(validateInput()){
                AppointmentDetailsRequest appointmentDetailsRequest = new AppointmentDetailsRequest();
                appointmentDetailsRequest.setMobile(edtContact.getText().toString());
                appointmentDetailsRequest.setDate(DateEdit.getText().toString());
                appointmentDetailsRequest.setTime(timeEdit.getText().toString());
                appointmentDetailsRequest.setName(edtName.getText().toString());
                presenter.sendFormData(this,appointmentDetailsRequest);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this,"Please signin to continue",Toast.LENGTH_LONG).show();
        }
    }

//    private void insertValuesInCalendar(){
//        long calID = 3;
//        long startMillis = 0;
//        long endMillis = 0;
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(2012, 9, 14, 7, 30);
//        startMillis = beginTime.getTimeInMillis();
//        Calendar endTime = Calendar.getInstance();
//        endTime.set(2012, 9, 14, 8, 45);
//        endMillis = endTime.getTimeInMillis();
//
//        ContentResolver cr = getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put(CalendarContract.Events.DTSTART, startMillis);
//        values.put(CalendarContract.Events.DTEND, endMillis);
//        values.put(CalendarContract.Events.TITLE, "Jazzercise");
//        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
//        values.put(CalendarContract.Events.CALENDAR_ID, calID);
//        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
//        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
//    }


    @Override
    public void showformDisplayFaliure(String message) {

    }

    @Override
    public void fetchDataFailure(String message) {

    }

    private static void showToast(String text, Activity context){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayCityDetails(ArrayList<City> cityArrayList) {

    }
}

