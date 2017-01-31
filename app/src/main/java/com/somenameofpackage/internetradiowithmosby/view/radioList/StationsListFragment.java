package com.somenameofpackage.internetradiowithmosby.view.radioList;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.StationsListPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                    public void onLongItemClick(View view, final int position) {
                        final String name = ((StationsListAdapter) recyclerView.getAdapter())
                                .getStationById(position)
                                .getName();
                        final String source = ((StationsListAdapter) recyclerView.getAdapter())
                                .getStationById(position)
                                .getSource();

                        showDeleteDialog(name,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        presenter.deleteStation(source);
                                        recyclerView.removeViewAt(position);
                                        Toast.makeText(getContext(), "Station: " + name + " was removed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }));
    }

    private void showDeleteDialog(String name, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setTitle("Delete");
        adb.setMessage("Do you want to delete " + name + "?");
        adb.setPositiveButton("Yes", listener);
        adb.setNegativeButton("No", null);
        adb.create();
        adb.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.closeBD();
    }

    @OnClick(R.id.add_station_btn)
    public void addStation() {
        ((AddStation) getActivity()).createStationDialog("AddStation");
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
                    recyclerView.scrollToPosition(newPosition);
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

    @NonNull
    @Override
    public ViewState createViewState() {
        return new StationsListViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        disableAllStation();
    }

    public void addStationToBD(String name, String source, Bitmap icon) {
        presenter.addStation(name, source, icon);
    }
}
