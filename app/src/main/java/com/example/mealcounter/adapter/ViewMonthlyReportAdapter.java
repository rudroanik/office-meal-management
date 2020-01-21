package com.example.mealcounter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcounter.R;
import com.example.mealcounter.Utils.MonthlyInfo;
import com.example.mealcounter.Utils.Order;
import com.example.mealcounter.databinding.ModelViewMonthlyReportBinding;

import java.util.List;

public class ViewMonthlyReportAdapter extends RecyclerView.Adapter<ViewMonthlyReportAdapter.ViewHolder> {

    private Context context;
    private List<MonthlyInfo> orderList;
    private int mealRate;

    public ViewMonthlyReportAdapter(Context context, List<MonthlyInfo> orderList,int mealRate) {
        this.context = context;
        this.orderList = orderList;
        this.mealRate = mealRate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelViewMonthlyReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.model_view_monthly_report,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MonthlyInfo order= orderList.get(position);

        holder.binding.nameTvID.setText(order.getUserName());
        holder.binding.totalMealTvID.setText("Total Meal: "+order.getTotalOrder());
        holder.binding.totalAmountTvID.setText(mealRate * order.getTotalOrder() +" Tk");



    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ModelViewMonthlyReportBinding binding;
        public ViewHolder(ModelViewMonthlyReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
