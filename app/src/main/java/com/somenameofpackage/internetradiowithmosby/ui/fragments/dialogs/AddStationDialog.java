package com.somenameofpackage.internetradiowithmosby.ui.fragments.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.ui.AddStation;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddStationDialog extends DialogFragment {
    @BindView(R.id.station_name)
    EditText stationNameEditText;

    @BindView(R.id.station_source)
    EditText stationSourceEditText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.add_station_dialog_title);
        View view = inflater.inflate(R.layout.dialog_fragment_add_station, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.save_station_btn)
    void saveStation() {
        String name = stationNameEditText.getText().toString();
        String source = stationSourceEditText.getText().toString();

        if (name.isEmpty() || source.isEmpty())
            Toast.makeText(getContext(), R.string.addStationErr_set_name_and_source, Toast.LENGTH_SHORT).show();
        else {
            ((AddStation) getActivity()).addStationToBD(name, source);
            dismiss();
        }
    }
}
