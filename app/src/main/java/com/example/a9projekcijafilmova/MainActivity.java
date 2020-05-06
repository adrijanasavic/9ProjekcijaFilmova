package com.example.a9projekcijafilmova;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a9projekcijafilmova.dialog.AboutDialog;
import com.example.a9projekcijafilmova.net.MyService;
import com.example.a9projekcijafilmova.net.model1.Search;
import com.example.a9projekcijafilmova.net.model1.SearchRezult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchAdapter.OnItemClickListener {

    private ImageButton btnSearch;
    private EditText movieName;
    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private SearchAdapter adapter;
    public static String KEY = "KEY";

    private Toolbar toolbar;
    private List<String> drawerItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    private RelativeLayout drawerPane;
    private ActionBarDrawerToggle drawerToggle;

    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        fillData();
        fillDataDrawer();
        setupToolbar();
        setupDrawer();
    }

    public void fillData() {
        btnSearch = findViewById( R.id.btn_search );
        movieName = findViewById( R.id.ime_filma );
        recyclerView = findViewById( R.id.rvLista );

        btnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovieByName( movieName.getText().toString() );
            }
        } );
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
                        title = "Lista filmova";
                        startActivity( new Intent( MainActivity.this, MainActivity.class ) );
                        break;
                    case 1:
                        title = "Settings";
                        startActivity( new Intent( MainActivity.this, SettingsActivity.class ) );
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
        drawerItems.add( "Lista filmova" );
        drawerItems.add( "Settings" );
        drawerItems.add( "About" );

    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitle( "Lista filmova" );
        toolbar.setTitleTextColor( Color.WHITE );

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
        getMenuInflater().inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.omiljeni_lista:
                startActivity( new Intent( this, OmiljeniActivity.class ) );
                break;

            case R.id.settings:
                startActivity( new Intent( this, SettingsActivity.class ) );
                break;
        }

        return super.onOptionsItemSelected( item );
    }
    private void showDialog() {
        if (dialog == null) {
            dialog = new AboutDialog( MainActivity.this ).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();
    }

    private void getMovieByName(String name) {
        Map<String, String> query = new HashMap<>();
        //TODO unesite api key
        query.put( "apikey", "API KEY" );
        query.put( "s", name.trim() );

        Call<SearchRezult> call = MyService.apiInterface().getMovieByName( query );
        call.enqueue( new Callback<SearchRezult>() {
            @Override
            public void onResponse(Call<SearchRezult> call, Response<SearchRezult> response) {

                if (response.code() == 200) {
                    try {
                        SearchRezult searches = response.body();

                        ArrayList<Search> search = new ArrayList<>();

                        for (Search e : searches.getSearch()) {

                            if (e.getType().equals( "movie" )) {
                                search.add( e );
                            }
                        }

                        layoutManager = new LinearLayoutManager( MainActivity.this );
                        recyclerView.setLayoutManager( layoutManager );

                        adapter = new SearchAdapter( MainActivity.this, search, MainActivity.this );
                        recyclerView.setAdapter( adapter );

                        Toast.makeText( MainActivity.this, "Prikaz filmova.", Toast.LENGTH_SHORT ).show();

                    } catch (NullPointerException e) {
                        Toast.makeText( MainActivity.this, "Ne postoji film sa tim nazivom", Toast.LENGTH_SHORT ).show();
                    }

                } else {

                    Toast.makeText( MainActivity.this, "Greska sa serverom", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<SearchRezult> call, Throwable t) {
                Toast.makeText( MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    @Override
    public void onItemClick(int position) {
        Search movie = adapter.get( position );

        Intent i = new Intent( this, DetailsActivity.class );
        i.putExtra( KEY, movie.getImdbID() );
        startActivity( i );
    }
}
