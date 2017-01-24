package co.com.grupoasd.pokedexdemoasd.persistencia;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

/**
 * Created by ASD on 24/01/2017.
 */

public class FavoritosProvider extends ContentProvider {

    private static final String uri = "content://co.com.grupoasd.pokedexdemoasd.contentproviders/favoritos";
    public static final Uri CONTENT_URI = Uri.parse(uri);
    private PokemonSQLiteHelper pokemonSQLiteHelper;
    private static final int BD_VERSION = 1;
    private static final String TABLA_FAVORITOS = "favoritos";
    private static final int FAVORITOS = 1;
    private static final int FAVORITOS_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("co.com.grupoasd.pokedexdemoasd.contentproviders", "favoritos", FAVORITOS);
        uriMatcher.addURI("co.com.grupoasd.pokedexdemoasd.contentproviders", "favoritos/#", FAVORITOS_ID);
    }

    @Override
    public boolean onCreate() {
        pokemonSQLiteHelper = new PokemonSQLiteHelper(getContext(), null, BD_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == FAVORITOS_ID){
            where = "_id=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = pokemonSQLiteHelper.getWritableDatabase();
        Cursor c = db.query(TABLA_FAVORITOS, projection, where, selectionArgs, null, null, sortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match)
        {
            case FAVORITOS:
                return "vnd.android.cursor.dir/vnd.grupoasd.favoritos";
            case FAVORITOS_ID:
                return "vnd.android.cursor.item/vnd.grupoasd.favoritos";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;
        SQLiteDatabase db = pokemonSQLiteHelper.getWritableDatabase();
        regId = db.insert(TABLA_FAVORITOS, null, values);
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cont;
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == FAVORITOS_ID){
            where = "_id=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = pokemonSQLiteHelper.getWritableDatabase();
        cont = db.delete(TABLA_FAVORITOS, where, selectionArgs);

        return cont;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cont;
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == FAVORITOS_ID){
            where = "_id=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = pokemonSQLiteHelper.getWritableDatabase();
        cont = db.update(TABLA_FAVORITOS, values, where, selectionArgs);

        return cont;
    }

    public static final class FavoritosColumns implements BaseColumns {
        private FavoritosColumns() {
        }

        //Nombres de columnas
        public static final String COL_NOMBRE = "nombre";
        public static final String COL_URL_IMAGE = "urlImage";
        public static final String COL_URL_POKEMON = "urlPokemon";
    }
}
