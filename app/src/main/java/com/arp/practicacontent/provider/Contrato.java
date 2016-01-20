package com.arp.practicacontent.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Alex on 19/01/2016.
 */
public class Contrato {
    public Contrato(){}

    public static abstract class TablaDisco implements BaseColumns {
        public static final String TABLA = "disco";
        public static final String NOMBRE = "nombre";
        public static final String INTERPRETE = "interprete";

        public final static String AUTHORITY = "com.arp.practicacontent.ProveedorDisco";
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLA);

        public final static String SINGLE_MIME = "vnd.android.cursor.item/vnd." + AUTHORITY + TABLA;
        public final static String MULTIPLE_MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY + TABLA;


    }
    public static abstract class TablaInterprete implements BaseColumns {
        public static final String TABLA = "interprete";
        public static final String NOMBRE = "nombre";

        public final static String AUTHORITY = "com.arp.practicacontent.Proveedor";
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLA);

        public final static String SINGLE_MIME = "vnd.android.cursor.item/vnd." + AUTHORITY + TABLA;
        public final static String MULTIPLE_MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY + TABLA;

    }
    public static abstract class TablaCancion implements BaseColumns {
        public static final String TABLA = "cancion";
        public static final String TITULO = "titulo";
        public static final String IDDISCO = "iddisco";

        public final static String AUTHORITY = "com.arp.practicacontent.ProveedorCancion";
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLA);

        /*Informa si se le pasa un solo valor o varios valores**/
        public final static String SINGLE_MIME = "vnd.android.cursor.item/vnd." + AUTHORITY + TABLA;
        public final static String MULTIPLE_MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY + TABLA;

    }
}
