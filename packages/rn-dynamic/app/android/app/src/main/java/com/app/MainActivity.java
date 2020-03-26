package com.app;

import android.content.Intent;
import android.os.Bundle;

import com.app.model.ModuleItem;
import com.app.utils.ZipUtils;
import com.facebook.react.ReactActivity;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;
public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ListModuleAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        featchData();
    }

    /**
     * 初始化布局视图，默认数据为空
     */
    public void initView() {
        recyclerView = this.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListModuleAdapter(this, new ArrayList<ModuleItem.Bundle>());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(bundle -> {
            // 检查是否下载过，如果已经下载过则直接打开，暂不考虑各种版本问题
            String f = MainActivity.this.getFilesDir().getAbsolutePath() + "/" + bundle.name + "/" + bundle.name + ".bundle";
            File file = new File((f));
            if (file.exists()) {
                goToRNActivity(bundle.name);
            } else {
                download(bundle.name);
            }
        });
    }

    /**
     * 跳转到RN的展示页面
     * @param bundleName
     */
    public void goToRNActivity(String bundleName) {
        Intent starter = new Intent(MainActivity.this, RNDynamicActivity.class);
        RNDynamicActivity.bundleName = bundleName;
        MainActivity.this.startActivity(starter);
    }

    /**
     * 调用服务获取数据
     */
    public void featchData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(API.MODULES).method("GET", null).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("数据获取失败");
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();

                ModuleItem moduleItem = new Gson().fromJson(data, ModuleItem.class);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //刷新列表
                        adapter.clearModules();
                        adapter.addModules(moduleItem.data);
                    }
                });


            }
        });
    }

    /**
     * 下载对应的bundle
     *
     * @param bundleName
     */
    private void download(final String bundleName) {
        System.out.println(API.DOWNLOAD + bundleName);
        FileDownloader.setup(this);
        FileDownloader.getImpl().create(API.DOWNLOAD + bundleName).setPath(this.getFilesDir().getAbsolutePath(), true)

                .setListener(new FileDownloadListener() {
                    @Override
                    protected void started(BaseDownloadTask task) {
                        super.started(task);
                    }

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {

                        try {
                            //下载之后解压，然后打开
                            ZipUtils.unzip(MainActivity.this.getFilesDir().getAbsolutePath() + "/" + bundleName + ".zip", MainActivity.this.getFilesDir().getAbsolutePath());

                            goToRNActivity(bundleName);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }
}