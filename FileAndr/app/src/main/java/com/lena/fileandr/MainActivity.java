package com.lena.fileandr;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Bitmap> {
    private static final int LOADER_ID = 1;
    private static final String TAGG = "Zooo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        Log.d(TAGG, "create activ   idontnow");
    }

    @Override
    public MyImageAsyncLoader<Bitmap> onCreateLoader(int id, Bundle args) {
        //initialize progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // установить в 0 и подписать на события изменения закачки progressBar.
        progressBar.setVisibility(View.VISIBLE);

        Log.d(TAGG, " create loader idontnow");
        return new MyImageAsyncLoader<>(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        switch (loader.getId()) {
            case LOADER_ID: {
                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                imageView.setImageBitmap(data);
                Log.d(TAGG, "something finished idontnow");
                //imgLoader.DisplayImage(R.string.image_url, loader, (ImageView) findViewById(R.id.image_view));
            }
            break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        //clear old data
    }

    /*public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }*/
}
