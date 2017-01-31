package com.somenameofpackage.internetradiowithmosby.ui.fragments.dialogs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.ui.AddStation;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class AddStationDialog extends DialogFragment {
    final private static int RESULT_LOAD_IMAGE = 42;
    @BindView(R.id.station_name)
    EditText stationNameEditText;

    @BindView(R.id.station_source)
    EditText stationSourceEditText;

    @BindView(R.id.station_image)
    ImageView stationImage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Add station");
        View view = inflater.inflate(R.layout.dialog_fragment_add_station, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.save_station_btn)
    void saveStation() {
        String name = stationNameEditText.getText().toString();
        String source = stationSourceEditText.getText().toString();
        Bitmap bitmap = null;
        Drawable drawable = stationImage.getDrawable();
        if (drawable != null) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        if (name.isEmpty() || source.isEmpty() || bitmap == null)
            Toast.makeText(getContext(), R.string.addStationErr_set_name_and_source, Toast.LENGTH_SHORT).show();
        else {
            ((AddStation) getActivity()).addStationToBD(name, source, bitmap);
            dismiss();
        }
    }

    @OnClick(R.id.station_image)
    void getImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                stationImage.setImageBitmap(scaled);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
