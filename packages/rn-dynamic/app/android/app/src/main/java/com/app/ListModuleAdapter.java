package com.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.model.ModuleItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//列表适配器编写
public class ListModuleAdapter extends RecyclerView.Adapter<ListModuleAdapter.ItemViewHolder> {
    private List<ModuleItem.Bundle> modules;
    private Context context;
    private OnItemClickListener listener;

    public ListModuleAdapter(Context context, List<ModuleItem.Bundle> modules) {
        this.context = context;
        this.modules = modules;
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addModules(List<ModuleItem.Bundle> data) {
        modules.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 数据清空
     */
    public void clearModules() {
        modules.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //实例化出列表的每一个Item对象
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //绑定数据
        holder.textView.setText(modules.get(position).name);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick((modules.get(position)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    //继承RecyclerView.ViewHolder抽象类的自定义ViewHolder
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemName);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(ModuleItem.Bundle bundle);
    }

}