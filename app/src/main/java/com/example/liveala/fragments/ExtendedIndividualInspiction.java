package com.example.liveala.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.liveala.R;
import com.example.liveala.Utils.Models.IndividualInspection;
import com.google.android.material.snackbar.Snackbar;

public class ExtendedIndividualInspiction extends Fragment {
    IndividualInspection inspection;
    TextView totalScore, floor, beds, dinningHall, disk, bin, clothing, heater, window, spoiltFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_extended_individual_inspiction, container, false);
        inspection = ExtendedIndividualInspictionArgs.fromBundle(getArguments()).getInspection();

        linkUi(root);


        return root;
    }

    @SuppressLint("SetTextI18n")
    private void linkUi(View root) {

        try {


            ((TextView) root.findViewById(R.id.text_inspection_for)).setText("Inspection by " + inspection.getInspector().getName());
            ((TextView) root.findViewById(R.id.text_inspection_date)).setText("On " + android.text.format.DateFormat.format("LLL, dd hh:mm a", inspection.getDate()));
            ((TextView) root.findViewById(R.id.text_total_score)).setText("" + inspection.getTotalScore());

            ((TextView) root.findViewById(R.id.floor)).setText("" + inspection.getScores().get(0).getPoints());
            ((TextView) root.findViewById(R.id.beds)).setText("" + inspection.getScores().get(1).getPoints());
            ((TextView) root.findViewById(R.id.dinning_hall)).setText("" + inspection.getScores().get(2).getPoints());
            ((TextView) root.findViewById(R.id.disk_clean)).setText("" + inspection.getScores().get(3).getPoints());
            ((TextView) root.findViewById(R.id.bin_empty)).setText("" + inspection.getScores().get(4).getPoints());
            ((TextView) root.findViewById(R.id.clothing_lying_around)).setText("" + inspection.getScores().get(5).getPoints());
            ((TextView) root.findViewById(R.id.heater)).setText("" + inspection.getScores().get(6).getPoints());
            ((TextView) root.findViewById(R.id.window_seal)).setText("" + inspection.getScores().get(7).getPoints());
            ((TextView) root.findViewById(R.id.spoilt_food)).setText("" + inspection.getScores().get(8).getPoints());

            if (inspection.getTotalScore() >= 20) {
                (root.findViewById(R.id.cont_total)).setBackgroundColor(getResources().getColor(R.color.greent));
                ((TextView) root.findViewById(R.id.text_total_score)).setTextColor(getResources().getColor(R.color.green));

            } else {
                (root.findViewById(R.id.cont_total)).setBackgroundColor(getResources().getColor(R.color.redt));
                ((TextView) root.findViewById(R.id.text_total_score)).setTextColor(getResources().getColor(R.color.red));

            }
        } catch (Exception ignored) {
            snack(ignored.getMessage());
        }

    }

    public void snack(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

    }

}