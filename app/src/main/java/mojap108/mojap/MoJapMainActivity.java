package mojap108.mojap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user12 interaction.
 */
public class MoJapMainActivity extends Activity implements OnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gDetector;
    private BeadData beadData;
    private int deviceWidth;
    private int deviceHeight;
    private MoJapDataUpload mojapTimerActivity;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private float x;

    private  Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case Constants.LOGIN_RESPONSE:
                    if(progress != null) {
                        progress.dismiss();
                    }
                    if(msg.obj != null && msg.obj instanceof LoginData) {
                        storeUserData((LoginData)msg.obj);
                    }else {
                        showErrorDialog("Sync failed!");
                    }
                    break;
                default:
            }
        }
    };
    private ImageView historyView;
    private TextView mantraView;
    private TextView mantraView1;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mPlanetTitles = {"basant", "gollapudi"};
    private boolean isLoginSuccessFull = false;
    private boolean isUploadActivityStarted;
    private String mantraText = "ॐ नम: शिवाय्";
    private ProgressDialog progress;

    public void storeUserData(LoginData mData) {
        Log.d("MojapMainActivity", "storeUserData, totalBeads ="+mData.getTotalBeads());
        isLoginSuccessFull = true;
        doDeviceInstallation();
        AppData.getInstance(this).storeUserData(mData.getId());
        beadData.setTotalBeadCount(mData.getTotalBeads());
        mojapTimerActivity = new MoJapDataUpload(this);
        mojapTimerActivity.startActivityUpload();
        isUploadActivityStarted = true;
        refreshViewData();
    }

    private void doDeviceInstallation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new DeviceInstallation(MoJapMainActivity.this, mHandler).doDeviceInstallation();
            }
        }).start();
    }


    private  void showErrorMessageQuit(String reason) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle( "Error" )
                .setMessage(reason)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_jap_main);
        gDetector = new GestureDetector(this, this);
        //  View v = new View(this);
        beadData = BeadData.getInstance(this, new Date());
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        deviceWidth = metrics.widthPixels;
        deviceHeight = metrics.heightPixels;
        historyView = (ImageView)findViewById(R.id.historview1);
        mantraView = (TextView)findViewById(R.id.mantraview);
        mantraView1 = (TextView)findViewById(R.id.mantraview1);
        historyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchhistoryPage();
            }
        });
        ImageView infoView = (ImageView)findViewById(R.id.mojapinfo);
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchInfoDialog();
            }
        });
        RelativeLayout coachMarkLayout = (RelativeLayout)findViewById(R.id.coachmark_layout);
        if(AppData.getInstance(this).isCoachMarkSeen()) {
            coachMarkLayout.setVisibility(View.GONE);
        }else {
            coachMarkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility(View.GONE);
                    AppData.getInstance(MoJapMainActivity.this).setCoachMarkSeen();
                }
            });
        }

        /*Button resetButton = (Button)findViewById(R.id.resetbutton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beadData.resetTodayBeadCount();
                refreshViewData();
            }
        });*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ImageView profileView = (ImageView)findViewById(R.id.profileview);
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        });
        ArrayList<ProfileDrawerListItem> items = getProfileDrawerItems();
        mDrawerList.setAdapter(new ProfileListAdapter(this, items));
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1) {
                    beadData.resetTodayBeadCount();
                    new ResetRequest(MoJapMainActivity.this, mHandler).sendResetRequest();
                    refreshViewData();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }else if(i == 2) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                    showSetMantraDialog();
                }else if(i == 3) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                    showFeedBackDialog();
                }
            }
        });


        refreshViewData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        doLogin();

    }

    private void showFeedBackDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.feedback, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        final AlertDialog b = dialogBuilder.create();
        //dialogBuilder.setTitle("Set Mantra");
        // dialogBuilder.setMessage("Enter text below");
        final TextView thankYouText = (TextView)dialogView.findViewById(R.id.thankyoutext);

        final Button dismissButton = (Button)dialogView.findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        Button setButton = (Button)dialogView.findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.findViewById(R.id.dialogbuttonlayout).setVisibility(View.GONE);
                //dialogView.setVisibility(View.GONE);
                edt.setVisibility(View.GONE);
                new FeedbackRequest(MoJapMainActivity.this, mHandler).sendFeedback(edt.getText().toString());
                thankYouText.setVisibility(View.VISIBLE);
                dismissButton.setVisibility(View.VISIBLE);

            }
        });
        Button cancelButton = (Button)dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
        /*dialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mantraText = edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });*/

        b.show();

    }

    private ArrayList<ProfileDrawerListItem> getProfileDrawerItems() {
        ArrayList<ProfileDrawerListItem> items = new ArrayList<ProfileDrawerListItem>();
        items.add(new ProfileDrawerListItem(AppData.getInstance(this).getUserData().getPhoneNo(), R.drawable.profile_icon, Constants.PROFILE_HEADER_ITEM));
        items.add(new ProfileDrawerListItem("Reset Bead Count", R.drawable.reset_icon, Constants.PROFILE_ROW_ITEM));
        items.add(new ProfileDrawerListItem("Set Mantra", R.drawable.reset_icon, Constants.PROFILE_ROW_ITEM));
        items.add(new ProfileDrawerListItem("App Feedback", R.drawable.reset_icon, Constants.PROFILE_ROW_ITEM));
        return items;
    }

    private void launchInfoDialog() {
        /*final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.infoview);
        dialog.setTitle("Jap Info");
        WebView webView = (WebView) dialog.findViewById(R.id.webView1);
        webView.loadData(String.format(Constants.htmlText, Constants.InfoText), "text/html", "utf-8");
        Button dialogButton = (Button)dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();*/

        Intent intent = new Intent(this, MojapInfo.class);
        startActivity(intent);

       /* AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(Constants.InfoText)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                    }
                }).show();*/

    }

    private void launchhistoryPage() {
        Intent intent = new Intent(this, LineChartActivity.class);
        startActivity(intent);
    }

    public void doLogin() {
        new Login(this, mHandler).doLogin();
        startProgressDialog();
    }

    private void startProgressDialog() {
        progress = new ProgressDialog(this);
        progress.setMessage("Syncing Your Data...");
        progress.show();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return gDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        // swipe from right to left
        if (motionEvent1.getY() >= deviceHeight / 2 && motionEvent.getY() > deviceHeight / 2 &&
                motionEvent1.getX() - motionEvent.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(v) > SWIPE_THRESHOLD_VELOCITY) {
            Log.d("MoJapActivity", "the onfing method swipe from left to right");
            final View view = MoJapMainActivity.this.findViewById(R.id.malaCount);
            longVibrateOnMalaComplete();

            incrementBeadCount();
            refreshViewData();
            rotateBead();
            /*view.animate()
                    .translationXBy(-1000)
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                           // view.setVisibility(View.INVISIBLE);
                        }
                    });*/
            return true;
        }
        return false;
    }

    private void longVibrateOnMalaComplete() {
        if((beadData.getTodayDisplayBeadCount()+1)%Constants.BEAD_TO_MALA_RATIO == 0) {
            Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vb.vibrate(100);
        }else {
            enableTouchFeeback();
        }
    }

    private void rotateBead() {
        RotateAnimation rotate = new RotateAnimation(x, x + 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(250);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        x=x+90;
        final ImageView bead = (ImageView) findViewById(R.id.beadimageview);
        bead.startAnimation(rotate);
        slideToRight(mantraView);

    }
    public void slideToRight(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, mantraView.getWidth(), 0, 0);
        animate.setDuration(250);
        animate.setFillAfter(true);
        mantraView.startAnimation(animate);

        TranslateAnimation animate1 = new TranslateAnimation(-mantraView1.getWidth(),0, 0, 0);
        animate1.setDuration(250);
        animate1.setFillAfter(true);
        mantraView1.startAnimation(animate1);
    }


    private void enableTouchFeeback() {
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //  vb.vibrate(100);
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.mainlayout);
        rl.setHapticFeedbackEnabled(true);
        if(rl.isHapticFeedbackEnabled()) {
            rl.performHapticFeedback( HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                            | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }
    }

    private void refreshViewData() {
        TextView totalMalaCount = (TextView) MoJapMainActivity.this.findViewById(R.id.totalMalaCount);
        String text1 = MoJapConstants.totalMalaCountString.replace("COUNT", String.valueOf(beadData.getGlobalMalaCount()));
        totalMalaCount.setText(text1);
        TextView todayMalaCount = (TextView) MoJapMainActivity.this.findViewById(R.id.todayMalaCount);
        String text2 = MoJapConstants.todayMalaCountString.replace("COUNT", String.valueOf(beadData.getTodayMalaCount()));
        todayMalaCount.setText(text2);
        TextView mainCounter = (TextView) MoJapMainActivity.this.findViewById(R.id.malaCounttextview);
        mainCounter.setText(String.valueOf(beadData.getTodayDisplayBeadCount()));
        TextView mantraSet1 = (TextView) MoJapMainActivity.this.findViewById(R.id.mantraview);
        mantraSet1.setText(AppData.getInstance(this).getMantraText());
        TextView mantraSet2 = (TextView) MoJapMainActivity.this.findViewById(R.id.mantraview1);
        mantraSet2.setText(AppData.getInstance(this).getMantraText());
        //TextView mainMalaCounter = (TextView) MoJapMainActivity.this.findViewById(R.id.todaymalacounter);
        // String text3 = MoJapConstants.todayMalaCounterString.replace("COUNT", String.valueOf(beadData.getTodayMalaCount()));
        // mainMalaCounter.setText(text3);
    }

    private void incrementBeadCount() {
        beadData.incrementBeadCount();
    }


    public void showSetMantraDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.setmantra, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        final AlertDialog b = dialogBuilder.create();
        //dialogBuilder.setTitle("Set Mantra");
        // dialogBuilder.setMessage("Enter text below");
        Button setButton = (Button)dialogView.findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mantraText = edt.getText().toString();
                AppData.getInstance(MoJapMainActivity.this).setMantraText(mantraText);
                refreshViewData();
                b.dismiss();
            }
        });
        Button cancelButton = (Button)dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
        /*dialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mantraText = edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });*/

        b.show();
    }

    public void showErrorDialog(String errorString) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.loginerror, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();

        TextView errorTextView = (TextView)dialogView.findViewById(R.id.loginerrortextview);
        errorTextView.setText(errorString);

        Button continueOfflineButton = (Button)dialogView.findViewById(R.id.continueofflinebutton);
        continueOfflineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
        Button cancelButton = (Button)dialogView.findViewById(R.id.exit_app_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                MoJapMainActivity.this.finish();
            }
        });
        /*dialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mantraText = edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });*/

        b.show();
    }




    @Override
    public void onStart() {
        super.onStart();
        try {
            if(isLoginSuccessFull && !isUploadActivityStarted) {
                if(mojapTimerActivity != null){
                    mojapTimerActivity.startActivityUpload();
                    isUploadActivityStarted = true;
                }
            }
        }catch (Exception e) {
            Log.d("MojapMainActivity", "timertask for upload activity failed");
        }
        try {
            if(beadData != null){
                beadData.startTime();
            }
        }catch (Exception e) {
            Log.d("MojapMainActivity", "timertask for daily beadcount reset failed");
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MoJapMain Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mojap108.mojap/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        if(mojapTimerActivity != null) {
            isUploadActivityStarted = false;
            mojapTimerActivity.stopActivityUpload();
        }
        if(beadData != null) {
            beadData.stopTimer();
        }
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MoJapMain Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mojap108.mojap/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}