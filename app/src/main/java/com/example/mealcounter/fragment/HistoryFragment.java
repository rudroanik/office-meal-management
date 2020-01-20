package com.example.mealcounter.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mealcounter.R;
import com.example.mealcounter.Utils.Order;
import com.example.mealcounter.adapter.MyOrderHistoryAdapter;
import com.example.mealcounter.adapter.ViewDailyOrderAdapter;
import com.example.mealcounter.databinding.FragmentHistoryBinding;
import com.example.mealcounter.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;
    FirebaseDatabase firebaseDatabase;
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM-yyyy");
    Date date = new Date();
    SharedPreferences sharedPreferences;
    MyOrderHistoryAdapter adapter;

    public HistoryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseDatabase = FirebaseDatabase.getInstance();
        sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_history, container, false);

        getMonth();

        binding.spinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item= parent.getItemAtPosition(position).toString();
                getMonthlyHistory(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void getMonthlyHistory(String item) {
        final List<Order> orderList = new ArrayList<>();
        final DatabaseReference databaseReferenceMonthlyOrder = firebaseDatabase.getReference().child("MonthlyOrder").child(item)
                .child(sharedPreferences.getString("UserId",""));

        databaseReferenceMonthlyOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                       Order order = dataSnapshot1.getValue(Order.class);
                       orderList.add(order);
                       initRecyclerView(orderList);
                       binding.title.setText(getActivity().getResources().getString(R.string.history)+" ("+orderList.size()+")");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMonth(){
        final List<Object> year = new ArrayList<>();
        final DatabaseReference databaseReferenceMonthlyOrder = firebaseDatabase.getReference().child("MonthlyOrder");
        databaseReferenceMonthlyOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                  for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                      year.add(snapshot.getKey());
                  }

                    ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(getContext(), android.R.layout.simple_spinner_dropdown_item, year);
                    binding.spinnerId.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initRecyclerView(List<Order> dailyOrderList) {
        adapter = new MyOrderHistoryAdapter(getActivity(), dailyOrderList);
        binding.recyclerViewID.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewID.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
