package com.example.liveala.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liveala.R;
import com.example.liveala.Utils.Adapters.RoomAdapter;
import com.example.liveala.Utils.Models.GeneralInspection;
import com.example.liveala.Utils.Models.Hall;
import com.example.liveala.Utils.Models.IndividualInspection;
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
    Button general, buttonRoomsInspection, saveGeneral;
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

    Hall hall;
    private void downloadStudents() {
        FirebaseDatabase.getInstance().getReference("profiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserProfile profile = snapshot1.getValue(UserProfile.class);
                    if (profile != null && profile.getHall() != null && profile.getHall().toLowerCase().equals(hallName.toLowerCase())) {
                        students.add(profile);
                    }
                }
                hall = new Hall(hallName, students, userProfile);
                progressBar.setVisibility(View.GONE);
                if (!students.isEmpty()) {
                    RoomAdapter adp = new RoomAdapter(getActivity(), R.layout.item_room, (ArrayList<UserProfile>) students, userProfile, getActivity());
                    rooms.setAdapter(adp);
                    inspectionContent.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    setListener();

                } else {
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    UserProfile student;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setListener() {
        rooms.setOnItemClickListener((parent, view, position, id) -> {
            student = students.get(position);
            showPopUp();
        });
    }

    View dialogView;
    Button save, exit;
    AlertDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    private void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.popup_window, null);
        builder.setView(view);
        dialogView = view;

        TextView studentName = view.findViewById(R.id.student_name_inspecting);
        studentName.setText("Inspecting " + student.getName());

        save = view.findViewById(R.id.button_save_inspection_dialog);
        exit = view.findViewById(R.id.button_exit_inspection_dialog);

        exit.setOnClickListener(v -> showSnackDialog("Press long on the button to exit"));

        exit.setOnLongClickListener(v -> {
            dialog.cancel();
            return true;
        });

        save.setOnClickListener(v -> {
            if (!ensureDataIndividualInspection()) {
                showSnackDialog("You need to complete all criteria first");
                return;
            }

            sendIndividualInspection();
        });

        setupSpinners(view);

       // builder.setOnCancelListener(dialog2 -> Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show());

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showSnackDialog(String message) {
        Snackbar snack = Snackbar.make(dialogView, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = 100;
        view.setLayoutParams(params);
        snack.show();
    }

    private void sendIndividualInspection() {
        calculateScore();

        IndividualInspection inspection = new IndividualInspection(student,
                userProfile,
                new Date(),
                total,
                scores);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("individuals_inspections");
        reference.push().setValue(inspection).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateLastInspected();
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    List<Score> scores;
    int total;

    private void calculateScore() {
        scores = new ArrayList<>();
        total = 0;

        Score floorScore = new Score("floor", Integer.parseInt(floor.getSelectedItem().toString()));
        scores.add(floorScore);
        total += Integer.parseInt(floor.getSelectedItem().toString());

        Score bedsScore = new Score("beds", Integer.parseInt(beds.getSelectedItem().toString()));
        scores.add(bedsScore);
        total += Integer.parseInt(beds.getSelectedItem().toString());

        Score dinningHallScore = new Score("dinning hall items", Integer.parseInt(dinningHall.getSelectedItem().toString()));
        scores.add(dinningHallScore);
        total += Integer.parseInt(dinningHall.getSelectedItem().toString());

        Score diskCleanScore = new Score("disk clean", Integer.parseInt(disk.getSelectedItem().toString()));
        scores.add(diskCleanScore);
        total += Integer.parseInt(disk.getSelectedItem().toString());

        Score binEmptyScore = new Score("bin empty", Integer.parseInt(bin.getSelectedItem().toString()));
        scores.add(binEmptyScore);
        total += Integer.parseInt(bin.getSelectedItem().toString());

        Score clothingLyingAroundScore = new Score("clothing lying around", Integer.parseInt(clothing.getSelectedItem().toString()));
        scores.add(clothingLyingAroundScore);
        total += Integer.parseInt(clothing.getSelectedItem().toString());

        Score heaterScore = new Score("heater", Integer.parseInt(heater.getSelectedItem().toString()));
        scores.add(heaterScore);
        total += Integer.parseInt(heater.getSelectedItem().toString());

        Score windowSealScore = new Score("window seal", Integer.parseInt(window.getSelectedItem().toString()));
        scores.add(windowSealScore);
        total += Integer.parseInt(window.getSelectedItem().toString());

        Score spoiltFoodScore = new Score("spoilt food", Integer.parseInt(spoiltFood.getSelectedItem().toString()));
        scores.add(spoiltFoodScore);
        total += Integer.parseInt(spoiltFood.getSelectedItem().toString());

    }

    private boolean ensureDataIndividualInspection() {
        boolean valid = false;
        if (floor.getSelectedItemPosition() != 0 &&
                beds.getSelectedItemPosition() != 0 &&
                dinningHall.getSelectedItemPosition() != 0 &&
                disk.getSelectedItemPosition() != 0 &&
                clothing.getSelectedItemPosition() != 0 &&
                heater.getSelectedItemPosition() != 0 &&
                window.getSelectedItemPosition() != 0 &&
                spoiltFood.getSelectedItemPosition() != 0)
            valid = true;

        return valid;
    }

    Spinner floor, beds, dinningHall, disk, bin, clothing, heater, window, spoiltFood;

    private void setupSpinners(View view) {
        String[] scores_2 = getActivity().getResources().getStringArray(R.array.scores_2);
        String[] scores_4 = getActivity().getResources().getStringArray(R.array.scores_4);

        floor = view.findViewById(R.id.floor);
        beds = view.findViewById(R.id.beds);
        dinningHall = view.findViewById(R.id.dinning_hall_items_dialog);
        disk = view.findViewById(R.id.disk_clean);
        bin = view.findViewById(R.id.bin_empty);
        clothing = view.findViewById(R.id.clothing_lying_around);
        heater = view.findViewById(R.id.heater);
        window = view.findViewById(R.id.window_seal);
        spoiltFood = view.findViewById(R.id.spoilt_food);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_2);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        floor.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_2);
        adapter2.setDropDownViewResource(R.layout.item_dropdown_spinner);
        disk.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_2);
        adapter3.setDropDownViewResource(R.layout.item_dropdown_spinner);
        clothing.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_2);
        adapter4.setDropDownViewResource(R.layout.item_dropdown_spinner);
        heater.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_2);
        adapter5.setDropDownViewResource(R.layout.item_dropdown_spinner);
        spoiltFood.setAdapter(adapter5);

        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_4);
        adapter6.setDropDownViewResource(R.layout.item_dropdown_spinner);
        beds.setAdapter(adapter6);

        ArrayAdapter<String> adapter7 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_4);
        adapter7.setDropDownViewResource(R.layout.item_dropdown_spinner);
        dinningHall.setAdapter(adapter7);

        ArrayAdapter<String> adapter8 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_4);
        adapter8.setDropDownViewResource(R.layout.item_dropdown_spinner);
        bin.setAdapter(adapter8);

        ArrayAdapter<String> adapter9 = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, scores_4);
        adapter9.setDropDownViewResource(R.layout.item_dropdown_spinner);
        window.setAdapter(adapter9);

    }

    @SuppressLint("SetTextI18n")
    private void linkUi(View root) {
        saveGeneral = root.findViewById(R.id.button_save_inspection_general);
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
            openIndividualInspection();
        });

        saveGeneral.setOnClickListener(v -> saveGeneralInspection());


        String json = Pref.getValue(getActivity(), USER_PROFILE, null);
        userProfile = new Gson().fromJson(json, UserProfile.class);
    }

    private void openIndividualInspection() {
        if (!individuals_expanded) {
            contbuttonRoomsInspection.setVisibility(View.VISIBLE);
            individuals_expanded = true;
        } else {
            contbuttonRoomsInspection.setVisibility(View.GONE);
            individuals_expanded = false;
        }
    }


    private void sendGeneralInspection() {
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
                hall,
                scores,
                total);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("general_inspections");
        reference.push().setValue(inspection).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    clearDataGeneralInspection();
                } else {
                    snack(task.getException().getMessage());
                }
            }
        });

    }

    private void clearDataGeneralInspection() {
        snack("Saved Successfully");

        refreshFragment();


    }

    UserProfile updatedProfile = null;

    private void updateLastInspected() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles/" + student.getId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile profile = snapshot.getValue(UserProfile.class);
                updatedProfile = profile;
                updatedProfile.setLastInspected(new Date());
                reference.setValue(updatedProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            clearDateIndividualInspection();
                        } else
                            snack(task.getException().getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                snack(error.getMessage());
            }
        });

    }

    private void clearDateIndividualInspection() {
        snack("Saved Successfully");
        dialog.dismiss();

        refreshFragment();
    }

    private void refreshFragment() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AddNewInspectionDirections.ActionNewInspectionToAddNewInspection action = AddNewInspectionDirections.actionNewInspectionToAddNewInspection(hallName);
        action.setHallName(hallName);
        navController.navigate(action);
        individuals_expanded = false;
        openIndividualInspection();
        students = new ArrayList<>();
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

        if (userProfile == null) {
            snack("Unknown Error!");
            return;
        }

        sendGeneralInspection();
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