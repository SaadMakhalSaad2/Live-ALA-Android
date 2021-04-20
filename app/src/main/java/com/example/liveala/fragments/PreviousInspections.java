package com.example.liveala.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.liveala.R;
import com.example.liveala.Utils.Adapters.MyInspectionsAdapter;
import com.example.liveala.Utils.Adapters.RoomAdapter;
import com.example.liveala.Utils.Models.GeneralInspection;
import com.example.liveala.Utils.Models.IndividualInspection;
import com.example.liveala.Utils.Models.Pref;
import com.example.liveala.Utils.Models.UserProfile;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.liveala.Utils.Models.Pref.USER_PROFILE;

public class PreviousInspections extends Fragment {
    ListView general, rooms;
    ProgressBar progressBarGeneral, progressBarRooms;
    LinearLayout cont;
    UserProfile userProfile;
    List<GeneralInspection> generalInspections = new ArrayList<>();
    List<IndividualInspection> individualInspections = new ArrayList<>();
    TextView noDataGeneral, noDataRooms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_previous_inspections, container, false);

        linUi(root);

        return root;
    }

    private void downloadGeneralInspections() {
        FirebaseDatabase.getInstance().getReference("general_inspections").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    GeneralInspection inspection = snapshot1.getValue(GeneralInspection.class);
                    if (inspection != null
                            && inspection.getInspector() != null
                            && inspection.getInspector().getId() != null
                            && inspection.getInspector().getId().equals(userProfile.getId())) {
                        generalInspections.add(inspection);
                    }
                }
                progressBarGeneral.setVisibility(View.GONE);
                if (!generalInspections.isEmpty()) {
                    MyInspectionsAdapter adp = new MyInspectionsAdapter(getActivity(), R.layout.item_my_inspections, null, (ArrayList<GeneralInspection>) generalInspections, getActivity());
                    general.setAdapter(adp);
                } else {
                    noDataGeneral.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void downloadIndividualsInspections() {
        FirebaseDatabase.getInstance().getReference("individuals_inspections").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    IndividualInspection inspection = snapshot1.getValue(IndividualInspection.class);
                    if (inspection != null
                            && inspection.getInspector() != null
                            && inspection.getInspector().getId() != null
                            && inspection.getInspector().getId().equals(userProfile.getId())) {
                        individualInspections.add(inspection);
                    }
                }
                progressBarRooms.setVisibility(View.GONE);
                if (!individualInspections.isEmpty()) {
                    MyInspectionsAdapter adp = new MyInspectionsAdapter(getActivity(), R.layout.item_my_inspections, (ArrayList<IndividualInspection>) individualInspections, null, getActivity());
                    rooms.setAdapter(adp);
                } else {
                    noDataRooms.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void linUi(View root) {

        general = root.findViewById(R.id.list_common_space_inspections);
        rooms = root.findViewById(R.id.list_rooms_inspections);
        progressBarGeneral = root.findViewById(R.id.progress_load_general_inspections);
        progressBarRooms = root.findViewById(R.id.progress_load_rooms_inspections);
        cont = root.findViewById(R.id.cont_inspections);
        noDataGeneral = root.findViewById(R.id.no_data_general_inspections);
        noDataRooms = root.findViewById(R.id.no_data_rooms_inspections);

        String json = Pref.getValue(getActivity(), USER_PROFILE, null);
        userProfile = new Gson().fromJson(json, UserProfile.class);

        if (userProfile != null) {
            downloadGeneralInspections();
            downloadIndividualsInspections();
        } else
            snack("Unknown error occurred!");

        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                snack("Coming soon");
            }
        });
        general.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                snack("Coming soon");
            }
        });

    }


    public void snack(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

    }

}