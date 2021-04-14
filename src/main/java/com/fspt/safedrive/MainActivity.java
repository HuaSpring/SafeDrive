package com.fspt.safedrive;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.fspt.safedrive.bean.Person;
import com.fspt.safedrive.utils.DriverBehavior;
import com.fspt.safedrive.utils.FFMpegUtils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvResult;
    private ImageView iv;
    private Button btnVideo;
    private Button btnShowVideo;
    private DriverBehavior driverBehavior;
    private Handler showVideoHandler;
    int[] resids = {R.mipmap.driver1, R.mipmap.driver2, R.mipmap.driver3,
            R.mipmap.driver4, R.mipmap.driver5, R.mipmap.driver_cctv};
    int showIndex = resids.length;
    private String videoPath = "/sdcard/Movies/driver.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iv = findViewById(R.id.ivOrig);
        iv.setOnClickListener(this);
        btnVideo = findViewById(R.id.btnVideo);
        btnVideo.setOnClickListener(this);

        btnShowVideo = findViewById(R.id.btnShowVideo);
        btnShowVideo.setOnClickListener(this);


        tvResult = findViewById(R.id.tvDetectResult);

    }

    @Override
    protected void onStart() {
        super.onStart();
        HandlerThread ht = new HandlerThread("Video Analys", Process.THREAD_PRIORITY_DISPLAY);
        ht.start();
        showVideoHandler = new VideoHandler(ht.getLooper());

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkStoragePermission();
        new Thread(new Runnable() {
            @Override
            public void run() {
                driverBehavior = DriverBehavior.getInstance();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivOrig:
                showIndex++;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (MainActivity.this) {
                            String ret = driverBehavior.
                                    driver_behavior(MainActivity.this, resids[showIndex % resids.length]);

                            ParseJsonResult(ret, true);
                        }
                    }
                }).start();


                break;

            case R.id.btnVideo:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FFMpegUtils.grabberFFmpegImage(videoPath, 80);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;

            case R.id.btnShowVideo:
                showVideoHandler.sendEmptyMessageDelayed(0, 1000);
                break;

            default:
                break;
        }
    }

    public static final String TAG = "HHHH";

    private void ParseJsonResult(String jsonStr, boolean showMipmapPic) {
        Gson gson = new Gson();
        Person p = gson.fromJson(jsonStr, Person.class);

        List<Person.PersonInfoDTO> person_info = p.getPerson_info();
        if (person_info == null || person_info.size() <= 0) return;

        Person.PersonInfoDTO personInfoDTO = person_info.get(0);
        Person.PersonInfoDTO.AttributesDTO attributes = personInfoDTO.getAttributes();
        DecimalFormat df = new DecimalFormat("#.00");
        String eyeCloseScore = df.format(attributes.getEyes_closed().getScore() * 100);
        String cellPhoneScore = df.format(attributes.getCellphone().getScore() * 100);
        String smokeScore = df.format(attributes.getSmoke().getScore() * 100) ;
        String yawningScore = df.format(attributes.getYawning().getScore() * 100) ;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (showMipmapPic) iv.setImageResource(resids[showIndex % resids.length]);

                StringBuilder sb = new StringBuilder();
                sb.append("Eye close Score: ").append(eyeCloseScore).append( " %").append("\r\n")
                        .append("CellPhone Score: ").append(cellPhoneScore).append( " %").append("\r\n")
                        .append("Smoke Score: ").append(smokeScore).append( " %").append("\r\n")
                        .append("yawning Score: ").append(yawningScore).append( " %").append("\r\n");
                tvResult.setTextSize(35);
                tvResult.setText(sb.toString());
//                Log.d(TAG, " ParseJsonResult " + sb.toString());
            }
        });


    }


    public class VideoHandler extends Handler {
        public VideoHandler(Looper looper) {

        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.d(TAG, "videoBitmaps : " + FFMpegUtils.videoBitmaps.size());
                    if (!FFMpegUtils.videoBitmaps.isEmpty()) {
                        Bitmap detectBm = FFMpegUtils.videoBitmaps.poll();
                        iv.setImageBitmap(detectBm);
                        Message message = Message.obtain();
                        message.what = 0;
                        if (FFMpegUtils.videoBitmaps.size() % 3 == 0) {
                            checkAndDetected(detectBm);
                        }
                        sendMessageDelayed(message, 150);
                    }
                    break;
            }
        }
    }

    private void checkAndDetected(Bitmap detectBm) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String driver_behavior = driverBehavior.driver_behavior(MainActivity.this, detectBm);
                    if (!TextUtils.isEmpty(driver_behavior))
                        ParseJsonResult(driver_behavior, false);
                }
            }).start();


    }

    public void checkStoragePermission() {
        if (!hasPermissionsGranted(STORAGE_PERMISSIONS)) {
            requestStoragePermission();
            return;
        }
    }


    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(STORAGE_PERMISSIONS)) {
            new ConfirmationDialog().show(getSupportFragmentManager(), "haha");
        } else {
            requestPermissions(STORAGE_PERMISSIONS, REQUEST_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 0;

    public static class ConfirmationDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            parent.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_EXTERNAL_STORAGE_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Activity activity = parent.getActivity();
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            })
                    .create();
        }
    }

}