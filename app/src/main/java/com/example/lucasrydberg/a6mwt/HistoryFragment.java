package com.example.lucasrydberg.a6mwt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by lucasrydberg on 10/2/17.
 */


public class HistoryFragment extends Fragment {

    private View myFragmentView;
    private ListView listView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_history, container, false);

        listView = myFragmentView.findViewById(R.id.listView);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String sharedPrefString = sharedPref.getString("numSteps", "");

        String[] array = sharedPrefString.split("/:/");

        for(int i =0; i < array.length; i++){
            array[i] = array[i] + " steps";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,array);

        listView.setAdapter(adapter);

        return myFragmentView;
    }

}