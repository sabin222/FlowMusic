package com.example.xavin.flowmusic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import static com.example.xavin.flowmusic.FavDatabase.TABLE_NAME;

public class FavDialog extends DialogFragment {
    FavDatabase db;
    Cursor cursor;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.selection).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    /*    Integer deletRows = db.Delete(id);
                        if (deletRows>0)
                        {
                            Toast.makeText(getContext(), "Song Deleted", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }).
                setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), " ", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
