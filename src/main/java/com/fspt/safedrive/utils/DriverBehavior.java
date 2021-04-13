package com.fspt.safedrive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fspt.safedrive.service.AuthService;

import java.io.ByteArrayOutputStream;
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
    public static  final String accessToken ;
    static {
        accessToken = AuthService.getAuth();
    }

    public String driver_behavior(Context ctx, int resId) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/driver_behavior";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(), resId);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imgData = baos.toByteArray();
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String result = HttpUtil.post(url, accessToken, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String driver_behavior(Context ctx, String filePath) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/driver_behavior";
        try {

            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String result = HttpUtil.post(url, accessToken, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
