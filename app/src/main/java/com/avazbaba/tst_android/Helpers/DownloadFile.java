package com.avazbaba.tst_android.Helpers;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class DownloadFile extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        String fileUrl = strings[0];
        String fileName = strings[1];
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "tst_android_pdf");
        folder.mkdir();

        File pdfFile = new File(folder, fileName);
//        pdfFile.createNewFile();
//        try {
//            pdfFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        FileDownloader.downloadFile(fileUrl, pdfFile);
        return null;
    }
}

