package bean;

import android.util.Log;

import com.example.natalya.trapplication.R;
import com.example.natalya.trapplication.TriathlonApplication;

import java.text.DecimalFormat;

/**
 * Created by Nataluysyk on 19.02.2015.
 */
public class SummarisedRecord {

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

    private int MIN = 60;
    private int HOUR = 3600;
    private int KM = 1000;

    private int type;
    private double duration;
    private int distance;
    private double averageSpeed;
    private int amountOfTrainings;

     public SummarisedRecord(
             int type, double duration, int distance, double averageSpeed, int amountOfTrainings) {
        this.type = type;
        this.duration = duration;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.amountOfTrainings = amountOfTrainings;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setAmountOfTrainings(int amountOfTrainings) {
        this.amountOfTrainings = amountOfTrainings;
    }

    public int getAmountOfTrainings() {
        return amountOfTrainings;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }


     @Override
    public String toString() {

         DecimalFormat decimalFormat = new DecimalFormat("#.###");
         if (type != TriathlonRecord.TYPE_TRAINER && type != TriathlonRecord.CYCLING && type != TriathlonRecord.TRIATHLON) {

             return getName() + ":\n " +
                     getStringTime() +
                     getStringDistance() +
                     getAverageSpeedString() + ' ' + String.valueOf(decimalFormat.format(averageSpeed)) + ' ' +
                     TriathlonApplication.getContext().getResources().getString(R.string.m_s) + "\n" +
                     getAmountOfTrainingsString() + ' ' + String.valueOf(amountOfTrainings);
         }
         if(type == TriathlonRecord.CYCLING || type == TriathlonRecord.TRIATHLON) {
                 return getName() + ":\n " +
                         getStringTime() +
                         getStringDistance() +
                         getAmountOfTrainingsString() + ' ' + String.valueOf(amountOfTrainings);
         }else {
                 return getName() + ":\n " +
                         getStringTime() +
                         getAmountOfTrainingsString() + ' ' + String.valueOf(amountOfTrainings);
         }

    }

    private String getName(){
        int name = R.string.triathlon;
        switch (type){
            case TYPE_SWIMMING_EXERCISE :
                name = R.string.sw_exercises;
                break;
            case TYPE_FREESTYLE :
                name = R.string.freestyle;
                break;
            case TYPE_BUTTERFLY :
                name = R.string.butterfly;
                break;
            case TYPE_BREASTSTROKE :
                name = R.string.breaststroke;
                break;
            case TYPE_BACKSTROKE :
                name = R.string.backstroke;
                break;
            case TYPE_RUNNING_EXERCISE :
                name = R.string.run_exercises;
                break;
            case TYPE_RUNNING :
                name = R.string.running;
                break;
            case TYPE_BICYCLE :
                name = R.string.bicycle;
                break;
            case TYPE_TRAINER :
                name = R.string.trainer;
                break;
            case SWIMMING :
                name = R.string.swimming;
                break;
            case RUNNING :
                name = R.string.running;
                break;
            case CYCLING :
                name = R.string.cycling;
                break;
            case TRIATHLON:
                name = R.string.triathlon;
                break;
        }
        return TriathlonApplication.getContext().getResources().getString(name);
    }


    private String getAmountOfTrainingsString(){
        int s = R.string.am_of_trainings;
        return TriathlonApplication.getContext().getResources().getString(s);
    }

    private String getAverageSpeedString(){
        int s = R.string.av_speed;
        return TriathlonApplication.getContext().getResources().getString(s);
    }
    public static int[] splitToComponentTimes(double biggy)
    {
        double longVal = biggy;
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }
    public String getStringTime(){

        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        Log.e("duration", String.valueOf(duration));

        if(duration < MIN) {
            return TriathlonApplication.getContext().getResources().getString(R.string.tot_duration)+ ' ' + String.valueOf(duration) + ' ' +
                    TriathlonApplication.getContext().getResources().getString(R.string.seconds) + "\n " ;
        } else if (duration < HOUR) {
            return  TriathlonApplication.getContext().getResources().getString(R.string.tot_duration) + ' ' + String.valueOf(decimalFormat.format(Math.floor(duration/MIN))) +
                    TriathlonApplication.getContext().getResources().getString(R.string.min) + ' ' +
                    String.valueOf(duration-(Math.floor(duration/MIN)*MIN)) +
                    TriathlonApplication.getContext().getResources().getString(R.string.seconds) + "\n ";
        } else {
            return TriathlonApplication.getContext().getResources().getString(R.string.tot_duration) + ' ' + String.valueOf(decimalFormat.format(Math.floor(duration / HOUR))) +
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
            return  TriathlonApplication.getContext().getResources().getString(R.string.tot_distance) + ' ' + String.valueOf(distance) +
                    TriathlonApplication.getContext().getResources().getString(R.string.meters) + "\n";
        } else {
            return  TriathlonApplication.getContext().getResources().getString(R.string.tot_distance) + ' ' +
                    String.valueOf(decimalFormat.format(Math.floor(distance/KM))) + ' ' +
                    TriathlonApplication.getContext().getResources().getString(R.string.kmeters) + ' ' +
                    String.valueOf(distance - (Math.floor(distance/KM))*KM) +
                    TriathlonApplication.getContext().getResources().getString(R.string.meters) + "\n";
        }
    }
}

