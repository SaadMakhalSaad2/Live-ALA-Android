package com.example.liveala.Utils.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.liveala.R;
import com.example.liveala.Activities.Home;
import com.example.liveala.Utils.Models.IndividualInspection;
import com.example.liveala.Utils.Models.Room;
import com.example.liveala.Utils.Models.RoomsInspectionScore;
import com.example.liveala.Utils.Models.Score;
import com.example.liveala.Utils.Models.UserProfile;
import com.example.liveala.fragments.AddNewInspection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RoomAdapter extends ArrayAdapter<Room> {
    TextView studentName, textLastInspected, nameNumber;
    UserProfile inspector, student;
    private final ArrayList<UserProfile> profiles;
    Context context;
    AlertDialog dialog;
    Button save, exit;
    Spinner floor, beds, dinningHall, disk, bin, clothing, heater, window, spoiltFood, fridge;
    Activity activity;
    ImageView done;

    public RoomAdapter(@NonNull Context context, int resource, ArrayList<UserProfile> profiles, UserProfile inspector, Activity activity) {
        super(context, resource);
        this.profiles = profiles;
        this.context = context;
        this.inspector = inspector;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    View dialogView;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_room,
                    parent, false);
        }

        student = profiles.get(position);

        studentName = convertView.findViewById(R.id.student_name);
        textLastInspected = convertView.findViewById(R.id.text_last_inpsected);
        nameNumber = convertView.findViewById(R.id.name_number);
        done = convertView.findViewById(R.id.image_inspection_done);
        studentName.setText(student.getName());
        nameNumber.setText(position + 1  + "");

        if (student.getLastInspected() != null)
            textLastInspected.setText(getDateFormat(student.getLastInspected()));

        return convertView;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    private String getDateFormat(Date lastInspected) {
        String formattedDate = null;
        long diff = 0;


//           formattedDate = android.text.format.DateFormat.format("LLL, dd hh:mm a", lastInspected).toString();
        diff = new Date().getTime() - lastInspected.getTime();

        if (TimeUnit.MILLISECONDS.toMinutes(diff) > 0) {
            if (TimeUnit.MILLISECONDS.toMinutes(diff) < 5)
                formattedDate = "Just Now";
            else if (TimeUnit.MILLISECONDS.toMinutes(diff) < 60)
                formattedDate = TimeUnit.MILLISECONDS.toMinutes(diff) + " m";
        } else {
            formattedDate = "Just Now";
        }

        if (TimeUnit.MILLISECONDS.toHours(diff) > 0) {
            if (TimeUnit.MILLISECONDS.toHours(diff) < 24) {
                formattedDate = TimeUnit.MILLISECONDS.toHours(diff) + " h";
            } else {
                formattedDate = TimeUnit.MILLISECONDS.toDays(diff) + " days";
            }
        }


        return formattedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    private void showPopUp(UserProfile student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        Home home = (Home) context;
        View view = home.getLayoutInflater().inflate(R.layout.popup_window, null);
        builder.setView(view);
        dialogView = view;

        TextView studentName = view.findViewById(R.id.student_name_inspecting);
        studentName.setText("Inspecting " + student.getName());

        save = view.findViewById(R.id.button_save_inspection_dialog);
        exit = view.findViewById(R.id.button_exit_inspection_dialog);

        exit.setOnClickListener(v -> Toast.makeText(home, "Press long on the button to exit", Toast.LENGTH_SHORT).show());

        exit.setOnLongClickListener(v -> {
            dialog.cancel();
            return true;
        });

        save.setOnClickListener(v -> {
            if (!ensureData()) {
                snack("You need to complete all criteria first");
                return;
            }

            sendInspection();
        });

        setupSpinners(view);

        builder.setOnCancelListener(dialog2 -> Toast.makeText(home, "Cancelled", Toast.LENGTH_SHORT).show());

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private boolean ensureData() {
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

    private void sendInspection() {
        calculateScore();

        IndividualInspection inspection = new IndividualInspection(student,
                inspector,
                new Date(),
                total,
                scores);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("individuals_inspections");
        reference.push().setValue(inspection).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar.make(activity.findViewById(android.R.id.content), "Saved successfully!", Snackbar.LENGTH_LONG).show();
                    clearData();
                } else {
                    Snackbar.make(activity.findViewById(android.R.id.content), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                }
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

    private void clearData() {
        dialog.dismiss();
        done.setVisibility(View.VISIBLE);
    }

    private void setupSpinners(View view) {
        String[] scores_2 = context.getResources().getStringArray(R.array.scores_2);
        String[] scores_4 = context.getResources().getStringArray(R.array.scores_4);

        floor = view.findViewById(R.id.floor);
        beds = view.findViewById(R.id.beds);
        dinningHall = view.findViewById(R.id.dinning_hall_items_dialog);
        disk = view.findViewById(R.id.disk_clean);
        bin = view.findViewById(R.id.bin_empty);
        clothing = view.findViewById(R.id.clothing_lying_around);
        heater = view.findViewById(R.id.heater);
        window = view.findViewById(R.id.window_seal);
        spoiltFood = view.findViewById(R.id.spoilt_food);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.item_spinner, scores_2);
        adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        floor.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, R.layout.item_spinner, scores_2);
        adapter2.setDropDownViewResource(R.layout.item_dropdown_spinner);
        disk.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context, R.layout.item_spinner, scores_2);
        adapter3.setDropDownViewResource(R.layout.item_dropdown_spinner);
        clothing.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(context, R.layout.item_spinner, scores_2);
        adapter4.setDropDownViewResource(R.layout.item_dropdown_spinner);
        heater.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(context, R.layout.item_spinner, scores_2);
        adapter5.setDropDownViewResource(R.layout.item_dropdown_spinner);
        spoiltFood.setAdapter(adapter5);

        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(context, R.layout.item_spinner, scores_4);
        adapter6.setDropDownViewResource(R.layout.item_dropdown_spinner);
        beds.setAdapter(adapter6);

        ArrayAdapter<String> adapter7 = new ArrayAdapter<>(context, R.layout.item_spinner, scores_4);
        adapter7.setDropDownViewResource(R.layout.item_dropdown_spinner);
        dinningHall.setAdapter(adapter7);

        ArrayAdapter<String> adapter8 = new ArrayAdapter<>(context, R.layout.item_spinner, scores_4);
        adapter8.setDropDownViewResource(R.layout.item_dropdown_spinner);
        bin.setAdapter(adapter8);

        ArrayAdapter<String> adapter9 = new ArrayAdapter<>(context, R.layout.item_spinner, scores_4);
        adapter9.setDropDownViewResource(R.layout.item_dropdown_spinner);
        window.setAdapter(adapter9);

    }


    public void snack(String message) {
        Snackbar.make(dialogView, message, Snackbar.LENGTH_LONG).show();

    }

}


