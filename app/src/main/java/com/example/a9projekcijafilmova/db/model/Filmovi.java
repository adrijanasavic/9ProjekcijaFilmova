package com.example.a9projekcijafilmova.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Filmovi.TABLE_NAME_USERS)
public class Filmovi {

    public static final String TABLE_NAME_USERS = "movies";

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_NAZIV = "naziv";
    public static final String FIELD_NAME_GODINA = "mGodina";
    public static final String FIELD_NAME_TRAJANJE = "mTrajanje";
    public static final String FIELD_NAME_ZANR = "mZanr";
    public static final String FIELD_NAME_PLOT = "mPlot";
    public static final String FIELD_NAME_JEZIK = "mJezik";
    public static final String FIELD_NAME_IMAGE = "mImage";
    public static final String FIELD_NAME_VREME = "mVreme";
    public static final String FIELD_NAME_CENA = "mCena";
    public static final String FIELD_NAME_IMDBID = "imdbId";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_NAZIV)
    private String mNaziv;

    @DatabaseField(columnName = FIELD_NAME_GODINA)
    private String mGodina;

    @DatabaseField(columnName = FIELD_NAME_TRAJANJE)
    private String mTrajanje;

    @DatabaseField(columnName = FIELD_NAME_ZANR)
    private String mZanr;

    @DatabaseField(columnName = FIELD_NAME_PLOT)
    private String mPlot;

    @DatabaseField(columnName = FIELD_NAME_JEZIK)
    private String mJezik;

    @DatabaseField(columnName = FIELD_NAME_IMAGE)
    private String mImage;

    @DatabaseField(columnName = FIELD_NAME_VREME)
    private String mVreme;

    @DatabaseField(columnName = FIELD_NAME_CENA)
    private String mCena;

    @DatabaseField(columnName = FIELD_NAME_IMDBID)
    private String mImdbId;

    //ORMLite zahteva prazan konstuktur u klasama koje opisuju tabele u bazi!
    public Filmovi() {
    }


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmNaziv() {
        return mNaziv;
    }

    public void setmNaziv(String mNaziv) {
        this.mNaziv = mNaziv;
    }

    public String getmGodina() {
        return mGodina;
    }

    public void setmGodina(String godina) {
        this.mGodina = godina;
    }

    public String getmTrajanje() {
        return mTrajanje;
    }

    public void setmTrajanje(String mTrajanje) {
        this.mTrajanje = mTrajanje;
    }

    public String getmZanr() {
        return mZanr;
    }

    public void setmZanr(String mZanr) {
        this.mZanr = mZanr;
    }

    public String getmPlot() {
        return mPlot;
    }

    public void setmPlot(String mPlot) {
        this.mPlot = mPlot;
    }

    public String getmJezik() {
        return mJezik;
    }

    public void setmJezik(String mJezik) {
        this.mJezik = mJezik;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmVreme() {
        return mVreme;
    }

    public void setmVreme(String mVreme) {
        this.mVreme = mVreme;
    }

    public String getmCena() {
        return mCena;
    }

    public void setmCena(String mCena) {
        this.mCena = mCena;
    }

    public String getmImdbId() {
        return mImdbId;
    }

    public void setmImdbId(String mImdbId) {
        this.mImdbId = mImdbId;
    }
}
