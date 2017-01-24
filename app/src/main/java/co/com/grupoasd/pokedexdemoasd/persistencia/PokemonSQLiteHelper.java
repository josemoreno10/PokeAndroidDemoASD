package co.com.grupoasd.pokedexdemoasd.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASD on 17/01/2017.
 */

public class PokemonSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "poke_db";
    String sqlCreate = "CREATE TABLE favoritos (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, urlImage TEXT, urlPokemon TEXT)";

    public PokemonSQLiteHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Se elimina la versión anterior de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS favoritos");
        //Se crea la nueva versión de la tabla
        sqLiteDatabase.execSQL(sqlCreate);
    }
}
