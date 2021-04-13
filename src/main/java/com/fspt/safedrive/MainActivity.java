package com.fspt.safedrive;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fspt.safedrive.bean.Person;
import com.fspt.safedrive.utils.DriverBehavior;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvResult;
    private ImageView iv;
    int[] resids = {R.mipmap.driver1, R.mipmap.driver2, R.mipmap.driver3, R.mipmap.driver4, R.mipmap.driver5};
    int showIndex = resids.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iv = findViewById(R.id.ivOrig);
        iv.setOnClickListener(this);
        tvResult = findViewById(R.id.tvDetectResult);

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
                            String ret = new DriverBehavior().
                                    driver_behavior(MainActivity.this, resids[showIndex % resids.length]);
                            Gson gson = new Gson();
                            Person person = gson.fromJson(ret, Person.class);
                            ParseJsonResult(person);
                        }
                    }
                }).start();


                break;
            default:
                break;
        }
    }

    public static final String TAG = "HHHH";

    private void ParseJsonResult(Person p) {
        List<Person.PersonInfoDTO> person_info = p.getPerson_info();
        if (person_info == null || person_info.size() <= 0) return;

        Person.PersonInfoDTO personInfoDTO = person_info.get(0);
        Person.PersonInfoDTO.AttributesDTO attributes = personInfoDTO.getAttributes();
        DecimalFormat df = new DecimalFormat("#.00");
        String eyeCloseScore = df.format(attributes.getEyes_closed().getScore() * 100) + " %";
        String cellPhoneScore = df.format(attributes.getCellphone().getScore() * 100) + " %";
        String smokeScore = df.format(attributes.getSmoke().getScore() * 100) + " %";
        String yawningScore = df.format(attributes.getYawning().getScore() * 100) + " %";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv.setImageResource(resids[showIndex % resids.length]);

                StringBuilder sb = new StringBuilder();
                sb.append("Eye close Score: ").append(eyeCloseScore).append("\r\n")
                        .append("CellPhone Score: ").append(cellPhoneScore).append("\r\n")
                        .append("Smoke Score: ").append(smokeScore).append("\r\n")
                        .append("yawning Score: ").append(yawningScore).append("\r\n");
                tvResult.setTextSize(35);
                tvResult.setText(sb.toString());
            }
        });


    }


}