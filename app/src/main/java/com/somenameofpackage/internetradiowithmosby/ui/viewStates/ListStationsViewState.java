package com.somenameofpackage.internetradiowithmosby.ui.viewStates;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

import io.realm.OrderedRealmCollection;

public class ListStationsViewState implements ViewState<StationsView> {
    private OrderedRealmCollection<Station> data;

    @Override
    public void apply(StationsView view, boolean retained) {
        view.setAdapter(data);
    }

    public void setData(OrderedRealmCollection<Station> data) {
        this.data = data;
    }
}
