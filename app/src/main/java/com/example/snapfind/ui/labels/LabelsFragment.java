package com.example.snapfind.ui.labels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.snapfind.MainActivity;
import com.example.snapfind.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class LabelsFragment extends Fragment {

    private LabelsViewModel labelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        labelsViewModel =
                ViewModelProviders.of(this).get(LabelsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_labels, container, false);
        final ImageButton addLabel = root.findViewById(R.id.imageButton_label_adder);
        final TextInputEditText addLabelInputText = root.findViewById(R.id.add_label_input_text);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        final ListView listViewForLabels = root.findViewById(R.id.listView_for_lables);
        MainActivity.data.put("SLTC", new ArrayList<String>());
        MainActivity.data.put("MSN", new ArrayList<String>());
        MainActivity.data.put("MCR", new ArrayList<String>());



        final ArrayList<String> labels = new ArrayList<>();
//        labels.add("SLTC");
//        labels.add("MSN");
//        labels.add("MCR");
//        labels.add("SLTC");
//        labels.add("MSN");
//        labels.add("MCR");
//        labels.add("SLTC");
//        labels.add("MSN");
//        labels.add("MCR");
//        labels.add("SLTC");
//        labels.add("MSN");
//        labels.add("MCR");

        ArrayAdapter listViewAdapter = new ArrayAdapter(getParentFragment().getContext(), android.R.layout.simple_list_item_1, MainActivity.data.keySet().toArray());
        listViewForLabels.setAdapter(listViewAdapter);

        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputValue = addLabelInputText.getText().toString();
                MainActivity.data.put(inputValue, new ArrayList<String>());
            }
        });

        listViewForLabels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getParentFragment().getContext(),"Clicked Item:" + i + " " + labels.get(i),Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}