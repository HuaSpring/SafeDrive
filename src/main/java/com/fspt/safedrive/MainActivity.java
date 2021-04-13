package com.fspt.safedrive;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fspt.safedrive.bean.Person;
import com.fspt.safedrive.utils.DriverBehavior;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvResult;
    private ImageView iv;

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String ret = new DriverBehavior().
                                driver_behavior("/sdcard/Pictures/driver3.jpg");

                        Gson gson = new Gson();
                        Person person = gson.fromJson(ret, Person.class);
                        ParseJsonResult(person);
                    }
                }).start();


                break;
            default:
                break;
        }
    }

    public static final String TAG = "HHHH";
    private void ParseJsonResult(Person p){
        List<Person.PersonInfoDTO> person_info = p.getPerson_info();
        Person.PersonInfoDTO personInfoDTO = person_info.get(0);
        Person.PersonInfoDTO.AttributesDTO attributes = personInfoDTO.getAttributes();

        Double eyeScore = attributes.getEyes_closed().getScore();
        Double cellPhoneScore = attributes.getCellphone().getScore();
        Double smokeScore = attributes.getSmoke().getScore();
        double yawningScore = attributes.getYawning().getScore();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("Eye close Score: ").append(eyeScore).append("\r\n")
                        .append("CellPhone Score: ").append(cellPhoneScore).append("\r\n")
                        .append("Smoke Score: ").append(smokeScore).append("\r\n")
                        .append("yawning Score: ").append(yawningScore).append("\r\n");
                tvResult.setTextSize(35);
                tvResult.setText(sb.toString());
            }
        });


    }


}