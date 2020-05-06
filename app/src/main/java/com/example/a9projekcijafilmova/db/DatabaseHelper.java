package com.example.a9projekcijafilmova.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.a9projekcijafilmova.db.model.Filmovi;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<Filmovi, Integer> moviesDao = null;


    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable( connectionSource, Filmovi.class );
        } catch (SQLException e) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {


        try {
            TableUtils.dropTable( connectionSource, Filmovi.class, true );
        } catch (SQLException e) {
            throw new RuntimeException( e );
        }
    }

    public Dao<Filmovi, Integer> getFilmoviDao() throws SQLException {
        if (moviesDao == null) {
            moviesDao = getDao( Filmovi.class );
        }

        return moviesDao;
    }

    @Override
    public void close() {
        moviesDao = null;

        super.close();
    }
}
