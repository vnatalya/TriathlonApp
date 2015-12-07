/*
package com.example.natalya.trapplication.Activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.natalya.trapplication.R;
import com.example.natalya.trapplication.TriathlonApplication;

import java.util.List;

import bean.Triathlon;
import db.DataSource;

*/
/**
 * Created by Natalya on 24.03.2015.
 *//*

public abstract class BaseActivity extends TabActivity implements PopupMenu.OnMenuItemClickListener {

    protected int type = Triathlon.TRIATHLON;

    protected DataSource.Period period = DataSource.Period.ALL;

    private final static int SWIMMING_NUMBERS = 5;



    */
/**
 * Method to show icon of type of triathlon on the tool bar.
 *//*

    protected void showTriathlonType(){
        if (type != Triathlon.TRIATHLON && type < Triathlon.RUNNING) {
//            setTitle(R.string.swimming);
            getActionBar().setIcon(R.drawable.swimming);
        }
        if (type == Triathlon.TYPE_RUNNING || type == Triathlon.TYPE_RUNNING_EXERCISE || type == Triathlon.RUNNING) {
//            setTitle(R.string.running);
            getActionBar().setIcon(R.drawable.running);
        }
        if (type == Triathlon.TYPE_BICYCLE || type == Triathlon.TYPE_TRAINER || type == Triathlon.CYCLING){
//            setTitle(R.string.cycling);
            getActionBar().setIcon(R.drawable.cycling);
        }
        if (type == Triathlon.TRIATHLON){
            getActionBar().setIcon(R.drawable.ic_launcher1);
        }
    }

    */
/**
 * Method to start TriathlonTypeActivity. Sends extra data - type of Triathlon to show.
 *//*

    protected void startHistoryActivity(int type) {
        int extraType = -1;
        if (type < SWIMMING_NUMBERS || type == Triathlon.SWIMMING) {
            extraType = Triathlon.SWIMMING;
        }
        if (type == Triathlon.TYPE_RUNNING || type == Triathlon.TYPE_RUNNING_EXERCISE || type == Triathlon.RUNNING) {
            extraType = Triathlon.RUNNING;
        }
        if (type == Triathlon.TYPE_BICYCLE || type == Triathlon.TYPE_TRAINER || type == Triathlon.CYCLING){
            extraType = Triathlon.CYCLING;
        }
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("type", extraType);
        startActivity(intent);
    }

    */
/**
 * Method to send user a message of result of some of his actions.
 * @param s is a content of the message to show.
 *//*

    protected void showToast(String s) {
        Context context = getApplicationContext();
        int durationOfToast = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, s, durationOfToast);
        toast.show();
    }

    protected List<Triathlon> getRecords(){
        TriathlonApplication triathlonApplication = (TriathlonApplication) getApplication();
        List<Triathlon> records;
        if (getIntent().getBooleanExtra("choose", false)) {
            records = triathlonApplication.getDataSource().
                    getChoosePeriodRecords(getIntent().getLongExtra("fromDate", 0),
                            getIntent().getLongExtra("toDate", 10000), type);
        } else {
            records = triathlonApplication.getDataSource().getAllRecords(period, type);
        }
        return records;
    }
    protected void setType(int position)
    {
        type = position;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_record:
                startEnterActivity();
                break;
            case R.id.history:
                showPopup(findViewById(R.id.history));
                break;
        }
        return true;
    }

    private void startEnterActivity() {
        Intent intent = new Intent(this, EnterActivity.class);
        //intent.putExtra("type", getIntent().getIntExtra("type", -1));
        startActivity(intent);
    }
    */
/**
 * Method to show list of items that indicate period of time to show records of.
 *
 * @param view
 *//*

    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        popup.inflate(R.menu.popup_menu_history);
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.today:
                setPeriod(DataSource.Period.TODAY);
                break;
            case R.id.week:
                setPeriod(DataSource.Period.WEEK);
                break;
            case R.id.month:
                setPeriod(DataSource.Period.MONTH);
                break;
            case R.id.choose:
                startChoosingActivity();
                break;
            default:
                return false;
        }
        return true;
    }


    protected void setPeriod(DataSource.Period per){
        period = per;
        Log.e("period", String.valueOf(period));
    }


    */
/**
 * Method to start another activity. /n
 * Starts ChoosingActivity, depends on parameter.
 *
 *//*

    private void startChoosingActivity() {
        Intent intent = new Intent(this, ChoosingActivity*/
/**//*
.class);
        startActivity(intent);
    }

}
*/
