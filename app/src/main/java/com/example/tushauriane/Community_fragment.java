package com.example.tushauriane;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.tushauriane.ContactActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class Community_fragment extends Fragment {
    private StorageReference storageRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("testimonials");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_fragment, container, false);
        LinearLayout bookingContainer = view.findViewById(R.id.booking_container);

        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        String bookingId = item.getName().replace(".json", "");
                        fetchBookingData(bookingId, bookingContainer);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                });

        AppCompatButton btnRetestify = view.findViewById(R.id.btnRetestify);
        btnRetestify.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ContactActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void fetchBookingData(String bookingId, LinearLayout bookingContainer) {
        StorageReference bookingRef = storageRef.child(bookingId + ".json");
        bookingRef.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(bytes -> {
                    String jsonData = new String(bytes, StandardCharsets.UTF_8);
                    Log.d("Community_fragment", "JSON Data: " + jsonData);
                    try {
                        JSONObject bookingData = new JSONObject(jsonData);
                        populateBookingView(bookingData, bookingContainer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Community_fragment", "Error fetching booking data: " + e.getMessage());
                });
    }

    private void populateBookingView(JSONObject bookingData, LinearLayout bookingContainer) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View bookingView = inflater.inflate(R.layout.booking_item_layout, bookingContainer, false);

        TextView Username = bookingView.findViewById(R.id.username);
        TextView Email_address = bookingView.findViewById(R.id.email_address);
        TextView Testimonial = bookingView.findViewById(R.id.testimonial);

        try {
            Username.setText(bookingData.getString("Username"));
            Email_address.setText(bookingData.getString("Email_address"));
            Testimonial.setText(bookingData.getString("Testimonial"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bookingContainer.addView(bookingView);
    }
}