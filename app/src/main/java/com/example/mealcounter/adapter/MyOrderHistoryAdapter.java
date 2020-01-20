package com.example.mealcounter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcounter.R;
import com.example.mealcounter.Utils.Order;
import com.example.mealcounter.databinding.ModelOrderBinding;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyOrderHistoryAdapter extends RecyclerView.Adapter<MyOrderHistoryAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");

    public MyOrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.model_order,parent,false);
        return new MyOrderHistoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.binding.nameID.setText(order.getUserName());
        holder.binding.dateID.setText(order.getDate());
        holder.binding.timeTvID.setText(dateFormat.format(order.getTime()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ModelOrderBinding binding;
        public ViewHolder(ModelOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
