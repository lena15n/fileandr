package com.lena.fileandr;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class AppResultsReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle data);
    }

    public AppResultsReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(AppResultsReceiver.Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}