package com.somenameofpackage.internetradiowithmosby.ui.fragments;

import android.content.DialogInterface;
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
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.presenter.StationsListPresenter;
import com.somenameofpackage.internetradiowithmosby.ui.adapters.StationsRecyclerViewAdapter;
import com.somenameofpackage.internetradiowithmosby.ui.viewStates.ListStationsViewState;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class StationsRecyclerViewFragment extends MvpViewStateFragment<StationsView, StationsListPresenter> implements StationsView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static StationsRecyclerViewFragment newInstance() {
        return new StationsRecyclerViewFragment();
    }

    public StationsRecyclerViewFragment() {
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new ListStationsViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        presenter.getStations();
    }

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    }

    public void showDeleteDialog(String source) {
        DialogInterface.OnClickListener listener = getOnYesClickListener(source);

        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setTitle(R.string.delete);
        adb.setMessage(getString(R.string.do_you_want_to_delete) + source + "?");
        adb.setPositiveButton(R.string.yes, listener);
        adb.setNegativeButton(R.string.no, null);
        adb.create();
        adb.show();
    }

    @NonNull
    private DialogInterface.OnClickListener getOnYesClickListener(String source) {
        return (dialog, which) -> {
            presenter.deleteStation(source);
            Toast.makeText(getContext(), getString(R.string.delete_dialog_start_message)
                            + source
                            + getString(R.string.delete_dialog_end_message),
                    Toast.LENGTH_SHORT).show();
        };
    }


    @Override
    public void setListStations(List<Station> data) {
        ((ListStationsViewState) viewState).setData(data);
        setAdapter(data);
    }

    @Override
    public void setAdapter(List<Station> data) {
        recyclerView.setAdapter(new StationsRecyclerViewAdapter(this, data));
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause(getActivity().getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        presenter.closeBD();
    }
}
