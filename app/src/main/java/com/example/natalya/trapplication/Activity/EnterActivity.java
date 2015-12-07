package com.example.natalya.trapplication.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.natalya.trapplication.R;
import com.example.natalya.trapplication.TriathlonApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import bean.TriathlonRecord;
import db.DataSource;


/**
 * Activity that saves or edits a record in database.
 * Activity to enter values of date, duration (and distance) of the record to save or edit.
 */

public class EnterActivity extends Activity implements PopupMenu.OnMenuItemClickListener {

    private static final int MIN = 60;

    private static final int POSITION_TRIATHLON = 0;
    private static final int POSITION_SWIMMING = 1;
    private static final int POSITION_RUNNING = 2;
    private static final int POSITION_CYCLING = 3;

    private int type = TriathlonRecord.TRIATHLON;

    private int menuID = 0;

    private int position;

    int id = -1;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 30000;

    private static final int REQUEST_RESOLVE_ERROR = 1001;

    private static final String DIALOG_ERROR = "dialog_error";
    private boolean mResolvingError = false;

    ToggleButton buttonStart;
    Location mLastLocation;

    EditText editDurationSec;
    EditText editDurationMin;
    EditText editDistance;
    TextView textViewMeters;
    TextView textViewDistance;
    DatePicker datePicker;

    GoogleApiClient mGoogleApiClient;


    /**
     * Method to set the view of the EnterActivity.
     * Sets Up button.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.nothing));

        initView();
    }



    /**
     * Sets the main type of triathlon.
     * Sets proper icon of main type of triathlon.
     */
    @Override
    protected void onResume() {
        super.onResume();

        setSpinner();

        if (getIntent().getBooleanExtra(ListActivity.EXTRA_EDIT_MODE, false)) {
            position = showRecordToEdit();
        }
    }

    private void initView() {

        showTriathlonType(position);

        editDurationSec = (EditText) findViewById(R.id.edit_duration_sec);
        editDurationMin = (EditText) findViewById(R.id.edit_duration_min);
        editDistance = (EditText) findViewById(R.id.edit_distance);

        textViewMeters = (TextView) findViewById(R.id.textView_meters);
        textViewDistance = (TextView) findViewById(R.id.textView_distance);

        datePicker = (DatePicker) findViewById(R.id.datePickerEnter);

        setTextVisibility();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    /**
     * Creates a Spinner on ActionBar.
     * Sets a Listener to manage changes of main triathlon types.
     * Changes icon of triathlon type depending on type of training.
     */
    private void setSpinner() {
        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.tr_list, android.R.layout.simple_spinner_dropdown_item);

        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int pos, long itemId) {
                switch (pos) {
                    case 0:
                        menuID = 0;
                        break;
                    case 1:
                        if (!getIntent().getBooleanExtra(ListActivity.EXTRA_EDIT_MODE, false)) {
                            showPopup(R.menu.popup_swimming);
                        }
                        menuID = R.menu.popup_swimming;
                        break;
                    case 2:
                        if (!getIntent().getBooleanExtra(ListActivity.EXTRA_EDIT_MODE, false)) {
                            showPopup(R.menu.popup_running);
                        }
                        menuID = R.menu.popup_running;
                        break;

                    case 3:
                        if (!getIntent().getBooleanExtra(ListActivity.EXTRA_EDIT_MODE, false)) {
                            showPopup(R.menu.popup_cycling);
                        }
                        menuID = R.menu.popup_cycling;
                        break;

                }
                position = pos;
                showTriathlonType(position);
                return true;
            }


        };

        bar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
        bar.setSelectedNavigationItem(position);
    }

    /**
     * Method to show list of items that indicate period of time to show records of.
     *
     * @param id - id of popup menu to show.
     */
    private void showPopup(int id) {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.exp_list));
        popup.setOnMenuItemClickListener(this);
        popup.inflate(id);
        popup.show();
    }

    /**
     * Sets type depending on chosen popup-menu item.
     * Sets proper visual content depending on chosen type.
     *
     * @param item - chosen item.
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.run_exercises:
                type = TriathlonRecord.TYPE_RUNNING_EXERCISE;
                break;
            case R.id.run_running:
                type = TriathlonRecord.TYPE_RUNNING;
                break;
            case R.id.bicycle:
                type = TriathlonRecord.TYPE_BICYCLE;
                break;
            case R.id.trainer:
                type = TriathlonRecord.TYPE_TRAINER;
                break;
            case R.id.sw_exercises:
                type = (TriathlonRecord.TYPE_SWIMMING_EXERCISE);
                break;
            case R.id.freestyle:
                type = (TriathlonRecord.TYPE_FREESTYLE);
                break;
            case R.id.butterfly:
                type = (TriathlonRecord.TYPE_BUTTERFLY);
                break;
            case R.id.breaststroke:
                type = (TriathlonRecord.TYPE_BREASTSTROKE);
                break;
            case R.id.backstroke:
                type = (TriathlonRecord.TYPE_BACKSTROKE);
                break;

        }
        setType(type);
        setTextVisibility();
        return true;
    }

    /**
     * Indicates which item of Action Bar was chosen and calls proper method.
     *
     * @param item - - chosen item.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (getIntent().getBooleanExtra(ListActivity.EXTRA_EDIT_MODE, false)) {
                    editRecord();
                } else {
                    saveRecord();
                }
                break;
            case R.id.exp_list:
                if (menuID != 0) {
                    showPopup(menuID);
                } else {
                    showToast(getString(R.string.wrong_type));
                }
                break;
            case android.R.id.home:
                startHistoryActivity();
                break;
        }
        return false;
    }


    /**
     * Method to set proper visual content.
     */
    private void setTextVisibility() {

        if (type == TriathlonRecord.TYPE_TRAINER) {
            editDistance.setVisibility(View.INVISIBLE);
            textViewMeters.setVisibility(View.INVISIBLE);
            textViewDistance.setVisibility(View.INVISIBLE);
        } else {
            editDistance.setVisibility(View.VISIBLE);
            textViewMeters.setVisibility(View.VISIBLE);
            textViewDistance.setVisibility(View.VISIBLE);
            editDistance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDistance.setText(getString(R.string.nothing));
                }
            });
        }
        editDurationMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDurationMin.setText(getString(R.string.nothing));
            }
        });
        editDurationSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDurationSec.setText(getString(R.string.nothing));
            }
        });
    }


    /**
     * Method to start HistoryActivity activity.
     */
    public void startHistoryActivity() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    /**
     * Method that shows previously saved value of date, duration (and distance) of record to edit.
     */
    private int showRecordToEdit() {
        id = getIntent().getIntExtra("id", -1);
        DataSource dataSource = ((TriathlonApplication) getApplication()).getDataSource();
        TriathlonRecord record = dataSource.getRecordsById(id);

        DecimalFormat decimalFormat = new DecimalFormat("#");
        if (record.getDuration() < MIN) {
            editDurationSec.setText(String.valueOf(record.getDuration()));
        } else {
            editDurationSec.setText(String.valueOf(record.getDuration() - Math.floor(record.getDuration() / MIN) * MIN));
        }
        editDurationMin.setText(String.valueOf(decimalFormat.format(Math.floor(record.getDuration() / MIN))));
        editDistance.setText(String.valueOf(record.getDistance()));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(record.getDate().getTime());
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        setType(record.getType());
        int t = record.getType();
        if (t != TriathlonRecord.TRIATHLON && t < TriathlonRecord.RUNNING) {
            position = POSITION_SWIMMING;
        }
        if (t == TriathlonRecord.TYPE_RUNNING || t == TriathlonRecord.TYPE_RUNNING_EXERCISE || t == TriathlonRecord.RUNNING) {
            position = POSITION_RUNNING;
        }
        if (t == TriathlonRecord.TYPE_BICYCLE || t == TriathlonRecord.TYPE_TRAINER || t == TriathlonRecord.CYCLING) {
            position = POSITION_CYCLING;
        }
        return position;
    }

    /**
     * Method to save in database a new Triathlon-type record.
     */
    private void saveRecord() {
        int distance;
        double duration;
        //checking for bad type.
        if (type == TriathlonRecord.SWIMMING || type == TriathlonRecord.RUNNING || type == TriathlonRecord.CYCLING
                || type == TriathlonRecord.TRIATHLON) {
            showToast(getString(R.string.wrong_type));
        } else {
            //checking for empty fields.
            if ((editDurationMin.getText().toString().equals("") &&
                    editDurationSec.getText().toString().equals("")) ||
                    (editDurationMin.getText().toString().equals(getString(R.string.zero)) &&
                            (editDurationSec.getText().toString().equals(getString(R.string.zero)) ||
                                    editDurationSec.getText().toString().equals(getString(R.string.zero_point_zero)))) ||
                    (editDistance.getText().toString().equals("") ||
                            editDistance.getText().toString().equals(getString(R.string.zero)) &&
                                    type != TriathlonRecord.TYPE_TRAINER)) {
                showToast(getString(R.string.wrong_data));
            } else {
                //checking if one of the duration fields is empty
                if (editDurationMin.getText().toString().equals("")) {
                    duration = Double.valueOf(editDurationSec.getText().toString());
                } else if (editDurationSec.getText().toString().equals("")) {
                    duration = Double.valueOf(((EditText) findViewById(R.id.edit_duration_min)).getText().toString()) * MIN;
                } else {
                    duration = Double.valueOf(((EditText) findViewById(R.id.edit_duration_sec)).getText().toString()) +
                            Integer.valueOf(((EditText) findViewById(R.id.edit_duration_min)).getText().toString()) * MIN;
                }
                if (type == TriathlonRecord.TYPE_TRAINER) {
                    distance = 0;
                } else {
                    distance = Integer.valueOf(
                            ((EditText) findViewById(R.id.edit_distance)).getText().toString());
                }

                final Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                Date date = calendar.getTime();
                //checking for future date.
                if (date.getTime() > System.currentTimeMillis()) {
                    showToast(getString(R.string.wrong_date));
                } else {
                    TriathlonApplication triathlonApplication = (TriathlonApplication) getApplication();
                    triathlonApplication.getDataSource().createRecord(type, duration, distance, date);
                    if (getIntent().getBooleanExtra(ListActivity.EXTRA_EDIT_MODE, false)) {
                        showToast(getString(R.string.edited));
                    } else {
                        showToast(getString(R.string.saved));
                    }

                    editDistance.setText(R.string.zero);
                    editDurationSec.setText(R.string.zero_point_zero);
                    editDurationMin.setText(R.string.zero);

                }
            }
        }

    }


    /**
     * Method to show icon of type of triathlon on the tool bar.
     */
    public void showTriathlonType(int position) {
        switch (position) {
            case POSITION_TRIATHLON:
                getActionBar().setIcon(R.drawable.ic_launcher1);
                break;
            case POSITION_SWIMMING:
                getActionBar().setIcon(R.drawable.swimming);
                break;
            case POSITION_RUNNING:
                getActionBar().setIcon(R.drawable.running);
                break;
            case POSITION_CYCLING:
                getActionBar().setIcon(R.drawable.cycling);
                break;
        }
    }


    /**
     * Method to send user a message of result of some of his actions.
     *
     * @param s is a content of the message to show.
     */
    public void showToast(String s) {
        Context context = getApplicationContext();
        int durationOfToast = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, s, durationOfToast);
        toast.show();
    }

    /**
     * Method that edits Triathlon-type record by id.
     */
    private void editRecord() {
        saveRecord();
        TriathlonApplication triathlonApplication = (TriathlonApplication) getApplication();
        DataSource ds = triathlonApplication.getDataSource();
        id = getIntent().getIntExtra(ListActivity.EXTRA_ID, -1);
        ds.deleteRecord(id);
    }

    /**
     * Sets type.
     *
     * @param position value to be set.
     */
    private void setType(int position) {
        type = position;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }
}
