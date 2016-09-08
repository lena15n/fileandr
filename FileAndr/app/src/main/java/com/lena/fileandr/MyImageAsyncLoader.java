package com.lena.fileandr;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyImageAsyncLoader<Bitmap> extends AsyncTaskLoader<Bitmap> {
    private Bitmap image;
    private static final String TAGG = "Zooo";
    static WeakReference<MainActivity> mActivity;
    private int progress;
    private boolean finished;
    public static final int UPDATE_PROGRESS = 8344;

    public MyImageAsyncLoader(Context context) {
        super(context);
        onContentChanged();
        Log.d(TAGG, "loader: crate loaderr");
    }

    @Override
    public Bitmap loadInBackground() {
        image = null;
        URL url = null;
        HttpURLConnection connection = null;
        Context appContext = getContext();
        Log.d(TAGG, "loader: inside load in background");

        try {
            url = new URL(appContext.getResources().getString(R.string.image_url));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url != null) {
            try {
                connection = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                reset();//stopLoading();cancelLoad();
                Log.d(TAGG, "loader: stop load");
            }

            if (connection != null) {
                try {
                    /*progress = 0;

                    while (!finished) {
                        progress += 1;
                        if (progress <= MainActivity.MAX_COUNT) {
                            Log.d(getClass().getSimpleName(), "Progress value is " + progress);

                            if (mActivity.get() != null) {
                                mActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivity.get().setProgressToProgressBar(progress);
                                    }
                                });
                            }
                        }
                    }*/

                    /*final ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER);
                    receiver.send(Constants.STATUS_RUNNING, Bundle.EMPTY);
*/

                    int fileLength = connection.getContentLength();

                    File catFile = new File(getContext().getFilesDir(), getContext().getString(R.string.image_file_name)) ;


                    // download the file
                    InputStream input = new BufferedInputStream(connection.getInputStream());
                    OutputStream output = new FileOutputStream(catFile);

                    byte[] data = new byte[1024];
                    long total = 0;
                    int count;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);

                        /*// publishing the progress....
                        Bundle resultData = new Bundle();
                        resultData.putInt("progress", (int) (total * 100 / fileLength));//в процентах
                        receiver.send(UPDATE_PROGRESS, resultData);*/
                        progress = (int) (total * MainActivity.MAX_COUNT / fileLength);

                        if (progress <= MainActivity.MAX_COUNT) {
                            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                            Log.d(TAGG, "BR sends progress = " + progress + "..");
                            intent.putExtra(MainActivity.PROGRESS, progress);
                            getContext().sendBroadcast(intent);
                        }
                    }

                    output.flush();
                    output.close();
                    input.close();

                    if (progress < MainActivity.MAX_COUNT) {
                        progress = MainActivity.MAX_COUNT;
                        Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                        Log.d(TAGG, "BR finished!");
                        intent.putExtra(MainActivity.PROGRESS, progress);
                        getContext().sendBroadcast(intent);
                    }

                    image = (Bitmap) BitmapFactory.decodeFile(catFile.getPath());

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
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
        Log.d(TAGG, "loader: on stop loading");
    }
}
