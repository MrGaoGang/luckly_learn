package com.app;

import com.app.model.ModuleItem;
import com.facebook.react.ReactActivity;
import com.google.gson.Gson;

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
    public void onCreate(@Nullable ModuleItem.Bundle savedInstanceState) {
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
                        adapter.clearModules();
                        System.out.println("数据获取成功" + data);
                        adapter.addModules(moduleItem.data);
                    }
                });


            }
        });
    }
}