package com.rnvideocompress.CompressVideo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;

import java.io.IOException;
import java.io.File;
import android.os.Environment;

import java.net.URI;
import java.net.URISyntaxException;

import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.iceteck.silicompressorr.SiliCompressor;

public class CompressVideo extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    Callback compressionCb;

    @Override
    public String getName() {
        /**
         * return the string name of the NativeModule which represents this class in JavaScript
         * In JS access this module through React.NativeModules.OpenSettings
         */
        return "CompressVideo";
    }

    @ReactMethod
    public void compressVideo(String videoURL,Callback cb){
        compressionCb = cb;
        Log.e("Path ::",videoURL);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "com.rnvideocompress" + "/files/");
        Log.e("Output Path ::", Uri.fromFile(f).toString());
        String filePath = null;
        if (f.mkdirs() || f.isDirectory()){
            //compress and output new video specs
//            try {

                new VideoCompressAsyncTask(reactContext).execute(videoURL.toString(), f.getPath());

//                filePath = SiliCompressor.with(reactContext).compressVideo(videoURL.toString(), f.getPath());
//            }
//            catch (URISyntaxException e){
//                e.printStackTrace();
//            }
        }
//        cb.invoke(filePath);
    }

    @ReactMethod
    public void openNetworkSettings(Callback cb) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            cb.invoke(false);
            return;
        }

        try {
            final Intent i = new Intent();
            i.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + currentActivity.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            currentActivity.startActivity(i);
            cb.invoke(true);
        } catch (Exception e) {
            cb.invoke(e.getMessage());
        }
    }

    /* constructor */
    public CompressVideo(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }


    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        ReactApplicationContext mContext;

        public VideoCompressAsyncTask(ReactApplicationContext reactContext) {
            mContext = reactContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Log.e("Success in native ::",filePath);
            compressionCb.invoke(filePath);
            return filePath;
        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            File imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            Log.i("Silicompressor", "Path: " + compressedFilePath);
        }
    }

}

