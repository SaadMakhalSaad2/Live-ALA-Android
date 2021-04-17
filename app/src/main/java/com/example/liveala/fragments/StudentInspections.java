package com.example.liveala.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.liveala.R;
import com.example.liveala.Utils.Adapters.StudentInspectionsAdapter;
import com.example.liveala.Utils.Models.IndividualInspection;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentInspections extends Fragment {
    ListView listView;
    ProgressBar progressBar;
    List<IndividualInspection> inspectionList = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
        inspectionList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_inspections, container, false);

        linUi(root);

        downloadInspections(root);
        return root;
    }

    private void linUi(View root) {
        listView = root.findViewById(R.id.list_my_inspections);
        progressBar = root.findViewById(R.id.progress_load_inspections);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openExtendedScoreReport(inspectionList.get(position));
            }
        });
    }

    private void openExtendedScoreReport(IndividualInspection inspection) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        StudentInspectionsDirections.ActionStudentInspectionsToExtendedIndividualInspiction action = StudentInspectionsDirections.actionStudentInspectionsToExtendedIndividualInspiction(inspection);
        action.setInspection(inspection);
        navController.navigate(action);
    }


    private void downloadInspections(View root) {
        FirebaseDatabase.getInstance().getReference("individuals_inspections").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    IndividualInspection inspection = snapshot1.getValue(IndividualInspection.class);
                    if (inspection != null && inspection.getStudent() != null && inspection.getStudent().getId() != null &&
                            inspection.getStudent().getId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        inspectionList.add(inspection);
                    }
                }
                progressBar.setVisibility(View.GONE);
                if (!inspectionList.isEmpty()) {
                    setUpAdapter();
                } else {
                    root.findViewById(R.id.text_no_inspections).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpAdapter() {
        StudentInspectionsAdapter adapter = new StudentInspectionsAdapter(getActivity(), R.layout.item_student_inspection, (ArrayList<IndividualInspection>) inspectionList, getActivity());
        listView.setAdapter(adapter);
    }
    public void snack(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

    }


}