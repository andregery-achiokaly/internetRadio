package com.somenameofpackage.internetradiowithmosby.model.db.sqliteDB;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.HashMap;

public class StationsProvider extends ContentProvider {
    private static final HashMap<String, String> stationsProjectionMap;
    private static final int STATIONS = 1;
    private static final int STATIONS_ID = 2;
    private static final UriMatcher uriMatcher;
    private SqliteDBHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(StationContracts.AUTHORITY, "stations", STATIONS);
        uriMatcher.addURI(StationContracts.AUTHORITY, "stations/#", STATIONS_ID);
        stationsProjectionMap = new HashMap<>();
        for (int i = 0; i < StationContracts.Stations.DEFAULT_PROJECTION.length; i++) {
            stationsProjectionMap.put(
                    StationContracts.Stations.DEFAULT_PROJECTION[i],
                    StationContracts.Stations.DEFAULT_PROJECTION[i]);
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String finalWhere;
        int count;
        switch (uriMatcher.match(uri)) {
            case STATIONS:
                count = db.delete(StationContracts.Stations.TABLE_NAME, where, whereArgs);
                break;
            case STATIONS_ID:
                finalWhere = StationContracts.Stations.COLUMN_NAME_ID + " = " + uri.getPathSegments().get(StationContracts.Stations.STATIONS_ID_PATH_POSITION);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.delete(StationContracts.Stations.TABLE_NAME, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case STATIONS:
                return StationContracts.Stations.CONTENT_TYPE;
            case STATIONS_ID:
                return StationContracts.Stations.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (uriMatcher.match(uri) != STATIONS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        long rowId = -1;
        Uri rowUri = Uri.EMPTY;
        switch (uriMatcher.match(uri)) {
            case STATIONS:
                if (!values.containsKey(StationContracts.Stations.COLUMN_NAME_NAME)) {
                    values.put(StationContracts.Stations.COLUMN_NAME_NAME, "");
                }
                if (!values.containsKey(StationContracts.Stations.COLUMN_NAME_SOURCE)) {
                    values.put(StationContracts.Stations.COLUMN_NAME_SOURCE, "");
                }
                if (!values.containsKey(StationContracts.Stations.COLUMN_NAME_IS_PLAY)) {
                    values.put(StationContracts.Stations.COLUMN_NAME_IS_PLAY, 0.0);
                }
                rowId = db.insert(StationContracts.Stations.TABLE_NAME,
                        StationContracts.Stations.COLUMN_NAME_NAME,
                        values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(StationContracts.Stations.CONTENT_ID_URI_BASE, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
        }
        return rowUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new SqliteDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String orderBy = null;
        switch (uriMatcher.match(uri)) {
            case STATIONS:
                qb.setTables(StationContracts.Stations.TABLE_NAME);
                qb.setProjectionMap(stationsProjectionMap);
                orderBy = StationContracts.Stations.DEFAULT_SORT_ORDER;
                break;
            case STATIONS_ID:
                qb.setTables(StationContracts.Stations.TABLE_NAME);
                qb.setProjectionMap(stationsProjectionMap);
                qb.appendWhere(StationContracts.Stations.COLUMN_NAME_ID + "=" + uri.getPathSegments().get(StationContracts.Stations.STATIONS_ID_PATH_POSITION));
                orderBy = StationContracts.Stations.DEFAULT_SORT_ORDER;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        String finalWhere;
        String id;
        switch (uriMatcher.match(uri)) {
            case STATIONS:
                count = db.update(StationContracts.Stations.TABLE_NAME, values, where, whereArgs);
                break;
            case STATIONS_ID:
                id = uri.getPathSegments().get(StationContracts.Stations.STATIONS_ID_PATH_POSITION);
                finalWhere = StationContracts.Stations._ID + " = " + id;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(StationContracts.Stations.TABLE_NAME, values, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
