package com.example.liveala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.liveala.Utils.Models.Pref;
import com.example.liveala.Utils.Models.UserProfile;
import com.example.liveala.Activities.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    public static final String SETTINGS_FILE = "IMPORTANT_SETTINGS";
    public static final String INDIVIDUAL_DATE = "INDIVIDUAL_DATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_login);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                direct();
            }
        }, 3000);
    }


    private void direct() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            initLogin();
        } else
            proceedToHome();
    }

    private void initLogin() {
        LinearLayout contLogo = findViewById(R.id.cont_splash);
        CardView cardLogin = findViewById(R.id.card_login);

        Animation moveUp = AnimationUtils.loadAnimation(this, R.anim.move_up);
        Animation zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        zoomOut.setFillEnabled(true);
        zoomOut.setFillAfter(true);
        cardLogin.startAnimation(moveUp);
        cardLogin.setVisibility(View.VISIBLE);
        contLogo.startAnimation(zoomOut);

        Button loginBtn = findViewById(R.id.button_login);
        loginBtn.setOnClickListener(v -> login());

    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        TextInputEditText email = findViewById(R.id.input_email);
        TextInputEditText password = findViewById(R.id.input_password);

        if (!isEmailValid(Objects.requireNonNull(email.getText()).toString())) {
            email.setError("Invalid Email Format");
            return;
        }
        if (password.getText().toString().trim().length() < 5) {
            password.setError("Short Password");
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(android.R.id.content), "Logged in successfully", Snackbar.LENGTH_LONG).show();
                            updateUserProfile();
                        } else {
                            Log.d("LOGIN", task.getException().getMessage());
                            Snackbar.make(findViewById(android.R.id.content), task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Try Again", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            email.requestFocus();
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                    .show();
                        }
                    }
                });

    }

    private void updateUserProfile() {

        FirebaseDatabase.getInstance().getReference("profiles/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile profile = snapshot.getValue(UserProfile.class);

                if (profile != null && profile.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Pref.setValue(MainActivity.this, Pref.USER_PROFILE, new Gson().toJson(profile));
                    startActivity(new Intent(MainActivity.this, Home.class));
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Registering your account was not completed! Contact the developer.", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void proceedToHome() {
        startActivity(new Intent(MainActivity.this, Home.class));
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}