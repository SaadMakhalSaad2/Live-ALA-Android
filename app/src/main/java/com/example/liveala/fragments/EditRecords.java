package com.example.liveala.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.liveala.R;

import com.example.liveala.Utils.Models.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditRecords extends Fragment {
    TextView studentname;
    EditText hallNumber;
    Button save;
    List<UserProfile> userProfiles = new ArrayList<>();
    int counter = 0;
    UserProfile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_records, container, false);

        studentname = root.findViewById(R.id.student_name);
        hallNumber = root.findViewById(R.id.hallOrder);
        save = root.findViewById(R.id.button_save);

        downloadUsers();
        save.setOnClickListener(v -> {
            if (hallNumber.getText().toString().trim().length() == 0) {
                hallNumber.setError("input value");
                return;
            }

            UserProfile updatedProfile = profile;
            updatedProfile.setHallOrder(hallNumber.getText().toString() + "");

            FirebaseDatabase.getInstance().getReference("profiles/" + profile.getId()).setValue(updatedProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        snack("added");
                        profile = userProfiles.get(counter + 1);
                        studentname.setText(profile.getName());
                    } else
                        snack("FAILED");
                }
            });

        });

        return root;
    }

    private void downloadUsers() {
        FirebaseDatabase.getInstance().getReference("profiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserProfile profile = snapshot1.getValue(UserProfile.class);
                    if (profile != null && profile.getUserType() != null && profile.getUserType().toLowerCase().equals("student")) {
                        userProfiles.add(profile);
                    }
                }

                save.setEnabled(true);
                if (!userProfiles.isEmpty()) {
                    profile = userProfiles.get(counter);
                    studentname.setText(profile.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void snack(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

    }

}