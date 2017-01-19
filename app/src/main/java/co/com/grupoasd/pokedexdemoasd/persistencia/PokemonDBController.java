package co.com.grupoasd.pokedexdemoasd.persistencia;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.Favoritos;

/**
 * Created by ASD on 17/01/2017.
 */

public class PokemonDBController {

    FavoritosDao favoritosDao;

    public PokemonDBController(Context context) {
        favoritosDao = new FavoritosDao(context).open();
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

    public List<Favoritos> getFavoritos(){
        List<Favoritos> favoritosList = new ArrayList<>();
        Favoritos favoritos;
        Cursor cursor = favoritosDao.selectAll();
        if(cursor.moveToFirst()){
            do{
                favoritos = new Favoritos();
                favoritos.setNombre(cursor.getString(0));
                favoritos.setUrlImage(cursor.getString(1));
                favoritos.setUrlPokemon(cursor.getString(2));
                favoritosList.add(favoritos);
            }while (cursor.moveToNext());
        }
        return favoritosList;
    }
}
