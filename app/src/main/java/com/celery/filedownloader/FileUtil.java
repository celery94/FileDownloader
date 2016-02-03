package com.celery.filedownloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class FileUtil {

    public static void openFile(Context context, String urlStr) {
        Log.d("FileUtil", "openFile urlStr:" + urlStr);

        // Create URI
        File file = new File(urlStr);
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (urlStr.contains(".apk")) {
            // PDF file
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else if (urlStr.contains(".doc") || urlStr.contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (urlStr.contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (urlStr.contains(".ppt") || urlStr.contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (urlStr.contains(".xls") || urlStr.contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (urlStr.contains(".zip") || urlStr.contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (urlStr.contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (urlStr.contains(".wav") || urlStr.contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (urlStr.contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (urlStr.contains(".jpg") || urlStr.contains(".jpeg") || urlStr.contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (urlStr.contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (urlStr.contains(".3gp") || urlStr.contains(".mpg") || urlStr.contains(".mpeg") || urlStr.contains(".mpe") || urlStr.contains(".mp4") || urlStr.contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
