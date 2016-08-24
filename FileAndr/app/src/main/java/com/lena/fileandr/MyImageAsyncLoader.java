package com.lena.fileandr;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Lena on 8/24/2016.
 */
public class MyImageAsyncLoader extends AsyncTaskLoader<D> {

    public MyImageAsyncLoader(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {
        return null;
    }
}
