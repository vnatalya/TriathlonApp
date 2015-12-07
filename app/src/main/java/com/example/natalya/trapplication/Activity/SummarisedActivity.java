package com.example.natalya.trapplication.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natalya.trapplication.R;
import com.example.natalya.trapplication.TriathlonApplication;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import bean.SummarisedRecord;
import bean.TriathlonRecord;
import db.DataSource;


/**
 /**
 * Activity to show results of trainings.
 * Activity that shows summarized data of trainings of certain type of Triathlon and of certain period of time.
 */
public class SummarisedActivity extends Activity {

    private DataSource.Period period = DataSource.Period.ALL;
    private int type = TriathlonRecord.TRIATHLON;

    private GraphicalView mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
    }


    @Override
    protected void onResume() {
        super.onResume();

        List<TriathlonRecord> records = getRecords();

        ((TextView) findViewById(R.id.textView_summarized)).setText(getSummarisedRecord(records).toString());

        drawChart(records, (getIntent().getIntExtra(ListActivity.EXTRA_TYPE, -1)));

        //setOnClick();
    }


    private SummarisedRecord getSummarisedRecord(List<TriathlonRecord> records) {
        double duration = 0;
        for (int i = 0; i < records.size(); ++i) {
            duration += records.get(i).getDuration();
        }

        int distance = 0;
        for (int i = 0; i < records.size(); ++i) {
            distance += records.get(i).getDistance();
        }

        int amOfTr = records.size();

        double avSpeed;

        double bicycleDuration = duration;
        for (int i = 0; i < records.size(); ++i) {
            if (records.get(i).getType() == TriathlonRecord.TYPE_TRAINER) {
                bicycleDuration -= records.get(i).getDuration();
            }
        }
        avSpeed = distance / bicycleDuration;
        return new SummarisedRecord(getIntent().getIntExtra(ListActivity.EXTRA_TYPE, -1), duration, distance, avSpeed, amOfTr);
    }

    private void drawChart(List<TriathlonRecord> records, int type) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.sum_tab);

        XYSeries durationSeries = new XYSeries(TriathlonApplication.getContext().getResources().
                getString(R.string.duration_gr));

        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setXTitle(TriathlonApplication.getContext().getResources().getString(R.string.trainings));
        multiRenderer.setYTitle(TriathlonApplication.getContext().getResources().getString(R.string.results));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

        XYSeriesRenderer durationRenderer = new XYSeriesRenderer();
        durationRenderer.setColor(Color.RED);
        durationRenderer.setPointStyle(PointStyle.CIRCLE);
        durationRenderer.setFillPoints(true);
        durationRenderer.setLineWidth(4);

        int[] m = {50, 50, 55, 51};
        multiRenderer.setLegendTextSize(18);
        multiRenderer.setPointSize(5);
        multiRenderer.setLabelsTextSize(15);
        multiRenderer.setAxisTitleTextSize(20);
        multiRenderer.setChartTitleTextSize(30);
        multiRenderer.setXLabelsColor(Color.CYAN);
        multiRenderer.setLabelsColor(Color.CYAN);
        multiRenderer.setYLabelsColor(0, Color.CYAN);
        multiRenderer.setMargins(m);
        multiRenderer.setXLabels(0);


        SummarisedRecord summarised = getSummarisedRecord(records);
        for (int i = 0; i < summarised.getAmountOfTrainings(); i++) {
            durationSeries.add(i, records.get(i).getDuration());
        }
        dataSet.addSeries(durationSeries);

        for (int i = 0; i < summarised.getAmountOfTrainings(); i++) {
            multiRenderer.addXTextLabel(i , dateFormat.format(records.get(i).getDate()));
        }
        multiRenderer.addSeriesRenderer(durationRenderer);

        if (type != TriathlonRecord.TYPE_TRAINER) {
            XYSeries distanceSeries = new XYSeries(TriathlonApplication.getContext().getResources().
                    getString(R.string.distance_gr));

            for (int i = 0; i < summarised.getAmountOfTrainings(); i++) {
                distanceSeries.add(i, records.get(i).getDistance());
            }

            dataSet.addSeries(distanceSeries);


            XYSeriesRenderer distanceRenderer = new XYSeriesRenderer();
            distanceRenderer.setColor(Color.GREEN);
            distanceRenderer.setPointStyle(PointStyle.CIRCLE);
            distanceRenderer.setFillPoints(true);
            distanceRenderer.setLineWidth(4);

            multiRenderer.setChartTitle(TriathlonApplication.getContext().getResources().getString(R.string.dist_and_dur));

            multiRenderer.addSeriesRenderer(distanceRenderer);

        } else {
            multiRenderer.setChartTitle(TriathlonApplication.getContext().getResources().getString(R.string.duration_gr));
        }

        mChart = ChartFactory.getCubeLineChartView(this, dataSet, multiRenderer, 0);
        layout.addView(mChart);
    }


   /* public void setOnClick() {
        Button saveButton = (Button) findViewById(R.id.buttonSaveImage);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            *//**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             *//*
            @Override
            public void onClick(View v) {
                saveGraphic();

            }
        };
        saveButton.setOnClickListener(onClickListener);
    }*/


    /**
     * Method to save the graphic.
     * Method that saves the image of the graphic in created folder.
     */
    private void saveGraphic() {
        Bitmap bitmap;
        mChart.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(mChart.getDrawingCache());
        mChart.setDrawingCacheEnabled(false);

        File triathlonPicturesDirectory = new File( getExternalCacheDir(),  getString(R.string.triathlon));


        File outputFile = new File(triathlonPicturesDirectory, String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".png");

        FileOutputStream out = null;
        try{
            out = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    showToast(getString(R.string.saved_im));
                } else {
                    showToast(getString(R.string.wrong_card));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected List<TriathlonRecord> getRecords() {
        TriathlonApplication triathlonApplication = (TriathlonApplication) getApplication();
        List<TriathlonRecord> records;
        if (getIntent().getBooleanExtra(ListActivity.EXTRA_CHOOSE, false)) {
            records = triathlonApplication.getDataSource().
                    getChoosePeriodRecords(getIntent().getLongExtra(ListActivity.EXTRA_FROM_DATE, 0),
                            getIntent().getLongExtra(ListActivity.EXTRA_TO_DATE, 10000),
                            type);
        } else {
            period = (DataSource.Period) getIntent().getSerializableExtra(ListActivity.EXTRA_PERIOD);
            type = getIntent().getIntExtra(ListActivity.EXTRA_TYPE, -1);
            records = triathlonApplication.getDataSource().getAllRecords(
                    period, type);

        }
        return records;
    }

    private void showToast(String s) {
        Context context = getApplicationContext();
        int durationOfToast = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, s, durationOfToast);
        toast.show();
    }


}