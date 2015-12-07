package bean;

import android.util.Log;

import com.example.natalya.trapplication.R;
import com.example.natalya.trapplication.TriathlonApplication;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TriathlonRecord {
    public static final int TYPE_SWIMMING_EXERCISE = 2;
    public static final int TYPE_FREESTYLE = 3;
    public static final int TYPE_BUTTERFLY = 4;
    public static final int TYPE_BREASTSTROKE = 5;
    public static final int TYPE_BACKSTROKE = 6;
    public static final int TYPE_RUNNING_EXERCISE = 8;
    public static final int TYPE_RUNNING = 9;
    public static final int TYPE_BICYCLE = 11;
    public static final int TYPE_TRAINER = 12;

    public static final int SWIMMING = 1;
    public static final int RUNNING = 7;
    public static final int CYCLING = 10;

    public static final int TRIATHLON = 0;

    private Date date;
    private int id;
    private int type;
    private double duration;
    private int distance;

    public static final int HOUR = 3600;
    public static final int MIN = 60;
    public static final int KM = 1000;


    public TriathlonRecord(int type, double duration, int distance, Date date) {
        this.type = type;
        this.duration = duration;
        this.distance = distance;
        this.date = date;
    }

    public TriathlonRecord() {
    }

    String convertDate(Date date) {
        return date.toString();
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        String name = getName();
        DecimalFormat decimalFormat=new DecimalFormat("#");


        if (getType() != TriathlonRecord.TYPE_TRAINER) {

                return name + ":\n " +
                        getStringTime() +
                        getStringDistance() +
                        dateFormat.format(date);

        } else {
                return name + ":\n " +
                        getStringTime() +
                        dateFormat.format(date);

        }
    }


    private String getName() {
        int name = R.string.triathlon;
        switch (type) {
            case TYPE_SWIMMING_EXERCISE:
                name = R.string.sw_exercises;
                break;
            case TYPE_FREESTYLE:
                name = R.string.freestyle;
                break;
            case TYPE_BUTTERFLY:
                name = R.string.butterfly;
                break;
            case TYPE_BREASTSTROKE:
                name = R.string.breaststroke;
                break;
            case TYPE_BACKSTROKE:
                name = R.string.backstroke;
                break;
            case TYPE_RUNNING_EXERCISE:
                name = R.string.run_exercises;
                break;
            case TYPE_RUNNING:
                name = R.string.running;
                break;
            case TYPE_BICYCLE:
                name = R.string.bicycle;
                break;
            case TYPE_TRAINER:
                name = R.string.trainer;
                break;
            case SWIMMING:
                name = R.string.swimming;
                break;
            case RUNNING:
                name = R.string.running;
                break;
            case CYCLING:
                name = R.string.cycling;
                break;
        }
        return TriathlonApplication.getContext().getResources().getString(name);
    }
    public String getStringTime(){

        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        Log.e("duration", String.valueOf(duration));

        if(duration < MIN) {
            return TriathlonApplication.getContext().getResources().getString(R.string.duration) + ' ' + String.valueOf(duration) + ' ' +
                    TriathlonApplication.getContext().getResources().getString(R.string.seconds) + "\n " ;
        } else if (duration < HOUR) {
            return  TriathlonApplication.getContext().getResources().getString(R.string.duration) + ' ' + String.valueOf(decimalFormat.format(Math.floor(duration/MIN))) +
                    TriathlonApplication.getContext().getResources().getString(R.string.min) + ' ' +
                    String.valueOf(duration-(Math.floor(duration/MIN)*MIN)) +
                    TriathlonApplication.getContext().getResources().getString(R.string.seconds) + "\n ";
        } else {
            return TriathlonApplication.getContext().getResources().getString(R.string.duration) + ' ' + String.valueOf(decimalFormat.format(Math.floor(duration / HOUR))) +
                    TriathlonApplication.getContext().getResources().getString(R.string.hours) + ' ' +
                    String.valueOf(decimalFormat.format(Math.floor((duration - (Math.floor(duration / HOUR) * HOUR)) / MIN))) +
                    TriathlonApplication.getContext().getResources().getString(R.string.min) + ' ' +
                    String.valueOf(duration - (Math.floor(duration / HOUR))*HOUR -
                            (Math.floor((duration - (Math.floor(duration / HOUR) * HOUR)) / MIN))*MIN) +
                    TriathlonApplication.getContext().getResources().getString(R.string.seconds) + "\n ";
        }
    }
    public String getStringDistance(){
        DecimalFormat decimalFormat = new DecimalFormat("#");
       if(distance < KM){
           return  TriathlonApplication.getContext().getResources().getString(R.string.distance) + ' ' + String.valueOf(distance) +
                   TriathlonApplication.getContext().getResources().getString(R.string.meters) + "\n";
       } else {
           return  TriathlonApplication.getContext().getResources().getString(R.string.distance) + ' ' +
                   String.valueOf(decimalFormat.format(Math.floor(distance/KM))) + ' ' +
                   TriathlonApplication.getContext().getResources().getString(R.string.kmeters) + ' ' +
                   String.valueOf(distance - (Math.floor(distance/KM))*KM) +
                   TriathlonApplication.getContext().getResources().getString(R.string.meters) + "\n";
       }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


}

