package com.somenameofpackage.internetradiowithmosby.ui.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStation;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class StationsListAdapter extends RecyclerView.Adapter<StationsListAdapter.StationViewHolder> implements RealmChangeListener{
    private final RealmResults<RadioStation> radioStations;
    private final LayoutInflater inflater;

    public StationsListAdapter(RealmResults<RadioStation> radioStations, Context context) {
        inflater = LayoutInflater.from(context);
        this.radioStations = radioStations;
        radioStations.addChangeListener(this);
    }

    @Override
    public StationViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StationViewHolder holder, final int position) {
        holder.stationNameTextView.setText(radioStations.get(position).getName());
        holder.stationSourceTextView.setText(radioStations.get(position).getSource());
        byte[] bytes = radioStations.get(position).getImage();
        holder.stationIconImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    @Override
    public int getItemCount() {
        return radioStations.size();
    }

    @Override
    public void onChange() {
            notifyDataSetChanged();
    }

    class StationViewHolder extends RecyclerView.ViewHolder {
        final TextView stationNameTextView;
        final TextView stationSourceTextView;
        final ImageView stationIconImageView;

        StationViewHolder(final View itemView) {
            super(itemView);
            stationNameTextView = (TextView) itemView.findViewById(R.id.stationName);
            stationSourceTextView = (TextView) itemView.findViewById(R.id.stationSource);
            stationIconImageView = (ImageView) itemView.findViewById(R.id.stationIcon);
        }
    }

    public RadioStation getStationById(int id) {
        return radioStations.get(id);
    }
}
