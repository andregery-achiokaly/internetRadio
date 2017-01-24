package com.somenameofpackage.internetradiowithmosby.view.radioList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.StationsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationsListFragment extends MvpFragment<StationsView, StationsPresenter> implements StationsView {
    private int currentPosition = 0;
    private int oldPosition = 0;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public StationsPresenter createPresenter() {
        return new StationsPresenter(getActivity().getApplicationContext());
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
                        presenter.startPlay(((StationsListAdapter) recyclerView.getAdapter())
                                        .getStationById(position)
                                        .getSource(),
                                getContext());
                        oldPosition = currentPosition;
                        currentPosition = position;
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        presenter.startChangeStation();
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.closeBD();
    }

    @Override
    public void showCurrentStation() {
        recyclerView.getChildAt(currentPosition).setBackgroundColor(Color.RED);
        recyclerView.getChildAt(oldPosition).setBackgroundColor(Color.WHITE);
    }
}
