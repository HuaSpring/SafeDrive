package com.fspt.safedrive;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.fspt.safedrive.adapter.DriverPicAdapter;
import com.fspt.safedrive.bean.Person;
import com.fspt.safedrive.event.DangerDriverEvent;
import com.fspt.safedrive.global.Constants;
import com.fspt.safedrive.utils.DriverBehavior;
import com.fspt.safedrive.utils.FileUtil;
import com.fspt.safedrive.utils.SecurityUtils;
import com.fspt.safedrive.utils.TTSUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvResult;
    private ImageView iv;
    private GridView gv;
    private DriverBehavior driverBehavior;
    private Handler showVideoHandler;
    int[] resids = {R.mipmap.driver1, R.mipmap.driver2,
            R.mipmap.driver4, R.mipmap.driver5, R.mipmap.driver6, R.mipmap.driver7};
    int showIndex = resids.length;
    private String videoPath = "/sdcard/Movies/driver.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViewAndDatas();
        initEventBusEvent();
        initTTS();

    }

    private DriverPicAdapter mDriverPicAdapter;
    private GridViewOnClickListener gridViewOnClickListener;

    private void initViewAndDatas() {
        gv = findViewById(R.id.gv);
        mDriverPicAdapter = new DriverPicAdapter(resids);
        gv.setAdapter(mDriverPicAdapter);
        gridViewOnClickListener = new GridViewOnClickListener();
        mDriverPicAdapter.registerOnItemClickListener(gridViewOnClickListener);

        tvResult = findViewById(R.id.tvDetectResult);
    }

    private void initEventBusEvent() {
        EventBus.getDefault().register(this);//EventBus注册
    }

    private TTSUtils ttsUtils;

    private void initTTS() {
        ttsUtils = new TTSUtils(MainActivity.this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onEvent(DangerDriverEvent event) {
        if (event.getType() == 0) {
            // TODO: 可以有更多判断，闭眼，打电话，抽烟，打瞌睡，双手离开方向盘等
            if (Float.parseFloat(event.getEyeCloseScore()) > 35) {
                ttsUtils.speak("Eye Close");
            } else if (Float.parseFloat(event.getCellPhoneScore()) > 50) {
                ttsUtils.speak("CellPhone");
            } else if (Float.parseFloat(event.getSmokeScore()) > 45) {
                ttsUtils.speak("Smoke");
            } else if (Float.parseFloat(event.getYawningScore()) > 30) {
                ttsUtils.speak("Yawning");
            } else {
                ttsUtils.speak("Good Drive");
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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

        initPicVideoStorePath(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initPicVideoStorePath(Context ctx) {
        File dir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Constants.CAMERA_PIC_CACHE_PATH = (dir == null ? "" : (dir.getAbsolutePath() + "/"));
        dir = ctx.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        Constants.CAMERA_VIDEO_CACHE_PATH = (dir == null ? "" : (dir.getAbsolutePath() + "/"));
    }


    public static final String TAG = "HHHH";

    private void ParseJsonResult(String jsonStr, boolean showMipmapPic) {
        ParseJsonResult(jsonStr, showMipmapPic, false);
    }

    private void ParseJsonResult(String jsonStr, boolean showMipmapPic, boolean savePic) {
        ParseJsonResult(jsonStr, showMipmapPic, false, null);
    }

    private void ParseJsonResult(String jsonStr, boolean showMipmapPic, boolean savePic, Bitmap bm) {
        // 4、对结果JSON字符串进行解析
        Gson gson = new Gson();
        Person p = gson.fromJson(jsonStr, Person.class);

        List<Person.PersonInfoDTO> person_info = p.getPerson_info();
        if (person_info == null || person_info.size() <= 0) return;

        Person.PersonInfoDTO personInfoDTO = person_info.get(0);
        Person.PersonInfoDTO.AttributesDTO attributes = personInfoDTO.getAttributes();
        DecimalFormat df = new DecimalFormat("#.00");
        String eyeCloseScore = df.format(attributes.getEyes_closed().getScore() * 100);
        String cellPhoneScore = df.format(attributes.getCellphone().getScore() * 100);
        String smokeScore = df.format(attributes.getSmoke().getScore() * 100);
        String yawningScore = df.format(attributes.getYawning().getScore() * 100);
        // TODO:   可以获取更多属性比如 双手离开方向盘
        String bothHandsLeavingWheelScore = df.format(attributes.getBoth_hands_leaving_wheel().getScore() * 100);
        // TODO: Step1 创建 DangerDriverEvent 事件 DangerDriveEvent event = new DangerDriverEvent();
        DangerDriverEvent dangerDriverEvent = new DangerDriverEvent();//初始化消息类
        dangerDriverEvent.setType(0);
        dangerDriverEvent.setCellPhoneScore(cellPhoneScore);
        dangerDriverEvent.setEyeCloseScore(eyeCloseScore);
        dangerDriverEvent.setSmokeScore(smokeScore);
        dangerDriverEvent.setYawningScore(yawningScore);
        // 添加更多的属性 比如 双手离开方向盘

        // TODO: Step2调用  EventBus.getDerault().post(event) 发送事件
        EventBus.getDefault().post(dangerDriverEvent);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResult.setTextSize(35);
                tvResult.setText(dangerDriverEvent.toString());
            }
        });


    }


    private void checkAndDetected(Bitmap detectBm) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String driver_behavior = driverBehavior.driver_behavior(MainActivity.this, detectBm);
                if (!TextUtils.isEmpty(driver_behavior))
                    ParseJsonResult(driver_behavior, false, true, detectBm);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(STORAGE_PERMISSIONS, REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 0;

    @Override
    public void onClick(View v) {

    }

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


    class GridViewOnClickListener implements DriverPicAdapter.IOnItemClickListener {
        @Override
        public void onItemClick(int pos) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (MainActivity.this) {
                        String ret = driverBehavior.
                                driver_behavior(MainActivity.this, resids[pos]);
                        ParseJsonResult(ret, false);
                    }
                }
            }).start();
        }
    }

}