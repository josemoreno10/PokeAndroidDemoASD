package co.com.grupoasd.pokedexdemoasd.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.FavoritosCampos;

/**
 * Created by ASD on 17/01/2017.
 */

public class FavoritosDao {

    private PokemonSQLiteHelper sqLiteHelper;
    private SQLiteDatabase database;
    private Context context;
    private FavoritosCampos favoritosCampos;

    FavoritosDao(Context context) {
        this.context = context;
        favoritosCampos = new FavoritosCampos();
    }

    FavoritosDao open() throws SQLException {
        sqLiteHelper = new PokemonSQLiteHelper(context, null, 2);
        database = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqLiteHelper.close();
    }

    public long createFavorito(String nombre, String urlImage, String urlPokemon) {
        ContentValues values = createContentValues(nombre, urlImage, urlPokemon);
        return database.insert(favoritosCampos.getDatabaseTable(), null, values);
    }

    public Cursor selectAll() {
        return database.query(favoritosCampos.getDatabaseTable(), new String[]{favoritosCampos.getNombre(),
                        favoritosCampos.getUrlImage(), favoritosCampos.getUrlPokemon()}, null, null, null,
                null, null);
    }

    public long eliminarFavorito(String nombre){
        return database.delete(favoritosCampos.getDatabaseTable(),
                favoritosCampos.getNombre() + " = '" + nombre+"'", null);
    }

    private ContentValues createContentValues(String nombre, String urlImagen, String urlPokemon) {
        ContentValues values = new ContentValues();
        values.put(favoritosCampos.getNombre(), nombre);
        values.put(favoritosCampos.getUrlImage(), urlImagen);
        values.put(favoritosCampos.getUrlPokemon(), urlPokemon);
        return values;
    }
}
