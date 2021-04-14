package com.fspt.safedrive.utils;

import android.graphics.Bitmap;
import android.util.Log;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Create by Spring on 2021/4/13 20:45
 */
public class FFMpegUtils {

    public static Queue<Bitmap> getVideoBitmaps() {
        return videoBitmaps;
    }

    public static Queue<Bitmap> videoBitmaps = new LinkedList<>();

    /**
     * 一次建议20帧,不宜过大
     *
     * @param filePath
     * @param grabSize
     * @throws Exception
     */
    public static void grabberFFmpegImage(String filePath, int grabSize) throws Exception {
        videoBitmaps.clear();

        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
        ff.start();
        for (int i = 0; i < grabSize; i++) {
            Frame frame = ff.grabImage();
            doExecuteFrame(frame, i);
        }
        ff.stop();
    }

    // grab frame from vedio;
    public static Bitmap doExecuteFrame(Frame frame, int index) {
        if (frame == null || frame.image == null) {
            return null;
        }
        AndroidFrameConverter converter = new AndroidFrameConverter();
        Bitmap bm = converter.convert(frame);
        if (bm != null) {
            videoBitmaps.offer(bm);
            Log.d("HHHH", "doExecuteFrame:  " + index + " w " +  bm.getWidth() + " H " + bm.getHeight());
        }
        return bm;
    }
}
