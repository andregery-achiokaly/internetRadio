package com.somenameofpackage.internetradiowithmosby.view.radioList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.StationsListPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationsListFragment extends MvpViewStateFragment<StationsView, StationsListPresenter> implements StationsView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stations_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @NonNull
    @Override
    public StationsListPresenter createPresenter() {
        return new StationsListPresenter(getActivity().getApplicationContext());
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new StationsListAdapter(presenter.getStations(), getActivity().getApplicationContext()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String source = ((StationsListAdapter) recyclerView.getAdapter())
                                .getStationById(position)
                                .getSource();
                        presenter.startPlay(source, getContext());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //change setting of station
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.closeBD();
    }

    @Override
    public void showCurrentStation(final int newPosition) {
        StationsListViewState stationsListViewState = (StationsListViewState) viewState;
        stationsListViewState.setCurrentStation(newPosition);

        disableStations();
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (newPosition < recyclerView.getChildCount() && newPosition != -1) {
                    recyclerView.getChildAt(newPosition).setBackgroundColor(Color.RED);
                }
            }
        });
    }

    @Override
    public void disableAllStation() {
        StationsListViewState stationsListViewState = (StationsListViewState) viewState;
        int disableAllStation = -1;
        stationsListViewState.setCurrentStation(disableAllStation);

        disableStations();
    }

    private void disableStations() {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public ViewState createViewState() {
        return new StationsListViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        disableAllStation();
    }
}
