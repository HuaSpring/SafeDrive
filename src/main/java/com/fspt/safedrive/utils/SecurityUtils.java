package com.fspt.safedrive.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SecurityUtils {

    public static boolean createFile(String strPath, String strFile,
                                     Bitmap bitmap) {
        //创建文件夹
        File path = new File(strPath);
        if (!path.exists()) {//文件路径不存在
            try {
                if (!path.mkdirs()) {//子目录没有创建文件夹
                    Log.e("SecurityUtils", "App can't create path " + strPath);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File file = new File(strPath + strFile);
        try {
            //用于将字节数据写出到文件
            FileOutputStream fos = new FileOutputStream(file);
            if (bitmap != null) {
                //压缩存储图片
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
            //关闭文件输出流
            fos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!file.createNewFile()) {
                Log.e("SecurityUtils", "App can't create file " + strPath + strFile);
                // return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * 是不是危险驾驶行为
     *
     * @param eyeCloseScore  eg   55  %
     * @param cellPhoneScore
     * @param smokeScore
     * @param yawningScore
     * @return
     */
    public static boolean isDangerDriverBehavior(String eyeCloseScore, String cellPhoneScore,
                                                 String smokeScore, String yawningScore) {
        float eyeCloseScoreF = Float.parseFloat(eyeCloseScore);
        float cellPhoneScoreF = Float.parseFloat(cellPhoneScore);
        float smokeScoreF = Float.parseFloat(smokeScore);
        float yawningScoreF = Float.parseFloat(yawningScore);
        return eyeCloseScoreF > 35 || cellPhoneScoreF > 50 || smokeScoreF > 45 || yawningScoreF > 30;
    }
}
