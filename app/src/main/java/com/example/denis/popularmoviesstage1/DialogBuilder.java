package com.example.denis.popularmoviesstage1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Denis on 12/18/2017.
 */

public class DialogBuilder {
    DialogBuilder(Context ctx, String header, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(header)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(ctx.getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
