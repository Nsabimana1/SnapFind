package com.example.snapfind.ui.labels.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.snapfind.R;

import java.util.ArrayList;

public class LabelsFragment extends Fragment {

    private LabelsViewModel labelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        labelsViewModel =
                ViewModelProviders.of(this).get(LabelsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_labels, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        final ListView listViewForLabels = root.findViewById(R.id.listView_for_lables);

        final ArrayList<String> labels = new ArrayList<>();
        labels.add("SLTC");
        labels.add("MSN");
        labels.add("MCR");
        labels.add("SLTC");
        labels.add("MSN");
        labels.add("MCR");
        labels.add("SLTC");
        labels.add("MSN");
        labels.add("MCR");
        labels.add("SLTC");
        labels.add("MSN");
        labels.add("MCR");

        ArrayAdapter listViewAdapter = new ArrayAdapter(getParentFragment().getContext(), android.R.layout.simple_list_item_1, labels);
        listViewForLabels.setAdapter(listViewAdapter);

        listViewForLabels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getParentFragment().getContext(),"Clicked Item:" + i + " " + labels.get(i),Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}