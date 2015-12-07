package com.example.natalya.trapplication.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.natalya.trapplication.R;
import com.example.natalya.trapplication.TriathlonApplication;

import java.util.List;

import bean.TriathlonRecord;
import db.DataSource;

/**
 * Activity to show results of trainings.
 * Activity that shows records of certain type of Triathlon and of certain period of time.
 */
public class ListActivity extends Activity {

    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_FROM_DATE = "fromDate";
    public static final String EXTRA_TO_DATE = "toDate";
    public static final String EXTRA_CHOOSE = "choose";
    public static final String EXTRA_PERIOD = "period";
    public static final String EXTRA_EDIT_MODE  = "editMode";
    public static final String EXTRA_ID  = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.history_layout);
        setContextualMenu(setRecordsInAdapter());
      //  Button button = (Button) findViewById(R.id.buttonSaveImage);
      //  button.setVisibility(View.INVISIBLE);
    }

    /**
     * Method to set the records that should be displayed.
     *
     * @return ArrayAdapter contains the records.
     */
    private ArrayAdapter setRecordsInAdapter() {
        List<TriathlonRecord> records = getRecords();
        ArrayAdapter adapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, records);
        return adapter;
    }

    /**
     * Creates contextual menu for records.
     *
     * @param arrayAdapter contains the records.
     */
    private void setContextualMenu(final ArrayAdapter arrayAdapter) {
        final ListView listView = (ListView) findViewById(R.id.listView_history);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                actionMode.setTitle(listView.getCheckedItemCount() +
                        getText(R.string.selected_items).toString());
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.contextual_menu_history, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                DataSource ds = ((TriathlonApplication) getApplication()).getDataSource();
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        for (int i = arrayAdapter.getCount() - 1; i >= 0; i--) {
                            if (checkedItems.get(i)) {
                                ds.deleteRecord(((TriathlonRecord) arrayAdapter.getItem(i)).getId());
                                arrayAdapter.remove(arrayAdapter.getItem(i));
                            }
                        }
                        showToast(String.valueOf(listView.getCheckedItemCount()) + " "
                                + getText(R.string.deleted_items).toString());
                        break;
                    case R.id.edit:
                        for (int i = arrayAdapter.getCount() - 1; i >= 0; i--) {
                            if (checkedItems.get(i)) {
                                if (listView.getCheckedItemCount() > 1) {
                                    showToast(getText(R.string.too_much_to_edit).toString());
                                } else {
                                    startEnterActivity(((TriathlonRecord) arrayAdapter.getItem(i)).getId());
                                    break;
                                }
                            }
                        }

                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

    }

    /**
     * Method to start EnterActivity to edit a record.
     *
     * @param id - id of record to edit.
     */
    private void startEnterActivity(int id) {
        Intent intent = new Intent(this, EnterActivity.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_EDIT_MODE, true);
        startActivity(intent);
    }

    /**
     * Method to start SummarisedActivity. Sends extra data - period and type of Triathlon to show total reports of.
     *//*
    private void startSummarisedActivity() {
        Intent intent = new Intent(this, SummarisedActivity.class);
        if (getIntent().getBooleanExtra("choose", false)) {
            intent.putExtra("choose", true);
            intent.putExtra("fromDate", getIntent().getLongExtra("fromDate", 0));
            intent.putExtra("toDate", getIntent().getLongExtra("toDate", 10000));
        }
     //   intent.putExtra("period", getIntent().getSerializableExtra("period"));
    //
    //
    //
    //  intent.putExtra("type", getIntent().getIntExtra("type", 474));
        startActivity(intent);
    }*/
    protected List<TriathlonRecord> getRecords() {
        TriathlonApplication triathlonApplication = (TriathlonApplication) getApplication();
        List<TriathlonRecord> records;
        if (getIntent().getBooleanExtra(EXTRA_CHOOSE, false)) {
            records = triathlonApplication.getDataSource().
                    getChoosePeriodRecords(getIntent().getLongExtra(EXTRA_FROM_DATE, 0),
                            getIntent().getLongExtra(EXTRA_TO_DATE, 10000), getIntent().getIntExtra(EXTRA_TYPE, -1));
        } else {
            records = triathlonApplication.getDataSource().getAllRecords(
                    (DataSource.Period) getIntent().getSerializableExtra(EXTRA_PERIOD), getIntent().getIntExtra(EXTRA_TYPE, -1));
        }
        return records;
    }

    protected void showToast(String s) {
        Context context = getApplicationContext();
        int durationOfToast = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, s, durationOfToast);
        toast.show();
    }

}