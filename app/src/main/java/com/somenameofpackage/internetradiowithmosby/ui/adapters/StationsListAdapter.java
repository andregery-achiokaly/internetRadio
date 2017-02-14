package com.somenameofpackage.internetradiowithmosby.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;

import java.util.List;

public class StationsListAdapter extends RecyclerView.Adapter<StationsListAdapter.StationViewHolder> {
    private List<Station> stations;
    private final LayoutInflater inflater;

    public StationsListAdapter(List<Station> stations, Context context) {
        inflater = LayoutInflater.from(context);
        this.stations = stations;
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
        holder.id = stations.get(position).getId_key();
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }


    public void onChange() {
        notifyDataSetChanged();
    }

    class StationViewHolder extends RecyclerView.ViewHolder {
        final TextView stationNameTextView;
        final TextView stationSourceTextView;
        int id;

        StationViewHolder(final View itemView) {
            super(itemView);
            stationNameTextView = (TextView) itemView.findViewById(R.id.stationName);
            stationSourceTextView = (TextView) itemView.findViewById(R.id.stationSource);
        }
    }

    public Station getStationById(int id) {
        return stations.get(id);
    }
}
