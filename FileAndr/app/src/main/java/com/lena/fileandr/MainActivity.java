package com.lena.fileandr;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
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
    private static final String TAGG = "Zooo";
    private static ProgressBar progressBar;
    private static Button downloadButton;
    private static TextView statusLabelTextView;
    private static boolean isImageDownloaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
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
                    intent.setDataAndType(Uri.parse("content://com.lena.fileandr/" + getString(R.string.image_file_name)), "image/*");
                    startActivity(intent);
                }
            }
        });

        Log.d(TAGG, "create activity");
    }

    @Override
    public MyImageAsyncLoader<Bitmap> onCreateLoader(int id, Bundle args) {
        //initialize progress bar
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // установить в 0 и подписать на события изменения закачки progressBar.
        progressBar.setVisibility(View.VISIBLE);

        statusLabelTextView = (TextView) findViewById(R.id.status_text_view);
        statusLabelTextView.setText(getString(R.string.status_downloading));

        downloadButton = (Button) findViewById(R.id.button);
        downloadButton.setEnabled(false);


        Log.d(TAGG, "create loader");
        return new MyImageAsyncLoader<>(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        switch (loader.getId()) {
            case LOADER_ID: {
                progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.INVISIBLE);

                //ImageView imageView = (ImageView) findViewById(R.id.image_view);
                //imageView.setImageBitmap(data);
                if (data != null) {
                    saveImageToInternalStorage(data);

                    statusLabelTextView = (TextView) findViewById(R.id.status_text_view);
                    statusLabelTextView.setText(getString(R.string.status_downloaded));

                    downloadButton = (Button) findViewById(R.id.button);
                    downloadButton.setText(R.string.open_button_text);
                    downloadButton.setEnabled(true);

                    isImageDownloaded = true;

                    Log.d(TAGG, "download has been finished");
                    //imgLoader.DisplayImage(R.string.image_url, loader, (ImageView) findViewById(R.id.image_view));

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

                    Log.d(TAGG, "download has ERROR");
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
        Log.d(TAGG, "RESET!!");
        //clear old data
    }
}
