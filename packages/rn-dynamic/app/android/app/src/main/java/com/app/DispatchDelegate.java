package com.app;

import android.app.Activity;

import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

//DispatchDelegate
public class DispatchDelegate extends ReactActivityDelegate {

    private Activity activity;
    private String bundleName;


    public DispatchDelegate(Activity activity, @Nullable String bundleName) {
        super(activity, bundleName);
        this.activity = activity;
        this.bundleName = bundleName;
    }

    @Override
    protected ReactNativeHost getReactNativeHost() {

        ReactNativeHost mReactNativeHost = new ReactNativeHost(activity.getApplication()) {
            @Override
            public boolean getUseDeveloperSupport() {
                return BuildConfig.DEBUG;
            }
            //注册原生模块，这样
            @Override
            protected List<ReactPackage> getPackages() {
                return Arrays.<ReactPackage>asList(
                        new MainReactPackage()
                );
            }

            @Nullable
            @Override
            protected String getJSBundleFile() {
                // 读取已经解压的bundle文件
                String file = activity.getFilesDir().getAbsolutePath() + "/" + bundleName + "/" + bundleName + ".bundle";
                return file;
            }

            @Nullable
            @Override
            protected String getBundleAssetName() {
                return bundleName + ".bundle";
            }

            @Override
            protected String getJSMainModuleName() {
                return "index";
            }
        };
        return mReactNativeHost;
    }
}