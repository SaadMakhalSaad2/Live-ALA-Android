package com.example.liveala.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.liveala.R;
import com.example.liveala.Utils.Models.GeneralInspection;
import com.example.liveala.Utils.Models.IndividualInspection;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportsDetailed extends Fragment {

    String hallName = "";
    int totalGeneralInspections = 0;
    int totalRoomsInspections = 0;
    int numberOfGeneralInspections = 0;
    int numberOfRoomsInspections = 0;
    Date mDateFrom;
    Date mDateTo = new Date();
    View mRoot;
    ProgressBar progress;
    List<GeneralInspection> generalInspections = new ArrayList<>();
    List<IndividualInspection> roomsInspections = new ArrayList<>();

    LinearLayout scoresCont;
    TextView textNumberOfGeneralInspections, textGeneralInspectionsAverageScore, textNumberOfRoomsInspections, textRoomsInspectionsAverage, total;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports_detailed, container, false);
        hallName = AddNewInspectionArgs.fromBundle(getArguments()).getHallName();
        mRoot = root;
        linkUi();
        return root;
    }

    @SuppressLint("SetTextI18n")
    void linkUi() {
        textNumberOfGeneralInspections = mRoot.findViewById(R.id.general_number_of_inspections);
        textGeneralInspectionsAverageScore = mRoot.findViewById(R.id.general_average_score);
        textNumberOfRoomsInspections = mRoot.findViewById(R.id.rooms_number_of_inpsections);
        textRoomsInspectionsAverage = mRoot.findViewById(R.id.rooms_average_score);
        total = mRoot.findViewById(R.id.total_score);
        scoresCont = mRoot.findViewById(R.id.cont_scores);


        ((TextView) (mRoot.findViewById(R.id.lkfefjek))).setText("Reports for " + hallName);

        ((TextView) (mRoot.findViewById(R.id.date_from))).setOnClickListener((View.OnClickListener) v -> {
            showDatePickerFrom();
        });
        ((TextView) (mRoot.findViewById(R.id.date_to))).setOnClickListener((View.OnClickListener) v -> {
            showDatePickerTo();
        });

        ((TextView) (mRoot.findViewById(R.id.find))).setOnClickListener((View.OnClickListener) v -> {
            if (mDateFrom == null || mDateTo == null) {
                snack("Provide date");
                return;
            }
            downloadGeneralInspections();
        });

        progress = mRoot.findViewById(R.id.progress_load_reports);

    }

    private void downloadGeneralInspections() {
        progress.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("general_inspections").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    GeneralInspection inspection = snapshot1.getValue(GeneralInspection.class);
                    if (inspection != null
                            && inspection.getHall() != null
                            && inspection.getHall().getHallName() != null
                            && inspection.getHall().getHallName().equals(hallName)
                            && inspection.getDate().before(mDateTo)
                            && inspection.getDate().after(mDateFrom)
                    ) {
                        totalGeneralInspections = totalGeneralInspections + inspection.getTotalScore();
                        numberOfGeneralInspections++;
                        generalInspections.add(inspection);
                    }
                }
                Log.d("Reports::", generalInspections.size() + "");

                FirebaseDatabase.getInstance().getReference("individuals_inspections").addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            IndividualInspection inspection = snapshot1.getValue(IndividualInspection.class);
                            if (inspection != null
                                    && inspection.getStudent().getHall() != null
                                    && inspection.getStudent().getHall().equals(hallName)
                                    && inspection.getDate().before(mDateTo)
                                    && inspection.getDate().after(mDateFrom)
                            ) {
                                totalRoomsInspections = totalRoomsInspections + inspection.getTotalScore();
                                numberOfRoomsInspections++;
                                roomsInspections.add(inspection);
                            }
                        }
                        progress.setVisibility(View.GONE);

                        if (!(roomsInspections.isEmpty() && generalInspections.isEmpty())) {
                            int generalAverage = totalGeneralInspections / numberOfGeneralInspections;
                            int roomsAverage = totalRoomsInspections / numberOfRoomsInspections;
                            int totalAverage = (generalAverage + roomsAverage) / 2;

                            textNumberOfGeneralInspections.setText(numberOfGeneralInspections + "");
                            textGeneralInspectionsAverageScore.setText(generalAverage + "");

                            textNumberOfRoomsInspections.setText(numberOfRoomsInspections + "");
                            textRoomsInspectionsAverage.setText(roomsAverage + "");
                            total.setText((totalAverage + ""));
                            scoresCont.setVisibility(View.VISIBLE);
                        }

                    }
//just a comment
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showDatePickerFrom() {
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Report From",
                "OK",
                "Cancel"
        );
        dateTimeDialogFragment.startAtCalendarView();
        dateTimeDialogFragment.set24HoursMode(false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());


        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Date date) {
                mDateFrom = date;
                Log.d("Reports::", mDateFrom.toString());
                ((TextView) (mRoot.findViewById(R.id.date_from))).setText(android.text.format.DateFormat.format("dd MMMM yyyy", mDateFrom));

            }

            @Override
            public void onNegativeButtonClick(Date date) {
            }
        });
        dateTimeDialogFragment.show(

                getFragmentManager(), "dialog_time");
    }

    private void showDatePickerTo() {
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Report To",
                "OK",
                "Cancel"
        );
        dateTimeDialogFragment.startAtCalendarView();
        dateTimeDialogFragment.set24HoursMode(false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());


        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Date date) {
                mDateTo = date;
                Log.d("Reports::", mDateTo.toString());
                ((TextView) (mRoot.findViewById(R.id.date_to))).setText(android.text.format.DateFormat.format("dd MMMM yyyy", mDateTo));

            }

            @Override
            public void onNegativeButtonClick(Date date) {
            }
        });
        dateTimeDialogFragment.show(

                getFragmentManager(), "dialog_time");
    }

    public void snack(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

    }

}