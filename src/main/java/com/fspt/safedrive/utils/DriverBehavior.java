package com.fspt.safedrive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.fspt.safedrive.service.AuthService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Create by Spring on 2021/4/13 15:50
 */
public class DriverBehavior {
    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String accessToken = "";
    private static final String BASE_URL = "https://aip.baidubce.com/rest/2.0/image-classify/v1/driver_behavior";
    private static DriverBehavior mInstance;

    private DriverBehavior() {
        accessToken = AuthService.getAuth();
        Log.d("HHHH", "DriverBehavior ====== " + accessToken);
    }

    public static DriverBehavior getInstance() {
        if (mInstance == null) {//第一层检查，检查是否有引用指向对象，高并发情况下会有多个线程同时进入
            synchronized (DriverBehavior.class) {//第一层锁，保证只有一个线程进入
                //双重检查，防止多个线程同时进入第一层检查(因单例模式只允许存在一个对象，故在创建对象之前无引用指向对象，所有线程均可进入第一层检查)
                //当某一线程获得锁创建一个Singleton对象时,即已有引用指向对象，singleton不为空，从而保证只会创建一个对象
                //假设没有第二层检查，那么第一个线程创建完对象释放锁后，后面进入对象也会创建对象，会产生多个对象
                if (mInstance == null) {//第二层检查
                    //volatile关键字作用为禁止指令重排，保证返回Singleton对象一定在创建对象后
                    mInstance = new DriverBehavior();
                    //singleton=new Singleton语句为非原子性，实际上会执行以下内容：
                    //(1)在堆上开辟空间；(2)属性初始化;(3)引用指向对象
                    //假设以上三个内容为三条单独指令，因指令重排可能会导致执行顺序为1->3->2(正常为1->2->3),当单例模式中存在普通变量需要在构造方法中进行初始化操作时，单线程情况下，顺序重排没有影响；但在多线程情况下，假如线程1执行singleton=new Singleton()语句时先1再3，由于系统调度线程2的原因没来得及执行步骤2，但此时已有引用指向对象也就是singleton!=null，故线程2在第一次检查时不满足条件直接返回singleton，此时singleton为null(即str值为null)
                    //volatile关键字可保证singleton=new Singleton()语句执行顺序为123，因其为非原子性依旧可能存在系统调度问题(即执行步骤时被打断)，但能确保的是只要singleton!=0，就表明一定执行了属性初始化操作；而若在步骤3之前被打断，此时singleton依旧为null，其他线程可进入第一层检查向下执行创建对象
                }
            }
        }
        return mInstance;

    }

    public String driver_behavior(Context ctx, int resId) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(), resId);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imgData = baos.toByteArray();
            return doHttpPost(imgData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String driver_behavior(Context ctx, Bitmap bm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imgData = baos.toByteArray();
            return doHttpPost(imgData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public String driver_behavior(Context ctx, String filePath) {
        // 请求url
        try {
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            return doHttpPost(imgData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private String doHttpPost(byte[] imgData) {
        try {
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            if(TextUtils.isEmpty(accessToken)){
                accessToken = AuthService.getAuth();
            }
            String result = HttpUtil.post(BASE_URL, accessToken, param);
            return result;
        } catch (Exception e) {

        }
        return "";
    }

}
