package co.com.grupoasd.pokedexdemoasd.persistencia;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.Favoritos;
import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.FavoritosCampos;

/**
 * Created by ASD on 17/01/2017.
 */

public class PokemonDBController {

    FavoritosDao favoritosDao;
    private FavoritosCampos favoritosCampos;
    Context context;

    public PokemonDBController(Context context) {
        this.context = context;
        favoritosDao = new FavoritosDao(context).open();
        favoritosCampos = new FavoritosCampos();
    }

    public boolean insertarFavorito(String nombre, String urlImage, String urlPokemon) {
        long res = favoritosDao.createFavorito(nombre, urlImage, urlPokemon);
        if (res > 0) {
            return true;
        }
        return false;
    }

    public boolean eliminararFavorito(String nombre) {
        long res = favoritosDao.eliminarFavorito(nombre);
        if (res > 0) {
            return true;
        }
        return false;
    }

    public List<Favoritos> getFavoritos() {
        List<Favoritos> favoritosList = new ArrayList<>();
        Favoritos favoritos;
        Cursor cursor = favoritosDao.selectAll();
        if (cursor.moveToFirst()) {
            do {
                favoritos = new Favoritos();
                favoritos.setNombre(cursor.getString(0));
                favoritos.setUrlImage(cursor.getString(1));
                favoritos.setUrlPokemon(cursor.getString(2));
                favoritosList.add(favoritos);
            } while (cursor.moveToNext());
        }
        return favoritosList;
    }

    public boolean insertarFavoritoContentProvider(String nombre, String urlImagen, String urlPokemon) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = createContentValues(nombre, urlImagen, urlPokemon);
        String uriResult = contentResolver.insert(FavoritosProvider.CONTENT_URI, values).getPath();
        if (uriResult.length() > 0) {
            return true;
        }
        return false;
    }

    private ContentValues createContentValues(String nombre, String urlImagen, String urlPokemon) {
        ContentValues values = new ContentValues();
        values.put(favoritosCampos.getNombre(), nombre);
        values.put(favoritosCampos.getUrlImage(), urlImagen);
        values.put(favoritosCampos.getUrlPokemon(), urlPokemon);
        return values;
    }

    public List<Favoritos> getFavoritosCP(String id) {
        String[] columns = new String[]{
                FavoritosProvider.FavoritosColumns.COL_NOMBRE,
                FavoritosProvider.FavoritosColumns.COL_URL_IMAGE,
                FavoritosProvider.FavoritosColumns.COL_URL_POKEMON,
        };
        Uri favoritosUri;
        if(id.equals("")){
            favoritosUri = FavoritosProvider.CONTENT_URI;
        }else {
            favoritosUri = Uri.parse(FavoritosProvider.CONTENT_URI+"/"+id);
        }
        ContentResolver contentResolver = context.getContentResolver();
        List<Favoritos> favoritosList = new ArrayList<>();
        Favoritos favoritos;
        Cursor cursor = contentResolver.query(favoritosUri
                , columns //Columnas a devolver
                , null //Condición de la query
                , null //Argumentos variables de la query
                , null); //Orden de los resultados
        if (cursor.moveToFirst()) {
            do {
                favoritos = new Favoritos();
                favoritos.setNombre(cursor.getString(0));
                favoritos.setUrlImage(cursor.getString(1));
                favoritos.setUrlPokemon(cursor.getString(2));
                favoritosList.add(favoritos);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoritosList;
    }
}
