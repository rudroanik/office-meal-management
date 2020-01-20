package com.example.mealcounter.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mealcounter.R;
import com.example.mealcounter.Utils.Order;
import com.example.mealcounter.activity.ViewOrderActivity;
import com.example.mealcounter.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final String TAG = "HomeFragment";
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM-yyyy");
    Date date = new Date();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<Order> dailyOrderList = new ArrayList<>();
    String userId;
    FirebaseDatabase firebaseDatabase;



    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();

        getDailyOrderList();
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userId = sharedPreferences.getString("UserId", "No Id");


        binding.dateID.setText("(" + dateFormat.format(date) + ") " + dayFormat.format(date));
        binding.viewOrderLayoutID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ViewOrderActivity.class));

            }
        });

        binding.oderButtonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Order order = new Order(sharedPreferences.getString("UserName", "No Name"), dateFormat.format(date), true, System.currentTimeMillis());
                saveOrderIntoDb(order);


            }
        });

        binding.cancelTvID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeOrder(userId);
            }
        });


        return binding.getRoot();
    }


    private void saveOrderIntoDb(Order order) {
        final HashMap<String, Object> hashMap = new HashMap<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("DailyOrder").child(dateFormat.format(date));
        final DatabaseReference databaseReferenceMonthlyOrder = firebaseDatabase.getReference().child("MonthlyOrder").child(monthFormat.format(date));
        final String orderID = databaseReference.push().getKey();

        hashMap.put("userId", userId);
        hashMap.put("orderId", orderID);
        hashMap.put("userName", order.getUserName());
        hashMap.put("date", order.getDate());
        hashMap.put("isOrdered", order.isOrdered());
        hashMap.put("time", order.getTime());

        databaseReference.child(userId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                editor.putString("orderId", orderID);
                editor.apply();
                binding.doneImageID.setVisibility(View.VISIBLE);
                binding.cancelTvID.setVisibility(View.VISIBLE);
                binding.oderButtonID.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Order submitted", Toast.LENGTH_SHORT).show();
                databaseReferenceMonthlyOrder.child(userId).child(orderID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });

    }

    private List<Order> getDailyOrderList() {


        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("DailyOrder").child(dateFormat.format(date)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    dailyOrderList.add(order);
                    for (Order order1 : dailyOrderList) {
                        if (order1.getUserName().equals(sharedPreferences.getString("UserName", "No Name"))) {

                            binding.doneImageID.setVisibility(View.VISIBLE);
                            binding.cancelTvID.setVisibility(View.VISIBLE);
                            binding.oderButtonID.setVisibility(View.GONE);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return dailyOrderList;
    }


    private void removeOrder(final String userId) {

        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("DailyOrder").child(dateFormat.format(date)).child(userId);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                final DatabaseReference databaseReferenceMonthlyOrder = firebaseDatabase.getReference().child("MonthlyOrder").child(monthFormat.format(date)).child(userId);
                databaseReferenceMonthlyOrder.child(sharedPreferences.getString("orderId", "")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        binding.doneImageID.setVisibility(View.GONE);
                        binding.cancelTvID.setVisibility(View.GONE);
                        binding.oderButtonID.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Order cancel", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}



