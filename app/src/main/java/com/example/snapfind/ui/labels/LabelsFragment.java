package com.example.snapfind.ui.labels;

import android.content.Context;
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
import com.example.snapfind.StorageHandler.PhotoStorage;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;

public class LabelsFragment extends Fragment {

    private LabelsViewModel labelsViewModel;
    private HashSet<String> labelsList;
    private ListView listViewForLabels;
    private Context context;

    private PhotoStorage photoStorage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoStorage = PhotoStorage.getInstance(getParentFragment().getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        labelsViewModel =
                ViewModelProviders.of(this).get(LabelsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_labels, container, false);
        final ImageButton addLabel = root.findViewById(R.id.imageButton_label_adder);
        final TextInputEditText addLabelInputText = root.findViewById(R.id.add_label_input_text);

        listViewForLabels = root.findViewById(R.id.listView_for_lables);
//        MainActivity.data.put("SLTC", new ArrayList<String>());
//        MainActivity.data.put("MSN", new ArrayList<String>());
//        MainActivity.data.put("MCR", new ArrayList<String>());

        photoStorage.addLabel("SLTC");
        photoStorage.addLabel("MSN");
        labelsList = new HashSet<>();
//        photoStorage.loadData();
        renderListView();

        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputValue = addLabelInputText.getText().toString();
                photoStorage.addLabel(inputValue);
//                MainActivity.data.put(inputValue, new ArrayList<String>());
                renderListView();
            }
        });

        listViewForLabels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getParentFragment().getContext(),"Clicked Item:" + i + " " + labelsList.toArray()[i],Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    public void renderListView(){
//        labelsList.addAll(MainActivity.data.keySet());
        labelsList.addAll(photoStorage.getAllLabels());
        ArrayAdapter listViewAdapter = new ArrayAdapter(getParentFragment().getContext(), android.R.layout.simple_list_item_1, labelsList.toArray());
        listViewForLabels.setAdapter(listViewAdapter);
    }



}