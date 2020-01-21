package com.example.mealcounter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mealcounter.R;
import com.example.mealcounter.Utils.MonthlyInfo;
import com.example.mealcounter.Utils.Order;
import com.example.mealcounter.Utils.UserInfo;
import com.example.mealcounter.adapter.ViewMonthlyReportAdapter;
import com.example.mealcounter.databinding.ActivityViewMonthlyReportBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMonthlyReportActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    ActivityViewMonthlyReportBinding binding;
    private static final String TAG = "ViewMonthlyReportActivity";
    ViewMonthlyReportAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_monthly_report);
        firebaseDatabase = FirebaseDatabase.getInstance();
        getMonth();


        binding.spinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                getMonthlyHistory(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMonth() {
        final List<String> year = new ArrayList<>();
        final DatabaseReference databaseReferenceMonthlyOrder = firebaseDatabase.getReference().child("MonthlyOrder");
        databaseReferenceMonthlyOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        year.add(snapshot.getKey());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewMonthlyReportActivity.this, android.R.layout.simple_spinner_dropdown_item, year);
                    binding.spinnerId.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMonthlyHistory(String item) {
        final List<String> userIdList = new ArrayList<>();
        final List<Order> orderList = new ArrayList<>();
        final List<UserInfo> userList = new ArrayList<>();
        final List<MonthlyInfo> monthInfoList = new ArrayList<>();
        final DatabaseReference databaseReferenceMonthlyOrder = firebaseDatabase.getReference().child("MonthlyOrder").child(item);
        final DatabaseReference databaseReferenceUserInfo = firebaseDatabase.getReference().child("UserInfo");

        databaseReferenceMonthlyOrder.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        userIdList.add(dataSnapshot1.getKey());

                    }
                }
                for (final String userId : userIdList) {


                    databaseReferenceUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                userList.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    userList.add(dataSnapshot1.getValue(UserInfo.class));
                                }

                                databaseReferenceMonthlyOrder.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                orderList.add(data.getValue(Order.class));
                                            }
                                        }
                                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("mealRate");


                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String meal;
                                                if (dataSnapshot.exists()) {
                                                    meal = dataSnapshot.getValue().toString();
                                                    monthInfoList.clear();

                                                    for (UserInfo userInfo1 : userList) {


                                                        MonthlyInfo monthlyInfo = new MonthlyInfo(userInfo1.getUserName(), userInfo1.getUserId(), orderList.size());
                                                        monthInfoList.add(monthlyInfo);


                                                    }

                                                    initRecyclerView(monthInfoList, Integer.parseInt(meal));
                                                }

                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void initRecyclerView(List<MonthlyInfo> monthlyOrder, int mealRate) {
        adapter = new ViewMonthlyReportAdapter(this, monthlyOrder, mealRate);
        binding.recyclerViewID.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewID.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
