package com.somenameofpackage.internetradiowithmosby.model.db.sqliteDB;


import android.net.Uri;
import android.provider.BaseColumns;

import com.somenameofpackage.internetradiowithmosby.model.db.Station;

public class StationContracts {
    static final String AUTHORITY = "com.somenameofpackage.internetradiowithmosby";

    private StationContracts() {
    }

    public static final class Stations implements BaseColumns {
        private Stations() {
        }

        static final String TABLE_NAME = Station.TABLE_NAME;
        private static final String SCHEME = "content://";
        private static final String PATH_STATIONS = "/stations";
        private static final String PATH_STATIONS_ID = "/stations/";
        static final int STATIONS_ID_PATH_POSITION = 0;
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_STATIONS);
        static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_STATIONS_ID);
        static final String CONTENT_TYPE = "com.somenameofpackage.internetradiowithmosby.cursor.dir/com.somenameofpackage.internetradiowithmosby.stations";
        static final String CONTENT_ITEM_TYPE = "com.somenameofpackage.internetradiowithmosby.cursor.item/com.somenameofpackage.internetradiowithmosby.students";
        static final String DEFAULT_SORT_ORDER = Station.STATION_ID_KEY +" ASC";
        public  static final String COLUMN_NAME_ID = Station.STATION_ID_KEY;
        public static final String COLUMN_NAME_NAME = Station.STATION_NAME;
        public static final String COLUMN_NAME_SOURCE = Station.STATION_SOURCE;
        public static final String COLUMN_NAME_IS_PLAY = Station.STATION_IS_PLAY;
        public static final String[] DEFAULT_PROJECTION = new String[]{
                COLUMN_NAME_ID,
                COLUMN_NAME_NAME,
                COLUMN_NAME_SOURCE,
                COLUMN_NAME_IS_PLAY
        };
    }
}
