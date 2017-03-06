package com.somenameofpackage.internetradiowithmosby.ui.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.StationsRecyclerViewFragment;

import java.util.List;

public class StationsRecyclerViewAdapter extends RecyclerView.Adapter<StationsRecyclerViewAdapter.StationViewHolder> {
    private StationsRecyclerViewFragment fragment;
    private List<Station> data;

    public StationsRecyclerViewAdapter(StationsRecyclerViewFragment fragment, List<Station> data) {
        this.fragment = fragment;
        this.data = data;
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_station, viewGroup, false);
        ;
        return new StationViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder, int position) {
        Station station = data.get(position);

        holder.stationNameTextView.setText(station.getName());
        holder.stationSourceTextView.setText(station.getSource());
        holder.station = station;

        if (station.isPlay()) {
            holder.stationNameTextView.setTextColor(Color.RED);
        } else {
            holder.stationNameTextView.setTextColor(Color.BLACK);
        }
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
            Log.v("GGG", station.getId_key()+ " ");
            fragment.getPresenter().stationClick(station);
            notifyDataSetChanged();
        }
    }
}
