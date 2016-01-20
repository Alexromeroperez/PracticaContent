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
public class ProveedorDisco extends ContentProvider {

    public static final UriMatcher convierteUri2Int;
    public static final int DISCOS = 1;
    public static final int DISCO_ID = 2;

    static {
        convierteUri2Int = new UriMatcher(android.content.UriMatcher.NO_MATCH);
        convierteUri2Int.addURI(Contrato.TablaDisco.AUTHORITY, Contrato.TablaDisco.TABLA, DISCOS);
        convierteUri2Int.addURI(Contrato.TablaDisco.AUTHORITY, Contrato.TablaDisco.TABLA+"/#", DISCO_ID);
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
            case DISCOS:
                c = db.query(Contrato.TablaDisco.TABLA, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case DISCO_ID:
                idActividad = ContentUris.parseId(uri);
                c = db.query(Contrato.TablaDisco.TABLA, projection,
                        Contrato.TablaDisco._ID + " = ?",
                        new String[]{idActividad + ""}, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
       /* c.setNotificationUri(getContext().getContentResolver(),
                Contrato.TablaCancion.CONTENT_URI);*/

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
        if (match != DISCOS ) {
            throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        if (values == null) {
            throw new IllegalArgumentException("Values null");
        }
        switch (match){
            case DISCO_ID:
                db = adb.getWritableDatabase();
                rowId = db.insert(Contrato.TablaDisco.TABLA, null, values);

                uri_actividad = ContentUris.withAppendedId(Contrato.
                        TablaDisco.CONTENT_URI, rowId);
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
            case DISCOS:
                affected = db.delete(Contrato.TablaDisco.TABLA,
                        selection,
                        selectionArgs);
                break;
            case DISCO_ID:
                idActividad = ContentUris.parseId(uri);
                affected = db.delete(Contrato.TablaDisco.TABLA,
                        Contrato.TablaDisco._ID + "= ?", new String[]{idActividad+""});
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
            case DISCOS:
                affected = db.update(Contrato.TablaDisco.TABLA, values,
                        selection, selectionArgs);
                break;
            case DISCO_ID:
                idActividad = uri.getPathSegments().get(1);
                idActividad = uri.getLastPathSegment();
                id = ContentUris.parseId(uri);
                affected = db.update(Contrato.TablaDisco.TABLA, values,
                        Contrato.TablaDisco._ID + "= ? ", new String[]{idActividad});
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
            case DISCOS:
                return Contrato.TablaDisco.MULTIPLE_MIME;
            case DISCO_ID:
                return Contrato.TablaDisco.SINGLE_MIME;
            default:
                throw new IllegalArgumentException(
                        "Tipo de actividad desconocida: " + uri);
        }

    }
}