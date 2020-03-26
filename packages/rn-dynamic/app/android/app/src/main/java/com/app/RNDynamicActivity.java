package com.app;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;

//RNDynamicActivity
public class RNDynamicActivity extends ReactActivity {
    public static String bundleName;

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {

        DispatchDelegate delegate = new DispatchDelegate(this, bundleName);

        return delegate;
    }
}