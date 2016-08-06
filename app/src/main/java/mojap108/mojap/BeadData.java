package mojap108.mojap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gollaba on 6/19/16.
 */
public class BeadData {

    static BeadData instance = null;
    private final Date todayDate;

    private int todayBeadCount =0;

    private int displayTodayBeadCount =0;;

    private int globalBeadCount = 0;

    private Context mContext;

    private int BEAD_TO_MALA_RATIO = 108;

    private String KEY_DAILYBEADCOUNT = "KEY_DAILYBEADCOUNT";
    private String KEY_GLOBALBEADCOUNT = "KEY_GLOBALBEADCOUNT";
    private String KEY_TODAYDATE = "KEY_TODAYDATE";


    public static final String PREFS = "MoJap_preferences" ;
    SharedPreferences sharedPref;
    private Timer timer;
    private TimerTask timerTask;

    private BeadData(Context context, Date date) {
        mContext = context;
        todayDate = date;
        sharedPref = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        initializeBeadData();
    }

    private void initializeBeadData() {
       getSavedBeadData();
        Date savedDate = getSavedTodayDate();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(todayDate);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = 0;
        if(savedDate != null) {
            cal1.setTime(savedDate);
            day2 = cal1.get(Calendar.DAY_OF_YEAR);
        }
        if(todayDate != null && savedDate != null && todayDate.getTime() > savedDate.getTime() && day1 > day2) {
            resetTodayBeadCount();
        }
        updateTodayDate();
    }

    public void resetTodayBeadCount() {
        todayBeadCount = 0;
        displayTodayBeadCount = 0;
        saveUpdatedBeadData();
    }



    private void getSavedBeadData() {
        todayBeadCount = sharedPref.getInt(KEY_DAILYBEADCOUNT,0);
        globalBeadCount= sharedPref.getInt(KEY_GLOBALBEADCOUNT,0);
        displayTodayBeadCount = sharedPref.getInt(KEY_DAILYBEADCOUNT,0);
    }

    private Date getSavedTodayDate() {
        String dateString = sharedPref.getString(KEY_TODAYDATE,"");
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sp.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateTodayDate() {
        SharedPreferences.Editor editor = sharedPref.edit();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        editor.putString(KEY_TODAYDATE, sp.format(todayDate));
        editor.commit();
    }

    private void saveUpdatedBeadData() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_DAILYBEADCOUNT, todayBeadCount);
        editor.putInt(KEY_GLOBALBEADCOUNT, globalBeadCount);
        editor.commit();
    }

    public static BeadData getInstance(Context context, Date date) {
        if(instance == null) {
            instance = new BeadData(context, date);
        }
        return instance;
    }

    public void incrementBeadCount() {
        todayBeadCount++;
        displayTodayBeadCount++;
        globalBeadCount++;
        saveUpdatedBeadData();
    }

    public int getTodayBeadCount() {
        return todayBeadCount;
    }

    public int getTodayDisplayBeadCount() {
        return displayTodayBeadCount;
    }

    public int getGlobalBeadCount(){
        return globalBeadCount;
    }

    public int getGlobalMalaCount() {
        return (int)globalBeadCount/BEAD_TO_MALA_RATIO;
    }

    public int getTodayMalaCount() {
        return (int)todayBeadCount/BEAD_TO_MALA_RATIO;
    }

    public void startTime() {
        Log.d("BeadData", "timer scheduled");
        timer = new Timer();
        timerTask = initTimeTask();
        timer.schedule(timerTask, 60 * 1000, 60 * 1000);
    }

    private TimerTask initTimeTask() {
        return new TimerTask() {
            @Override
            public void run() {
                Log.d("BeadData", "timertask start run");
                checkForDayChangeAndResetBeadCount();
            }
        };
    }

    public void stopTimer() {
        if(timer != null) {
            Log.d("BeadData", "timer cancelled");
            timer.cancel();
            timer = null;
        }
    }

    private void checkForDayChangeAndResetBeadCount() {
        Date savedDate = getSavedTodayDate();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = 0;
        if(savedDate != null) {
            cal1.setTime(savedDate);
            day2 = cal1.get(Calendar.DAY_OF_YEAR);
        }
        Log.d("BeadData", "checking for day change");
        if(todayDate != null && savedDate != null && todayDate.getTime() > savedDate.getTime() && day1 > day2) {
            Log.d("BeadData", "day change detected resetting the bead count");
            resetTodayBeadCount();
        }
        updateTodayDate();
    }

}
