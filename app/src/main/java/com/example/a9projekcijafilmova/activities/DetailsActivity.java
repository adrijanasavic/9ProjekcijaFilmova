package com.example.a9projekcijafilmova.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.a9projekcijafilmova.R;
import com.example.a9projekcijafilmova.db.DatabaseHelper;
import com.example.a9projekcijafilmova.db.model.Filmovi;
import com.example.a9projekcijafilmova.net.MyService;
import com.example.a9projekcijafilmova.net.model2.Detail;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private Detail detalji;
    private DatabaseHelper databaseHelper;
    private SharedPreferences prefs;
    public static final String NOTIF_CHANNEL_ID = "notif_channel_007";

    private Toolbar toolbar;

    private ImageView slika;
    private TextView naslov;
    private TextView godina;
    private TextView glumac;
    private RatingBar rating_star;
    private TextView rating;
    private TextView vreme;
    private TextView zanr;
    private TextView jezik;
    private TextView opis;
    private TextView drzava;
    private TextView nagrada;
    private TextView rezija;
    private TextView budzet;


    private TimePicker vremePicker;
    private EditText cenaEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details );

        setupToolbar();
        createNotificationChannel();

        prefs = PreferenceManager.getDefaultSharedPreferences( this );

    }


    private void getDetail(String imdbKey) {
        HashMap<String, String> queryParams = new HashMap<>();
        //TODO unesi api key
        queryParams.put( "apikey", "API KEY" );
        queryParams.put( "i", imdbKey );

        Call<Detail> call = MyService.apiInterface().getMovieData( queryParams );
        call.enqueue( new Callback<Detail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.code() == 200) {
                    Log.d( "REZ", "200" );

                    detalji = response.body();
                    if (detalji != null) {
                        fillData( detalji );
                        add();

                    }
                }
            }

            private void fillData(Detail detalji) {
                slika = findViewById( R.id.detalji_slika );
                Picasso.with( DetailsActivity.this ).load( detalji.getPoster() ).into( slika );

                naslov = findViewById( R.id.detalji_naziv );
                naslov.setText( detalji.getTitle() );

                godina = findViewById( R.id.detalji_godina );
                godina.setText( "(" + detalji.getYear() + ")" );

                glumac = findViewById( R.id.detalji_glumac );
                glumac.setText( detalji.getActors() );

                rating_star = findViewById( R.id.detalji_ocenaZ );
                rating_star.setRating( Float.valueOf( detalji.getImdbRating() ) );

//                rating = findViewById(R.id.detalji_ocenaB);
//                rating.setText( detalji.getRated() );

                vreme = findViewById( R.id.detalji_vreme );
                vreme.setText( detalji.getRuntime() );

                zanr = findViewById( R.id.detalji_zanr );
                zanr.setText( detalji.getGenre() );

                jezik = findViewById( R.id.detalji_jezik );
                jezik.setText( detalji.getLanguage() );

                opis = findViewById( R.id.detalji_opis );
                opis.setText( detalji.getPlot() );

                drzava = findViewById( R.id.detalji_drzava );
                drzava.setText( detalji.getCountry() );

                nagrada = findViewById( R.id.detalji_nagrada );
                nagrada.setText( detalji.getAwards() );

                rezija = findViewById( R.id.detalji_rezija );
                rezija.setText( detalji.getDirector() );

                budzet = findViewById( R.id.detalji_budzet );
                budzet.setText( detalji.getBoxOffice() );

                vremePicker = findViewById( R.id.details_picker );
                cenaEdit = findViewById( R.id.details_cena );


            }

            public void add(){
                ImageButton add = findViewById( R.id.add );
                add.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFilm();
                    }
                } );
            }
            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText( DetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "Detalji filmova" );
        //toolbar.setLogo( R.drawable.heart_white );


        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.back );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_detalji, menu );
        return super.onCreateOptionsMenu( menu );
    }


    @Override
    protected void onResume() {
        super.onResume();

        String imdbKey = getIntent().getStringExtra( MainActivity.KEY );
        getDetail( imdbKey );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_film:
                addFilm();
                break;
            case android.R.id.home:
                startActivity( new Intent( this, MainActivity.class ) );
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    public void addFilm() {
        if (cenaEdit.getText().toString().isEmpty()) {
            Toast.makeText( this, "Morate upisati cenu karte", Toast.LENGTH_LONG ).show();

        } else {
            Filmovi film = new Filmovi();
            film.setmNaziv( detalji.getTitle() );
            film.setmGodina( detalji.getYear() );
            film.setmImage( detalji.getPoster() );
            film.setmImdbId( detalji.getImdbID() );

            String vreme = vremePicker.getCurrentHour() + ":" + vremePicker.getCurrentMinute() + "h";
            String cena = cenaEdit.getText().toString() + "din";

            film.setmCena( cena );
            film.setmVreme( vreme );

            try {
                getDataBaseHelper().getFilmoviDao().create( film );

                String tekstNotifikacije = film.getmNaziv() + " je uspesno dodat na repertoar omiljenih filmova!";

                boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
                boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );


                if (toast) {
                    Toast.makeText( DetailsActivity.this, tekstNotifikacije, Toast.LENGTH_LONG ).show();

                }

                if (notif) {
                    showNotification( tekstNotifikacije );

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            startActivity( new Intent( this, OmiljeniActivity.class ) );
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

    public void showNotification(String poruka) {

        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        NotificationCompat.Builder builder = new NotificationCompat.Builder( DetailsActivity.this, NOTIF_CHANNEL_ID );
        builder.setSmallIcon( R.drawable.heart );
        builder.setContentTitle( "Notifikacija" );
        builder.setContentText( poruka );

        Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher );

        builder.setLargeIcon( bitmap );
        notificationManager.notify( 1, builder.build() );
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Description of My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( NOTIF_CHANNEL_ID, name, importance );
            channel.setDescription( description );

            NotificationManager notificationManager = getSystemService( NotificationManager.class );
            notificationManager.createNotificationChannel( channel );
        }
    }
}




