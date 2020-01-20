package com.example.mealcounter.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mealcounter.R;
import com.example.mealcounter.Utils.Order;
import com.example.mealcounter.adapter.ViewDailyOrderAdapter;
import com.example.mealcounter.databinding.ActivityViewOrderBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewOrderActivity extends AppCompatActivity {

    ActivityViewOrderBinding binding;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    Date date = new Date();
    List<Order> dailyOrderList;
    ViewDailyOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_order);

        if (getDailyOrderList().size()>0){
            binding.swipeRefreshLayoutId.setRefreshing(false);
        }
        else {
            binding.swipeRefreshLayoutId.setRefreshing(true);

        }
        binding.swipeRefreshLayoutId.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDailyOrderList();
                binding.swipeRefreshLayoutId.setRefreshing(false);
            }
        });

    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initRecyclerView(List<Order> dailyOrderList) {
        adapter = new ViewDailyOrderAdapter(this, dailyOrderList);
        binding.recyclerViewID.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewID.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private List<Order> getDailyOrderList(){

        dailyOrderList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("DailyOrder").child(dateFormat.format(date)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Order order= data.getValue(Order.class);
                        dailyOrderList.add(order);
                        initRecyclerView(dailyOrderList);
                        binding.swipeRefreshLayoutId.setRefreshing(false);
                        binding.countTvID.setText("Total number of order: "+dailyOrderList.size());


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ViewOrderActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        return  dailyOrderList;

    }
}
