package com.lena.fileandr;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] PROJECTION = new String[]{"_id", "text_column"};
    private static final int LOADER_ID = 1;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] dataColumns = {"text_column"};
        int[] viewIDs = {R.id.text_view};
        mAdapter = new SimpleCursorAdapter(this, R.layout.list_itemnull, dataColumns, viewIDs, 0);
        setListAdapter(mAdapter);
        mCallbacks = this;
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        //getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(MainActivity.this, CONTENT_URI, PROJECTION, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
            mAdapter.swapCursor(data);
            break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
