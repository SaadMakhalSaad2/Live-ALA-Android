package com.example.liveala.Utils.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.liveala.R;
import com.example.liveala.Activities.Home;
import com.example.liveala.Utils.Models.Room;
import com.example.liveala.Utils.Models.UserProfile;

import java.util.ArrayList;

public class RoomAdapter extends ArrayAdapter<Room> {
    TextView studentName;
    private ArrayList<UserProfile> profiles;
    Context context;
    AlertDialog dialog;
    Button save, exit;
    Spinner floor, beds, dinningHall, disk, bin, clothing, heater, window, spoiltFood, fridge;

    public RoomAdapter(@NonNull Context context, int resource, ArrayList<UserProfile> profiles) {
        super(context, resource);
        this.profiles = profiles;
        this.context = context;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_room,
                    parent, false);
        }

        UserProfile student = profiles.get(position);


        studentName = convertView.findViewById(R.id.student_name);
        studentName.setText(student.getName());
        studentName.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                showPopUp(student);
            }
        });

        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    private void showPopUp(UserProfile student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        Home home = (Home) context;
        View view = home.getLayoutInflater().inflate(R.layout.popup_window, null);
        builder.setView(view);

        TextView studentName = view.findViewById(R.id.student_name_inspecting);
        studentName.setText("Inspecting " + student.getName());

        save = view.findViewById(R.id.button_save_inspection_dialog);
        exit = view.findViewById(R.id.button_exit_inspection_dialog);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(home, "Press long on the button to exit", Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog.cancel();
                return true;
            }
        });

        setupSpinners(view);

        builder.setOnCancelListener(dialog2 -> Toast.makeText(home, "Cancelled", Toast.LENGTH_SHORT).show());

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
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


}


