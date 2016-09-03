package com.lena.fileandr;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MyImageAsyncLoader<Bitmap> extends AsyncTaskLoader<Bitmap> {
    private Bitmap image;
    private static final String TAGG = "Zooo";
    static WeakReference<MainActivity> mActivity;
    private int progress;
    private boolean finished;

    public MyImageAsyncLoader(Context context) {
        super(context);
        onContentChanged();
        Log.d(TAGG, "loader: crate loaderr");
    }

   /* public MyImageAsyncLoader(MainActivity activity) {
        super(activity);
        mActivity = new WeakReference<MainActivity>(activity);
    }*/


    @Override
    public Bitmap loadInBackground() {
        image = null;
        URL url = null;
        URLConnection connection = null;
        Context appContext = getContext();
        Log.d(TAGG, "loader: inside load in background");

        try {
            url = new URL(appContext.getResources().getString(R.string.image_url));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url != null) {
            try {
                connection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                reset();//stopLoading();cancelLoad();
                Log.d(TAGG, "loader: stop load");
            }

            if (connection != null) {
                try {
                    image = (Bitmap) BitmapFactory.decodeStream(connection.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();// ругается на разницу Bitmap и graphics.Bitmap
                }

                finished = true;
                return image;
            }
        } else {
            image = null;

            stopLoading();
        }

        return image;
    }


    @Override
    protected void onStartLoading() {//method of Loader
        // protected Bitmap onLoadInBackground() {//takeContentChanged(){//onContentChanged() {
        if (takeContentChanged()) {
            forceLoad();
        }

        while (!finished) {
            progress += 5;
            if (progress < MainActivity.MAX_COUNT) {
                Log.d(getClass().getSimpleName(), "Progress value is " + progress);
                Log.d(getClass().getSimpleName(), "getActivity is " + getContext());
                Log.d(getClass().getSimpleName(), "this is " + this);

                if (mActivity.get() != null) {
                    mActivity.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.get().setProgressToProgressBar(progress);
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onStopLoading() {//method of Loader
        cancelLoad();
        Log.d(TAGG, "loader: on stop loading");
    }





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
