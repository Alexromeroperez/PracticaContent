package com.arp.practicacontent;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.arp.practicacontent.provider.Ayudante;
import com.arp.practicacontent.provider.Contrato;

/**
 * Created by Alex on 20/01/2016.
 */
public class ProveedorCancion extends ContentProvider {

    public static final UriMatcher convierteUri2Int;
    public static final int CANCIONES = 1;
    public static final int CANCION_ID = 2;


    static {
        convierteUri2Int = new UriMatcher(android.content.UriMatcher.NO_MATCH);

        convierteUri2Int.addURI(Contrato.TablaCancion.AUTHORITY, Contrato.TablaCancion.TABLA, CANCIONES);
        convierteUri2Int.addURI(Contrato.TablaCancion.AUTHORITY, Contrato.TablaCancion.TABLA+"/#", CANCION_ID);
    }

    private Ayudante adb;

    @Override
    public boolean onCreate() {
        adb=new Ayudante(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = adb.getReadableDatabase();
        int match = convierteUri2Int.match(uri);
        long idActividad;
        Cursor c;
        switch (match) {
            case CANCIONES:
                c = db.query(Contrato.TablaCancion.TABLA, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case CANCION_ID:
                idActividad = ContentUris.parseId(uri);
                c = db.query(Contrato.TablaCancion.TABLA, projection,
                        Contrato.TablaCancion._ID + " = ?",
                        new String[]{idActividad + ""}, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(),
                Contrato.TablaCancion.CONTENT_URI);

        return c;

    }

    /***Devuelve el tipo mime, dependiendo el tipo de llamada que hayas hecho ****/


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match=convierteUri2Int.match(uri);
        SQLiteDatabase db;
        Uri uri_actividad;
        long rowId;
        if ( match!=CANCIONES) {
            throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        if (values == null) {
            throw new IllegalArgumentException("Values null");
        }
        switch (match){
            case CANCION_ID:
                db = adb.getWritableDatabase();
                rowId = db.insert(Contrato.TablaCancion.TABLA, null, values);

                uri_actividad = ContentUris.withAppendedId(Contrato.
                        TablaCancion.CONTENT_URI, rowId);
                getContext().getContentResolver().notifyChange(uri_actividad, null);
                return uri_actividad;

            default:
                throw new SQLException("Error al insertar fila en : " + uri);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = adb.getWritableDatabase();
        int match = convierteUri2Int.match(uri);
        long idActividad;
        int affected;
        switch (match) {
            case CANCIONES:
                affected = db.delete(Contrato.TablaCancion.TABLA,
                        selection,
                        selectionArgs);
                break;
            case CANCION_ID:
                idActividad = ContentUris.parseId(uri);
                affected = db.delete(Contrato.TablaCancion.TABLA,
                        Contrato.TablaCancion._ID + "= ?", new String[]{idActividad+""});
                // Notificar cambio asociado a la uri
                break;
            default:
                throw new IllegalArgumentException("Elemento actividad desconocido: " +
                        uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = adb.getWritableDatabase();
        String idActividad;
        long id;
        int affected;
        switch (convierteUri2Int.match(uri)) {
            case CANCIONES:
                affected = db.update(Contrato.TablaCancion.TABLA, values,
                        selection, selectionArgs);
                break;
            case CANCION_ID:
                idActividad = uri.getPathSegments().get(1);
                idActividad = uri.getLastPathSegment();
                id = ContentUris.parseId(uri);
                affected = db.update(Contrato.TablaCancion.TABLA, values,
                        Contrato.TablaCancion._ID + "= ? ", new String[]{idActividad});
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (convierteUri2Int.match(uri)) {
            case CANCIONES:
                return Contrato.TablaCancion.MULTIPLE_MIME;
            case CANCION_ID:
                return Contrato.TablaCancion.SINGLE_MIME;
            default:
                throw new IllegalArgumentException(
                        "Tipo de actividad desconocida: " + uri);
        }

    }
}