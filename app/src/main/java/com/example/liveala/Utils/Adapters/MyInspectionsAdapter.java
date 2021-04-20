package com.example.liveala.Utils.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.liveala.R;
import com.example.liveala.Utils.Models.GeneralInspection;
import com.example.liveala.Utils.Models.IndividualInspection;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MyInspectionsAdapter extends ArrayAdapter<IndividualInspection> {
    IndividualInspection individualInspection;
    GeneralInspection generalInspection;
    private final ArrayList<IndividualInspection> individualInspections;
    private final ArrayList<GeneralInspection> generalInspections;

    Context context;
    Activity activity;

    public MyInspectionsAdapter(@NonNull Context context, int resource, ArrayList<IndividualInspection> individualInspections, ArrayList<GeneralInspection> generalInspections, Activity activity) {
        super(context, resource);
        this.individualInspections = individualInspections;
        this.context = context;
        this.activity = activity;
        this.generalInspections = generalInspections;
    }

    @Override
    public int getCount() {
        if (individualInspections != null)
            return individualInspections.size();
        else
            return generalInspections.size();
    }

    View dialogView;

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_inspections,
                    parent, false);
        }
        TextView hallName = convertView.findViewById(R.id.text_hall_name);
        TextView inspectionDate = convertView.findViewById(R.id.text_inspection_date);
        TextView totalScore = convertView.findViewById(R.id.text_total_score);

        if (individualInspections != null) {
            try {
                individualInspection = individualInspections.get(position);
                hallName.setText(individualInspection.getStudent().getHall().substring(0, 1).toUpperCase() + individualInspection.getStudent().getHall().substring(1) + "—" + individualInspection.getStudent().getName());
                inspectionDate.setText(android.text.format.DateFormat.format("LLL, dd hh:mm a", individualInspection.getDate()));
                totalScore.setText(individualInspection.getTotalScore() + "");
                totalScore.setTextColor(getFlagColor(individualInspection.getTotalScore()));
            } catch (Exception ignored) {
                snack(ignored.getMessage());
            }
        } else {
            try {
                generalInspection = generalInspections.get(position);
                hallName.setText(generalInspection.getHall().getHallName().substring(0, 1).toUpperCase() + generalInspection.getHall().getHallName().substring(1));
                inspectionDate.setText(android.text.format.DateFormat.format("LLL, dd hh:mm a", generalInspection.getDate()));
                totalScore.setText(generalInspection.getTotalScore() + "");
                totalScore.setTextColor(getFlagColor(generalInspection.getTotalScore()));
            } catch (Exception ignored) {
                snack(ignored.getMessage());
            }
        }
        return convertView;
    }

    private int getFlagColor(int totalScore) {
        int color = activity.getResources().getColor(R.color.black);

        if (totalScore > 20)
            color = activity.getResources().getColor(R.color.green);
        else
            color = activity.getResources().getColor(R.color.red);

        return color;

    }


    public void snack(String message) {
        Snackbar.make(dialogView, message, Snackbar.LENGTH_LONG).show();
    }

}


