package com.example.liveala.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.liveala.R;
import com.example.liveala.Utils.Models.Pref;
import com.example.liveala.Utils.Models.UserProfile;

import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import static com.example.liveala.Utils.Models.Pref.USER_PROFILE;

public class HomeFragment extends Fragment {
    UserProfile userProfile;
    LinearLayout contCardsInspector, contCardsStudent;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        linkUi(root);

        return root;
    }

    private void directUser(View root) {
        if (userProfile.getUserType() != null)
            switch (userProfile.getUserType()) {
                case "inspector":
                    contCardsInspector.setVisibility(View.VISIBLE);
                    break;
                case "student":
                    contCardsStudent.setVisibility(View.VISIBLE);
                    setupStudentUi(root);
                    break;
            }
    }

    private void setupStudentUi(View root) {
        root.findViewById(R.id.card_my_scores).setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_home_to_studentInspections);
        });
    }

    NavController navController;

    private void linkUi(View root) {
        TextView username = root.findViewById(R.id.text_username);
        TextView email = root.findViewById(R.id.text_user_email);
        LinearLayout contHeader = root.findViewById(R.id.cont_header);
        contCardsInspector = root.findViewById(R.id.cont_cards_inspector);
        contCardsStudent = root.findViewById(R.id.cont_cards_student);
        CircularImageView imageView = root.findViewById(R.id.image_user_profile);
        Animation moveDown = AnimationUtils.loadAnimation(getActivity(), R.anim.move_down);
        Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
        contCardsInspector.startAnimation(fade);

        contHeader.startAnimation(moveDown);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        root.findViewById(R.id.card_new_inspection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_newInspection);
            }
        });
        root.findViewById(R.id.card_prev_inspections).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_previousInspections);
            }
        });

        String json = Pref.getValue(getActivity(), USER_PROFILE, null);
        userProfile = new Gson().fromJson(json, UserProfile.class);

        if (userProfile != null) {
            username.setText(userProfile.getName());
            email.setText(userProfile.getEmail());
            if (userProfile.getImageUrl() != null)
                Glide.with(getActivity()).load(userProfile.getImageUrl()).into(imageView);
        }

        directUser(root);
//        else
//            Snackbar.make(getActivity().getCurrentFocus(), "Error loading data", Snackbar.LENGTH_LONG).show();

        root.findViewById(R.id.card_edit_records).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_editRecords);

            }
        });
    }
}