package com.lena.fileandr;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Bitmap> {
    private static final int LOADER_ID = 1;
    public final static String BROADCAST_ACTION = "com.lena.fileandr.loaderbackbroadcast";
    public static final String PROGRESS = "currprogress";

    private static Button downloadButton;
    private static TextView statusLabelTextView;
    private static boolean isImageDownloaded;

    private ProgressBar progressBar;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setMax(Constants.MAX_COUNT);
        progressBar.setVisibility(View.INVISIBLE);

        statusLabelTextView = (TextView) findViewById(R.id.status_text_view);
        statusLabelTextView.setText(getString(R.string.status_idle));

        isImageDownloaded = false;

        downloadButton = (Button) findViewById(R.id.button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isImageDownloaded) {
                    getLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);


                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(getString(R.string.content_path) + getString(R.string.image_file_name)), "image/*");
                    startActivity(intent);
                }
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int currentProgress = intent.getIntExtra(PROGRESS, 0);
                Log.d(Constants.TAGG, "onReceive: currentProgress = " + currentProgress + ", status = " + currentProgress);

                if (currentProgress <= Constants.MAX_COUNT) {
                        progressBar.setProgress(currentProgress);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        Log.d(Constants.TAGG, "create activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public MyImageAsyncLoader<Bitmap> onCreateLoader(int id, Bundle args) {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        statusLabelTextView = (TextView) findViewById(R.id.status_text_view);
        statusLabelTextView.setText(getString(R.string.status_downloading));

        downloadButton = (Button) findViewById(R.id.button);
        downloadButton.setEnabled(false);

        Log.d(Constants.TAGG, "create loader");
        return new MyImageAsyncLoader<>(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        switch (loader.getId()) {
            case LOADER_ID: {
                progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.INVISIBLE);

                if (data != null) {
                    //saveImageToInternalStorage(data);

                    statusLabelTextView = (TextView) findViewById(R.id.status_text_view);
                    statusLabelTextView.setText(getString(R.string.status_downloaded));

                    downloadButton = (Button) findViewById(R.id.button);
                    downloadButton.setText(R.string.open_button_text);
                    downloadButton.setEnabled(true);

                    isImageDownloaded = true;

                    Log.d(Constants.TAGG, "download has been finished");

                    //getLoaderManager().destroyLoader(id);
                } else {
                    statusLabelTextView = (TextView) findViewById(R.id.status_text_view);
                    statusLabelTextView.setText(getString(R.string.status_idle));

                    downloadButton = (Button) findViewById(R.id.button);
                    downloadButton.setText(R.string.download_button_text);
                    downloadButton.setEnabled(true);

                    isImageDownloaded = false;

                    Toast.makeText(getApplicationContext(), R.string.error_conn, Toast.LENGTH_LONG).show();
                    getLoaderManager().destroyLoader(LOADER_ID);

                    Log.d(Constants.TAGG, "download has ERROR");
                }
            }
            break;
        }
    }

    private boolean saveImageToInternalStorage(android.graphics.Bitmap someImage) {
        try {

            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.image_file_name), Context.MODE_PRIVATE);

            // Use the compress method on the Bitmap object to write image to
            someImage.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        Log.d(Constants.TAGG, "RESET!!");
    }
}
