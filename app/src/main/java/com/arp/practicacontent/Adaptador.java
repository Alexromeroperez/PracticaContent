package com.arp.practicacontent;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Alex on 19/01/2016.
 */
public class Adaptador extends CursorAdapter {
    private View v;
    private LayoutInflater i;
    public Adaptador(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        i = LayoutInflater.from(parent.getContext());
        v = i.inflate(R.layout.lista_detalle, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvNombre,tvDisco,tvAutor;
        tvNombre=(TextView) v.findViewById(R.id.tvNombre);
        tvDisco=(TextView) v.findViewById(R.id.tvDisco);
        tvAutor=(TextView) v.findViewById(R.id.tvAutor);
        tvNombre.setText(cursor.getString(cursor.getColumnIndex("title")));
        tvDisco.setText(cursor.getString(cursor.getColumnIndex("album_artist")));
        tvAutor.setText(cursor.getString(cursor.getColumnIndex("artist")));
    }
}
