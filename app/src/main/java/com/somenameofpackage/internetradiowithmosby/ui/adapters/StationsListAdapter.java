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
import com.somenameofpackage.internetradiowithmosby.model.db.Station;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class StationsListAdapter extends RecyclerView.Adapter<StationsListAdapter.StationViewHolder> implements RealmChangeListener{
    private final RealmResults<Station> stations;
    private final LayoutInflater inflater;

    public StationsListAdapter(RealmResults<Station> stations, Context context) {
        inflater = LayoutInflater.from(context);
        this.stations = stations;
        stations.addChangeListener(this);
    }

    @Override
    public StationViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StationViewHolder holder, final int position) {
        holder.stationNameTextView.setText(stations.get(position).getName());
        holder.stationSourceTextView.setText(stations.get(position).getSource());
        byte[] bytes = stations.get(position).getImage();
        holder.stationIconImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    @Override
    public int getItemCount() {
        return stations.size();
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

    public Station getStationById(int id) {
        return stations.get(id);
    }
}
