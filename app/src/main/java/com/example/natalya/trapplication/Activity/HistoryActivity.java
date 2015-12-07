package com.example.natalya.trapplication.Activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.natalya.trapplication.R;

import bean.TriathlonRecord;
import db.DataSource;

/**
 * Activity to show results of trainings.
 */
public class HistoryActivity extends TabActivity implements
        ActionBar.TabListener, AdapterView.OnItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    DataSource.Period period = DataSource.Period.ALL;
    int type = TriathlonRecord.TRIATHLON;
    int COUNTER = 0;
    int menuID = 0;

    /**
     * Sets the view of activity.
     * Sets proper icon of main type of triathlon.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        showTriathlonType();
        setSpinner();
        getActionBar().setTitle(getString(R.string.nothing));

        if(getIntent().getBooleanExtra(ListActivity.EXTRA_CHOOSE, false)){
        //    type = getIntent().getIntExtra("type", -1);
            period = DataSource.Period.CHOOSE;
        }

       // actionBar.setSelectedNavigationItem(position);

        createTab(SummarisedActivity.class, getString(R.string.summarized_tab), "sum", period, type);
        createTab(ListActivity.class, getString(R.string.list_tab), "list", period, type);

    }

    /**
     * Creates a Spinner on ActionBar.
     * Sets a Listener to manage changes of main triathlon types.
     * Changes icon of triathlon type depending on type of training.
     * Sets a type of triathlon.
     */
    private void setSpinner(){
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.tr_list, android.R.layout.simple_spinner_dropdown_item);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                switch (position) {
                    case 0:
                        setType(TriathlonRecord.TRIATHLON);
                        menuID = 0;
                        break;
                    case 1:
                        setType(TriathlonRecord.SWIMMING);
                        menuID = R.menu.popup_swimming;
                        break;
                    case 2:
                        setType(TriathlonRecord.RUNNING);
                        menuID = R.menu.popup_running;
                        break;
                    case 3:
                        setType(TriathlonRecord.CYCLING);
                        menuID = R.menu.popup_cycling;
                        break;
                }
                showTriathlonType();
                return true;
            }

        };

        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }

    /**
     * Method to create a new tab.
     * @param tabClass - class of the tab.
     * @param indicator - label of the tab.
     * @param tag -  tag that is used to keep track of the tab.
     * @param p - period to show results of.
     * @param t - type to show records of.
     */
    private void createTab(Class tabClass, String indicator, String tag, DataSource.Period p, int t) {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec tab = tabHost.newTabSpec(tag);
        Intent intent = new Intent(this, tabClass);
        intent.putExtra(ListActivity.EXTRA_PERIOD, p);
        intent.putExtra(ListActivity.EXTRA_TYPE, t);
        if (getIntent().getBooleanExtra(ListActivity.EXTRA_CHOOSE, false) && period == DataSource.Period.CHOOSE){
            intent.putExtra(ListActivity.EXTRA_CHOOSE, true);
            intent.putExtra(ListActivity.EXTRA_FROM_DATE, getIntent().getLongExtra(ListActivity.EXTRA_FROM_DATE, 0));
            intent.putExtra(ListActivity.EXTRA_TO_DATE, getIntent().getLongExtra(ListActivity.EXTRA_TO_DATE, 100000));
        }
        tab.setContent(intent);
        tab.setIndicator(indicator);
        tabHost.addTab(tab);
    }


    /**
     * Sets period to show results of.
     * Deletes previous tabs and creates new of proper period.
     * @param per - value to be set as period.
     */
    private void setPeriod(DataSource.Period per) {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.clearAllTabs();
        createTab(SummarisedActivity.class, getString(R.string.summarized_tab),
                String.valueOf(COUNTER), per, type);
        createTab(ListActivity.class, getString(R.string.list_tab),
                String.valueOf(COUNTER), per, type);
        ++COUNTER;
        period = per;
    }

    /**
     * Sets type to show results of.
     * Deletes previous tabs and creates new of proper type.
     * @param t - value to be set as type.
     */
    private void setType(int t) {
        type = t;
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.clearAllTabs();
        createTab(SummarisedActivity.class, getString(R.string.summarized_tab),
                String.valueOf(COUNTER), period, type);
        createTab(ListActivity.class, getString(R.string.list_tab),
                String.valueOf(COUNTER), period, type);
        ++COUNTER;
    }

    /**
     * Method to show icon of type of triathlon on the tool bar.
     */

    protected void showTriathlonType() {
        if (type != TriathlonRecord.TRIATHLON && type < TriathlonRecord.RUNNING) {
            getActionBar().setIcon(R.drawable.swimming);
        }
        if (type == TriathlonRecord.TYPE_RUNNING || type == TriathlonRecord.TYPE_RUNNING_EXERCISE || type == TriathlonRecord.RUNNING) {

            getActionBar().setIcon(R.drawable.running);
        }
        if (type == TriathlonRecord.TYPE_BICYCLE || type == TriathlonRecord.TYPE_TRAINER || type == TriathlonRecord.CYCLING) {
            getActionBar().setIcon(R.drawable.cycling);
        }
        if (type == TriathlonRecord.TRIATHLON) {
            getActionBar().setIcon(R.drawable.ic_launcher1);
        }
    }


    /**
     * Method to show list of items that indicate period of time to show results of.
     *
     * @param view - a view to pin popup menu.
     */
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu_history);
        popup.show();
    }

    /**
     * Method to show list of items that indicate type to show results of.
     *
     * @param id - id of popup menu to show.
     */
    public void showPopup(int id) {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.exp_list));
        popup.setOnMenuItemClickListener(this);
        popup.inflate(id);
        popup.show();
    }

    /**
     * Sets type or period depending on chosen popup-menu item.
     *
     * @param item
     * @return
     */
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
            case R.id.all:
                setPeriod(DataSource.Period.ALL);
                break;
            case R.id.run_exercises:
                setType(TriathlonRecord.TYPE_RUNNING_EXERCISE);
                break;
            case R.id.run_running:
                setType(TriathlonRecord.TYPE_RUNNING);
                break;
            case R.id.bicycle:
                setType(TriathlonRecord.TYPE_BICYCLE);
                break;
            case R.id.trainer:
                setType(TriathlonRecord.TYPE_TRAINER);
                break;
            case R.id.sw_exercises:
                setType(TriathlonRecord.TYPE_SWIMMING_EXERCISE);
                break;
            case R.id.freestyle:
                setType(TriathlonRecord.TYPE_FREESTYLE);
                break;
            case R.id.butterfly:
               setType(TriathlonRecord.TYPE_BUTTERFLY);
                break;
            case R.id.breaststroke:
                setType(TriathlonRecord.TYPE_BREASTSTROKE);
                break;
            case R.id.backstroke:
                setType(TriathlonRecord.TYPE_BACKSTROKE);
                break;
        }
        return true;
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
            case R.id.exp_list:
                if (menuID != 0) {
                    showPopup(menuID);
                } else {
                    showToast(getString(R.string.wrong_type));
                }
                break;

        }
        return true;
    }

    private void startEnterActivity() {
        Intent intent = new Intent(this, EnterActivity.class);
        startActivity(intent);
    }

    private void startChoosingActivity() {
        Intent intent = new Intent(this, ChoosingActivity.class);
        startActivity(intent);
    }

    /**
     * Method to send user a message of result of some of his actions.
     *
     * @param s is a content of the message to show.
     */
    private void showToast(String s) {
        Context context = getApplicationContext();
        int durationOfToast = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, s, durationOfToast);
        toast.show();
    }


    public DataSource.Period getPeriod() {
        return period;
    }

    public int getType() {
        return type;
    }

    /**
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {


    }

    /**
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    /**
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

}