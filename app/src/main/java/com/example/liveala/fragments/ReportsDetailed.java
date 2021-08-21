package com.example.liveala.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.liveala.R;

public class ReportsDetailed extends Fragment {

    String hallName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports_detailed, container, false);
        hallName = AddNewInspectionArgs.fromBundle(getArguments()).getHallName();

        return root;
    }
}