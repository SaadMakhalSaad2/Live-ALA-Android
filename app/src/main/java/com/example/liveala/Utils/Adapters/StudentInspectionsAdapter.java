package com.example.liveala.Utils.Adapters;

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
import com.example.liveala.Utils.Models.IndividualInspection;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class StudentInspectionsAdapter extends ArrayAdapter<IndividualInspection> {
    IndividualInspection inspection;
    private final ArrayList<IndividualInspection> inspections;
    Context context;
    Activity activity;

    public StudentInspectionsAdapter(@NonNull Context context, int resource, ArrayList<IndividualInspection> inspections, Activity activity) {
        super(context, resource);
        this.inspections = inspections;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return inspections.size();
    }

    View dialogView;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_student_inspection,
                    parent, false);
        }

        inspection = inspections.get(position);

        TextView totalScore = convertView.findViewById(R.id.text_total_score);
        TextView inspectorName = convertView.findViewById(R.id.text_inspector_name);
        TextView date = convertView.findViewById(R.id.text_inspection_date);

        totalScore.setTextColor(getFlagColor(inspection.getTotalScore()));
        try {
            totalScore.setText(inspection.getTotalScore() + "");
            inspectorName.setText(inspection.getInspector().getName());
            date.setText(android.text.format.DateFormat.format("LLL, dd hh:mm a", inspection.getDate()));
        } catch (Exception ignored) {
            snack(ignored.getMessage());
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


