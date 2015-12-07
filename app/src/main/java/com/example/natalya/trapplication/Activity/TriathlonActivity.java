/*
package com.example.natalya.trapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.natalya.trapplication.R;

import bean.Triathlon;


*/
/**
 * Activity for choosing type of Triathlon. /n
 * Is used to display types of Triathlon and choosing one of them to work with.
 *
 * @author Nataliia Volovach
 * @version 1.0.0
 *//*


public class TriathlonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.triathlon_layout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_triathlon, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setClickListener(){
        Button buttonSwimming = (Button) findViewById(R.id.button_choose_swimming);
        Button buttonRunning = (Button) findViewById(R.id.button_choose_running);
        Button buttonCycling = (Button) findViewById(R.id.button_choose_cycling);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int triathlonType = -1;
                switch (view.getId()) {
                    case R.id.button_choose_swimming:
                        triathlonType = Triathlon.SWIMMING;
                        break;
                    case R.id.button_choose_running:
                        triathlonType = Triathlon.RUNNING;
                        break;
                    case R.id.button_choose_cycling:
                        triathlonType = Triathlon.CYCLING;
                        break;
                    default:
                        break;
                }
                startTriathlonTypeActivity(triathlonType);
            }
        };
        buttonSwimming.setOnClickListener(onClickListener);
        buttonRunning.setOnClickListener(onClickListener);
        buttonCycling.setOnClickListener(onClickListener);
    }

    private void startTriathlonTypeActivity(int triathlonType){
        Intent intent = new Intent(TriathlonActivity.this, HistoryActivity.class);
        intent.putExtra("type", triathlonType);
        startActivity(intent);
    }

}
*/
