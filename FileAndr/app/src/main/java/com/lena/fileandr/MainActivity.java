package com.lena.fileandr;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Bitmap> {
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderManager loaderManager = getLoaderManager();
        //MyImageAsyncLoader imageAsyncLoader = new MyImageAsyncLoader(getApplicationContext());
        // (MyImageAsyncLoader) findViewById(R.id.image_view);
        loaderManager.initLoader(LOADER_ID, null, this);
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        //getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public MyImageAsyncLoader<Bitmap> onCreateLoader(int id, Bundle args) {
        //getFilesDir()
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
        //return new CursorLoader(MainActivity.this, CONTENT_URI, PROJECTION, null, null, null);
        return new MyImageAsyncLoader<Bitmap>(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        switch (loader.getId()) {
            case LOADER_ID: {
                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                imageView.setImageBitmap(data);
                //imgLoader.DisplayImage(R.string.image_url, loader, (ImageView) findViewById(R.id.image_view));
            }
            break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        mAdapter.swapCursor(null);
    }

    public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }
}
