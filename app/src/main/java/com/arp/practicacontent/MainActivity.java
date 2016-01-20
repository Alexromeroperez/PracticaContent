package com.arp.practicacontent;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.arp.practicacontent.provider.Contrato;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private Cursor c,cur;
    private Uri uri;
    private Adaptador ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uri= Contrato.TablaCancion.CONTENT_URI;
        lv=(ListView)findViewById(R.id.listView);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cur.close();
    }

    private void init(){
        cur = getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
        ad=new Adaptador(this,cur);
        lv.setAdapter(ad);
        cur.moveToFirst();
        while(cur.moveToNext()){
            ContentValues cv=new ContentValues();
            cv.put(Contrato.TablaInterprete.NOMBRE, cur.getString(cur.getColumnIndex("artist")));
            getContentResolver().insert(Contrato.TablaInterprete.CONTENT_URI, cv);

            cv=new ContentValues();
            cv.put(Contrato.TablaCancion.TITULO, cur.getString(cur.getColumnIndex("title")));
            cv.put(Contrato.TablaCancion.IDDISCO, cur.getLong(cur.getColumnIndex("album_id")));
            getContentResolver().insert(Contrato.TablaCancion.CONTENT_URI, cv);

            cv=new ContentValues();
            cv.put(Contrato.TablaDisco.NOMBRE, cur.getString(cur.getColumnIndex("album_artist")));
            cv.put(Contrato.TablaDisco.INTERPRETE, cur.getLong(cur.getColumnIndex("artist_id")));
            getContentResolver().insert(Contrato.TablaDisco.CONTENT_URI, cv);
        }
        for (int i = 0; i < cur.getColumnCount(); i++) {
            Log.v("log", i + " " + cur.getColumnName(i));
            Log.v("log", i+" "+cur.getString(i));
        }

    }
}
