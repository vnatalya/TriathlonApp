package com.example.natalya.trapplication.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.natalya.trapplication.R;

import java.util.Calendar;

/**
 * Activity to choose the period of time to display the results in History Activity.
 */
public class ChoosingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_period_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Method to get initial date to show records of in HistoryActivity from DatePicker.
     * @return long - initial date.
     */
    public long getFromDate() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePickerFrom);
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return calendar.getTimeInMillis();
    }

    /**
     * Method to get final date to show records of in HistoryActivity from DatePicker.
     * @return long - final date.
     */
    public long getToDate() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePickerTo);
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return calendar.getTimeInMillis();
    }

    /**
     * Method to start HistoryActivity. Sends to HistoryActivity type, initial and final date to
     * display results of.
     * @param v
     */
    public void goToHistoryActivityFromChoosing(View v) {
        if (getToDate()<getFromDate() || getToDate() > Calendar.getInstance().getTimeInMillis()){
            Context context = getApplicationContext();
            int durationOfToast = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, getString(R.string.wrong_date), durationOfToast);
            toast.show();
        } else {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra(ListActivity.EXTRA_FROM_DATE, getFromDate());
            intent.putExtra(ListActivity.EXTRA_TO_DATE, getToDate());
            intent.putExtra(ListActivity.EXTRA_CHOOSE, true);
            startActivity(intent);
        }
    }

    /**
     * Indicates which item of Action Bar was chosen and calls proper method.
     * @param item - - chosen item.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startHistoryActivity();
                break;
        }
        return true;
    }

    /**
     * Method to start HistoryActivity activity.
     */
    private void startHistoryActivity() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

}
