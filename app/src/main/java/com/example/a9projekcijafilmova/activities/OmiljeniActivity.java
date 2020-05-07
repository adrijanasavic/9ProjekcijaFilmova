package com.example.a9projekcijafilmova.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a9projekcijafilmova.R;
import com.example.a9projekcijafilmova.adapters.OmiljeniAdapter;
import com.example.a9projekcijafilmova.db.DatabaseHelper;
import com.example.a9projekcijafilmova.db.model.Filmovi;
import com.example.a9projekcijafilmova.dialog.AboutDialog;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OmiljeniActivity extends AppCompatActivity implements OmiljeniAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private OmiljeniAdapter adapterLista;
    private List<Filmovi> filmovi;
    private SharedPreferences prefs;

    private Toolbar toolbar;
    private List<String> drawerItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    private RelativeLayout drawerPane;
    private ActionBarDrawerToggle drawerToggle;

    private AlertDialog dialog;

    public static String KEY = "KEY";
    public static final String NOTIF_CHANNEL_ID = "notif_channel_007";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_omiljeni );

        fillData();
        fillDataDrawer();
        setupToolbar();
        setupDrawer();
    }
    private void fillData() {
        prefs = PreferenceManager.getDefaultSharedPreferences( this );

        recyclerView = findViewById( R.id.rvRepertoarLista );
        setupToolbar();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        try {

            filmovi = getDataBaseHelper().getFilmoviDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapterLista = new OmiljeniAdapter( this, filmovi, this );
        recyclerView.setAdapter( adapterLista );
    }

    private void setupDrawer() {
        drawerList = findViewById( R.id.left_drawer );
        drawerLayout = findViewById( R.id.drawer_layout );
        drawerPane = findViewById( R.id.drawerPane );

        drawerList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title = "Unknown";
                switch (i) {
                    case 0:
                        title = "Pretraga filmova";
                        startActivity( new Intent( OmiljeniActivity.this, MainActivity.class ) );
                        break;
                    case 1:
                        title = "Settings";
                        startActivity( new Intent( OmiljeniActivity.this, SettingsActivity.class ) );
                        break;
                    case 2:
                        title = "About";
                        showDialog();
                        break;

                }
                drawerList.setItemChecked( i, true );
                setTitle( title );
                drawerLayout.closeDrawer( drawerPane );
            }
        } );
        drawerList.setAdapter( new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, drawerItems ) );


        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
    }

    private void fillDataDrawer() {
        drawerItems = new ArrayList<>();
        drawerItems.add( "Pretraga filmova" );
        drawerItems.add( "Settings" );
        drawerItems.add( "About" );

    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new AboutDialog( OmiljeniActivity.this ).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();
    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "OmiljeniAdapter filmova" );

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.menu );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_omiljeni, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity( new Intent( this, MainActivity.class ) );
                break;

            case R.id.delete_all:
                deleteFilmove();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    public void deleteFilmove() {

        try {
            ArrayList<Filmovi> filmoviZaBrisanje = (ArrayList<Filmovi>) getDataBaseHelper().getFilmoviDao().queryForAll();
            getDataBaseHelper().getFilmoviDao().delete( filmoviZaBrisanje );

            adapterLista.removeAll();
            adapterLista.notifyDataSetChanged();

            String tekstNotifikacije = "OmiljeniAdapter filmovi obrisani";
            boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
            boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

            if (toast) {
                Toast.makeText( OmiljeniActivity.this, tekstNotifikacije, Toast.LENGTH_LONG ).show();
            }

            if (notif) {
                NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                NotificationCompat.Builder builder = new NotificationCompat.Builder( OmiljeniActivity.this, NOTIF_CHANNEL_ID );
                builder.setSmallIcon( android.R.drawable.ic_menu_delete );
                builder.setContentTitle( "Notifikacija" );
                builder.setContentText( tekstNotifikacije );

                Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher );

                builder.setLargeIcon( bitmap );
                notificationManager.notify( 1, builder.build() );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent( this, MainActivity.class );
        startActivity( intent );

    }

    @Override
    public void onItemClick(int position) {
        Filmovi film = adapterLista.get( position );

        Intent i = new Intent( this, DetaljiOmiljeni.class );
        i.putExtra( KEY, film.getmImdbId() );
        i.putExtra( "id", film.getmId() );
        startActivity( i );
    }

    private void refresh() {

        RecyclerView recyclerView = findViewById( R.id.rvRepertoarLista );
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
            recyclerView.setLayoutManager( layoutManager );
            List<Filmovi> film = null;
            try {

                film = getDataBaseHelper().getFilmoviDao().queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            OmiljeniAdapter adapter = new OmiljeniAdapter( this, film, this );
            recyclerView.setAdapter( adapter );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public DatabaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper( this, DatabaseHelper.class );
        }
        return databaseHelper;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}


