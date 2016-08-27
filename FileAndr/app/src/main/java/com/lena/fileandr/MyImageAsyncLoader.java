package com.lena.fileandr;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MyImageAsyncLoader<Bitmap> extends AsyncTaskLoader<Bitmap> {
    private Bitmap image;

    public MyImageAsyncLoader(Context context) {
        super(context);
    }

    @Override
    public Bitmap loadInBackground() {
        image = null;
        URL url = null;
        URLConnection connection = null;
        Context appContext = getContext();

        try {
            url = new URL(appContext.getResources().getString(R.string.image_url));

        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

        if (url != null) {
            try {
                connection = url.openConnection();
            } catch (IOException e) {
//                Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            if (connection != null) {
                try {
                    image = (Bitmap) BitmapFactory.decodeStream(connection.getInputStream());
                } catch (IOException e) {
//                    Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }

//        Toast.makeText(appContext, "something was returned idontnow", Toast.LENGTH_LONG).show();
        return image;
    }



    /*
        String filename = "myfile";
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
         */



   /* @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
        else if (hasResult) {
            deliverResult(image);
        }
    }

    @Override
    public void deliverResult(final Bitmap data) {
        image = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            onReleaseResources(image);
            image = null;
            hasResult = false;
        }
    }

    protected void onReleaseResources(Bitmap data) {
        //nothing to do.
    }

    public Bitmap getResult() {
        return image;
    }*/

    /*@Override
    public void deliverResult(Bitmap data) {//
        image = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }


    @Override
    protected void onStartLoading() {//
        if (image != null) {
            deliverResult(image);
        }
        if (takeContentChanged() || image == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {//
        cancelLoad();// Попытаться отменить текущую задачу загрузки, если возможно.
    }

    @Override
    public void onCanceled(Bitmap data) {//
        if (data != null) {
            data = null;
        }
    }

    @Override
    protected void onReset() {//
        super.onReset();

        onStopLoading();// Убедиться в том, что загрузчик остановлен
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }*/
}
