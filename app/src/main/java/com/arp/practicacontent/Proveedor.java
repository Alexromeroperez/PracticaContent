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
 * Created by Alex on 19/01/2016.
 */
public class Proveedor extends ContentProvider {

    public static final UriMatcher convierteUri2Int;
    public static final int INTERPRETES = 1;
    public static final int INTERPRETE_ID = 2;

    static {
        convierteUri2Int = new UriMatcher(android.content.UriMatcher.NO_MATCH);
        convierteUri2Int.addURI(Contrato.TablaInterprete.AUTHORITY, Contrato.TablaInterprete.TABLA, INTERPRETES);
        convierteUri2Int.addURI(Contrato.TablaInterprete.AUTHORITY, Contrato.TablaInterprete.TABLA+"/#", INTERPRETE_ID);
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
            case INTERPRETES:
                c = db.query(Contrato.TablaInterprete.TABLA, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case INTERPRETE_ID:
                idActividad = ContentUris.parseId(uri);
                c = db.query(Contrato.TablaInterprete.TABLA, projection,
                        Contrato.TablaInterprete._ID + " = ?",
                        new String[]{idActividad + ""}, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(),
                Contrato.TablaInterprete.CONTENT_URI);
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
        if ( match!= INTERPRETES ) {
            throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        if (values == null) {
            throw new IllegalArgumentException("Values null");
        }
        switch (match){
            case INTERPRETE_ID:
                db = adb.getWritableDatabase();
                rowId = db.insert(Contrato.TablaInterprete.TABLA, null, values);

                    uri_actividad = ContentUris.withAppendedId(Contrato.
                            TablaInterprete.CONTENT_URI, rowId);
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
            case INTERPRETES:
                affected = db.delete(Contrato.TablaInterprete.TABLA,
                        selection,
                        selectionArgs);
                break;
            case INTERPRETE_ID:
                idActividad = ContentUris.parseId(uri);
                affected = db.delete(Contrato.TablaInterprete.TABLA,
                        Contrato.TablaInterprete._ID + "= ?", new String[]{idActividad+""});
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
            case INTERPRETES:
                affected = db.update(Contrato.TablaInterprete.TABLA, values,
                        selection, selectionArgs);
                break;
            case INTERPRETE_ID:
                idActividad = uri.getPathSegments().get(1);
                idActividad = uri.getLastPathSegment();
                id = ContentUris.parseId(uri);
                affected = db.update(Contrato.TablaInterprete.TABLA, values,
                        Contrato.TablaInterprete._ID + "= ? ", new String[]{idActividad});
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
            case INTERPRETES:
                return Contrato.TablaInterprete.MULTIPLE_MIME;
            case INTERPRETE_ID:
                return Contrato.TablaInterprete.SINGLE_MIME;
            default:
                throw new IllegalArgumentException(
                        "Tipo de actividad desconocida: " + uri);
        }

    }
}
