package com.example.snapfind.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.snapfind.MainActivity;
import com.example.snapfind.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
        final Spinner dropDownSpinner = root.findViewById(R.id.spinner);
        final ImageButton prevButton = root.findViewById(R.id.imageButtonPrev);
        final ImageButton nextButton = root.findViewById(R.id.imageButtonNext);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        ArrayList<String> dropDownValues = new ArrayList<>();
        dropDownValues.add("Select Location");
        dropDownValues.add("WAC");
        dropDownValues.add("SLTC");
        dropDownValues.add("WAC");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getParentFragment().getContext(), android.R.layout.simple_list_item_1, dropDownValues);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownSpinner.setAdapter(spinnerAdapter);
        return root;
    }
}