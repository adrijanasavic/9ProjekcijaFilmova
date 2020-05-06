package com.example.a9projekcijafilmova.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AboutDialog extends AlertDialog.Builder {


    public AboutDialog(Context context) {
        super( context );
        setTitle( "O aplikaciji" );
        setMessage( "App name: Projekcija filmova 9\nAuthor: Adrijana Savic" );
        setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
        setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
    }

    public AlertDialog prepareDialog() {
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside( false );
        return dialog;
    }
}
