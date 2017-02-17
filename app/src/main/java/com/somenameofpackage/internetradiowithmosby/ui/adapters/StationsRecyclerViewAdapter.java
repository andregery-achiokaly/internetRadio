package com.somenameofpackage.internetradiowithmosby.ui.adapters;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.StationsRecyclerViewFragment;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class StationsRecyclerViewAdapter extends RealmRecyclerViewAdapter<Station, StationsRecyclerViewAdapter.StationViewHolder> {
    StationsRecyclerViewFragment fragment;

    public StationsRecyclerViewAdapter(StationsRecyclerViewFragment fragment, OrderedRealmCollection<Station> data) {
        super(fragment.getContext(), data, true);
        this.fragment = fragment;
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder, int position) {
        Station station = getData().get(position);
        holder.stationNameTextView.setText(station.getName());
        holder.stationSourceTextView.setText(station.getSource());
        holder.station = station;
    }

    class StationViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        final TextView stationNameTextView;
        final TextView stationSourceTextView;

        public Station station;

        StationViewHolder(View view) {
            super(view);
            stationNameTextView = (TextView) itemView.findViewById(R.id.stationName);
            stationSourceTextView = (TextView) itemView.findViewById(R.id.stationSource);
            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            fragment.showDeleteDialog(station.getSource());
            return true;
        }

        @Override
        public void onClick(View v) {
            fragment.changePlayStation(station.getSource());
            v.setBackgroundColor(Color.RED);
        }
    }
}
