package mojap108.mojap;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LineChartActivity extends Activity implements OnChartValueSelectedListener {

    protected LineChart mChart;
    private TextView tvX, tvY;
    private ArrayList<String> mXvaluesDay = new ArrayList<String>();
    private ArrayList<Integer> mBeadCountsDay = new ArrayList<Integer>();

    private Typeface mTf;
    private long offsetHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);

       // tvX = (TextView) findViewById(R.id.tvXMax);
     //   tvY = (TextView) findViewById(R.id.tvYMax);

        setUpButtonView();
        calculateTimeZonOffset();
        ImageView backButton = (ImageView) findViewById(R.id.headerbackbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

      //  mChart.setDrawBarShadow(false);
      //  mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");
        mChart.setDrawBorders(false);
        mChart.setDrawGridBackground(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);


        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

     //   mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setAxisLineWidth(3);
      //  xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);

        xAxis.setDrawLimitLinesBehindData(false);
        xAxis.setSpaceBetweenLabels(2);

       // YAxisValueFormatter custom = new MyYAxisValueFormatter();

       // YAxis leftAxis = mChart.getAxisLeft();
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinValue(0);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setAxisLineWidth(3);
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
       // leftAxis.addLimitLine(ll1);
       // leftAxis.addLimitLine(ll2);
       // leftAxis.setAxisMaxValue(200f);
        //leftAxis.setAxisMinValue(-50f);
        //leftAxis.setYOffset(20f);
      //  leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawGridLines(false);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
       /* YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
      //  rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8, false);
       // rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)*/

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        requestData("day");
       // setData(7, 50);

    }

    private void calculateTimeZonOffset() {
        Calendar cal1 = Calendar.getInstance();
        TimeZone tz = cal1.getTimeZone();
        long msFromEpochGmt = cal1.getTimeInMillis();
        offsetHours = tz.getOffset(msFromEpochGmt);

      /*  Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        offsetHours =  TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS);*/
    }

    private void setUpButtonView() {
        final Button dayButton = (Button)findViewById(R.id.dayButton);
        final Button weekButton = (Button)findViewById(R.id.weekButton);
        final Button monthButton = (Button)findViewById(R.id.monthButton);
        int blueColor = ContextCompat.getColor(LineChartActivity.this, android.R.color.holo_blue_light);
        dayButton.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_day_selected));

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int blueColor = ContextCompat.getColor(LineChartActivity.this, android.R.color.holo_blue_light);
                view.setBackgroundColor(blueColor);
                view.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_day_selected));
                requestData("day");
                weekButton.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_week_normal));
                monthButton.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_month_normal));
            }
        });

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int blueColor = ContextCompat.getColor(LineChartActivity.this, android.R.color.holo_blue_light);
                requestData("week");
                view.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_week_selected));
                dayButton.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_day_normal));
                monthButton.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_month_normal));
            }
        });

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int blueColor = ContextCompat.getColor(LineChartActivity.this, android.R.color.holo_blue_light);
                view.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_month_selected));
                requestData("month");
                dayButton.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_day_normal));
                weekButton.setBackground(LineChartActivity.this.getDrawable(R.drawable.button_week_normal));
            }
        });
    }

    private void requestData(final String timePeriod) {
        final DigitsData mData = AppData.getInstance(getApplicationContext()).getDigitsData();
        String url = Constants.BASE_URL + Constants.GET_ACTIVITY+"/"+mData.getAuthId()+"/"+timePeriod+"?offset="+(offsetHours/(3600*1000));
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CharData", "JSON response="+response.toString());
                        parseActivityData(response, timePeriod);
                        Log.d("MojapDataUpload", "activity successfully uploaded");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MojapDataUpload", "activity upload error");
                    }
                });

        mQueue.add(jsObjRequest);
    }

    private void parseActivityData(JSONObject response, String timePeriod) {
        Log.d("ChartActivity","the parseactivitydata, timeperiod="+timePeriod);
        mXvaluesDay.clear();
        mBeadCountsDay.clear();
        int max = 0;
        JSONArray dataArray = response.optJSONArray("value");
        try {
        for(int i =0;i< dataArray.length();i++) {
            JSONObject activityObject = dataArray.getJSONObject(i);
            String pValue = activityObject.optString("dateTime");
            int beadCount = activityObject.optInt("beedCount");
            if (pValue != null) {
                mXvaluesDay.add(getFormattedXAxisValue(pValue, timePeriod));
                mBeadCountsDay.add(new Integer(beadCount));
                max = Math.max(beadCount, max);
            }
        }
            skipLabels(timePeriod);
        updateChartWithData(mXvaluesDay, mBeadCountsDay, max);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void skipLabels(String timePeriod) {
        if(timePeriod.equals("day")) {
           mChart.getXAxis().setLabelsToSkip(2);
        }else if(timePeriod.equals("week")) {
            mChart.getXAxis().setLabelsToSkip(0);
        }else if(timePeriod.equals("month")) {
            mChart.getXAxis().setLabelsToSkip(5);
        }
    }

    private String getFormattedXAxisValue(String pValue, String timePeriod) {
        if(timePeriod.equals("day")) {
            return getHoursString(pValue);
        }else if(timePeriod.equals("week")) {
            return getWeekDay(pValue);
        }else if(timePeriod.equals("month")) {
            return getMonthDay(pValue);
        }
        return "";
    }

    private String getMonthDay(String value) {
      //  value = value.substring(0, value.lastIndexOf("."));
        Calendar cal1  = getCalendar(value);
        int dayOfMonth = cal1.get(Calendar.DAY_OF_MONTH);
        int month = cal1.get(Calendar.MONTH);
        return String.valueOf(month+1)+"/"+String.valueOf(dayOfMonth+1);
    }

    public Calendar getCalendar(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = formatter.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.setTimeInMillis(cal.getTimeInMillis() /*+ offsetHours*/);
            return cal;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    private String getWeekDay(String value) {
        Calendar cal1  = getCalendar(value);
        int dayOfMonth = cal1.get(Calendar.DAY_OF_MONTH);
        int month = cal1.get(Calendar.MONTH);
        return String.valueOf(month+1)+"/"+String.valueOf(dayOfMonth+1);
    }

    private String getHoursString(String val) {
     //   val = val.substring(0, val.lastIndexOf("."));
        Calendar cal1  = getCalendar(val);
        int hour = cal1.get(Calendar.HOUR_OF_DAY);
        int temp = hour % 12;
        if(hour / 12 > 0) {
            return String.valueOf(temp) + " pm";
        }else {
            return String.valueOf(temp) + " am";
        }
    }

    private void updateChartWithData(ArrayList<String> mXvaluesDay, ArrayList<Integer> mBeadCountsDay, int range) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < mBeadCountsDay.size(); i++) {
            float val = mBeadCountsDay.get(i).intValue();
            yVals1.add(new Entry(val, i));
        }
        LineDataSet set1 = new LineDataSet(yVals1, "Bead Count");

//        BarDataSet set1 = new BarDataSet(yVals1, "BeadCount");
      //  set1.setBarSpacePercent(35f);

        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.BLACK);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);


        LineData data = new LineData(mXvaluesDay, dataSets);


        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return String.valueOf((int)v);
            }
        });
        data.setValueTextSize(10f);
        //  data.setValueTypeface(mTf);

        mChart.clear();

        mChart.setData(data);

    }


    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(String.valueOf(i));
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new Entry(val, i));
        }

        LineDataSet set1 = new LineDataSet(yVals1, "Bead Count");
      //  set1.setBarSpacePercent(35f);
        set1.setColor(Color.BLACK);
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);



        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);
        data.setValueTextSize(10f);

      //  data.setValueTypeface(mTf);

        mChart.setData(data);
    }


    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }
}
