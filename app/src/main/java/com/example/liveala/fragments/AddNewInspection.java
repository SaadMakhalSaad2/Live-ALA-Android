package com.example.liveala.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liveala.R;
import com.example.liveala.Utils.Adapters.RoomAdapter;
import com.example.liveala.Utils.Models.GeneralInspection;
import com.example.liveala.Utils.Models.Pref;
import com.example.liveala.Utils.Models.Score;
import com.example.liveala.Utils.Models.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.liveala.Utils.Models.Pref.USER_PROFILE;

public class AddNewInspection extends Fragment {

    String hallName = "";
    boolean general_expanded, individuals_expanded;
    Button general, buttonRoomsInspection, saveGeneral, saveRooms;
    LinearLayout inspectionContent, contbuttonRoomsInspection;
    TextView inspectionfor;
    LinearLayout generalElements;
    ListView rooms;
    List<UserProfile> students = new ArrayList<>();
    ProgressBar progressBar;
    TextView noData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_new_inspection, container, false);
        hallName = AddNewInspectionArgs.fromBundle(getArguments()).getHallName();

        linkUi(root);

        setupAdapters(root);

        downloadStudents();
        return root;
    }

    private void downloadStudents() {
        FirebaseDatabase.getInstance().getReference("profiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserProfile profile = snapshot1.getValue(UserProfile.class);
                    if (profile != null && profile.getHall() != null && profile.getHall().toLowerCase().equals(hallName.toLowerCase())) {
                        students.add(profile);
                    }
                }
                progressBar.setVisibility(View.GONE);
                if (!students.isEmpty()) {
                    RoomAdapter adp = new RoomAdapter(getActivity(), R.layout.item_room, (ArrayList<UserProfile>) students);
                    rooms.setAdapter(adp);
                    inspectionContent.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);

                } else {
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void linkUi(View root) {
        saveGeneral = root.findViewById(R.id.button_save_inspection_general);
        saveRooms = root.findViewById(R.id.button_save_rooms_inspection);
        noData = root.findViewById(R.id.no_data);
        inspectionContent = root.findViewById(R.id.cont_inspection);
        contbuttonRoomsInspection = root.findViewById(R.id.cont_rooms_inspection);
        progressBar = root.findViewById(R.id.progress_load_students);
        rooms = root.findViewById(R.id.list_rooms);
        generalElements = root.findViewById(R.id.cont_general_inespeciton_elements);
        inspectionfor = root.findViewById(R.id.text_inspection_for);
        if (hallName != null)
            inspectionfor.setText(inspectionfor.getText() + hallName);
        general = root.findViewById(R.id.button_general_hall_rubric);
        general.setOnClickListener(v -> {
            Toast.makeText(getActivity(), hallName, Toast.LENGTH_SHORT).show();
            if (!general_expanded) {
                generalElements.setVisibility(View.VISIBLE);
                general_expanded = true;
            } else {
                generalElements.setVisibility(View.GONE);
                general_expanded = false;
            }
        });

        buttonRoomsInspection = root.findViewById(R.id.button_rooms_inspeciton);
        buttonRoomsInspection.setOnClickListener(v -> {
            if (!individuals_expanded) {
                contbuttonRoomsInspection.setVisibility(View.VISIBLE);
                individuals_expanded = true;
            } else {
                contbuttonRoomsInspection.setVisibility(View.GONE);
                individuals_expanded = false;
            }
        });

        saveGeneral.setOnClickListener(v -> saveGeneralInspection());

        saveRooms.setOnClickListener(v -> saveRoomsInspection());

    }

    private void saveRoomsInspection() {

    }

    private void sendRoomsInspection() {

        int total = 0;
        List<Score> scores = new ArrayList<>();

        Score fridgeScore = new Score("fridge", Integer.parseInt(fridge.getSelectedItem().toString()));
        scores.add(fridgeScore);
        total += Integer.parseInt(fridge.getSelectedItem().toString());

        Score kitchen1 = new Score("kitchen", Integer.parseInt(kitchen.getSelectedItem().toString()));
        scores.add(kitchen1);
        total += Integer.parseInt(kitchen.getSelectedItem().toString());

        Score dinning_hall1 = new Score("dinning hall", Integer.parseInt(dinning_hall.getSelectedItem().toString()));
        scores.add(dinning_hall1);
        total += Integer.parseInt(dinning_hall.getSelectedItem().toString());


        Score room_cupboard = new Score("broom cupboard", Integer.parseInt(broom_cupboard.getSelectedItem().toString()));
        scores.add(room_cupboard);
        total += Integer.parseInt(broom_cupboard.getSelectedItem().toString());

        Score microwave1 = new Score("microwave", Integer.parseInt(microwave.getSelectedItem().toString()));
        scores.add(microwave1);
        total += Integer.parseInt(microwave.getSelectedItem().toString());


        Score hall_way1 = new Score("hall way", Integer.parseInt(hall_way.getSelectedItem().toString()));
        scores.add(hall_way1);
        total += Integer.parseInt(hall_way.getSelectedItem().toString());


        Score linen_change1 = new Score("linen change", Integer.parseInt(linen_change.getSelectedItem().toString()));
        scores.add(linen_change1);
        total += Integer.parseInt(linen_change.getSelectedItem().toString());

        Score inspection_report1 = new Score("inspection report", Integer.parseInt(inspection_report.getSelectedItem().toString()));
        scores.add(inspection_report1);
        total += Integer.parseInt(inspection_report.getSelectedItem().toString());

        //TODO: get the value of the hall object
        GeneralInspection inspection = new GeneralInspection(new Date(),
                userProfile,
                null,
                scores,
                total);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("general_inspections");
        String key = reference.push().getKey();
        reference.push().setValue(inspection).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    snack("Saved Successfully");
                    clearData();
                } else {
                    snack(task.getException().getMessage());
                }
            }
        });

    }

    private void clearData() {

    }

    private boolean scoresEnsured() {
        boolean valid = false;

        if (fridge.getSelectedItemPosition() != 0 &&
                kitchen.getSelectedItemPosition() != 0 &&
                broom_cupboard.getSelectedItemPosition() != 0 &&
                microwave.getSelectedItemPosition() != 0 &&
                hall_way.getSelectedItemPosition() != 0 &&
                inspection_report.getSelectedItemPosition() != 0 &&
                linen_change.getSelectedItemPosition() != 0 &&
                dinning_hall.getSelectedItemPosition() != 0)
            valid = true;

        return valid;
    }

    UserProfile userProfile;

    private void saveGeneralInspection() {
        if (!scoresEnsured()) {
            snack("You need to complete all criteria first");
            return;
        }

        String json = Pref.getValue(getActivity(), USER_PROFILE, null);
        userProfile = new Gson().fromJson(json, UserProfile.class);
        if (userProfile == null) {
            snack("Unknown Error!");
            return;
        }

        sendRoomsInspection();
    }

    Spinner fridge;
    Spinner kitchen;
    Spinner dinning_hall;
    Spinner broom_cupboard;
    Spinner microwave;
    Spinner hall_way;
    Spinner inspection_report;
    Spinner linen_change;

    private void setupAdapters(View root) {
        String[] scores_5 = getResources().getStringArray(R.array.scores_5);
        String[] scores_10 = getResources().getStringArray(R.array.scores_10);

        fridge = (Spinner) root.findViewById(R.id.fridge);
        kitchen = (Spinner) root.findViewById(R.id.kitchen);
        dinning_hall = (Spinner) root.findViewById(R.id.dinning_hall);
        broom_cupboard = (Spinner) root.findViewById(R.id.broom_cupboard);
        microwave = (Spinner) root.findViewById(R.id.microwave);
        hall_way = (Spinner) root.findViewById(R.id.hall_way);
        inspection_report = (Spinner) root.findViewById(R.id.inspection_report);
        linen_change = (Spinner) root.findViewById(R.id.linen_change);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_5);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        inspection_report.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_5);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        fridge.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_10);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        kitchen.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_10);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        dinning_hall.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_5);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        broom_cupboard.setAdapter(adapter5);

        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_5);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        microwave.setAdapter(adapter6);

        ArrayAdapter<String> adapter7 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_5);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        hall_way.setAdapter(adapter7);

        ArrayAdapter<String> adapter8 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_5);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        linen_change.setAdapter(adapter8);

    }

    public void snack(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

    }

}