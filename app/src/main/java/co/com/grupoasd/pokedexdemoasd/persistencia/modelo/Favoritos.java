package co.com.grupoasd.pokedexdemoasd.persistencia.modelo;

/**
 * Created by ASD on 17/01/2017.
 */

public class Favoritos {

    private String nombre;
    private String urlImage;
    private String urlPokemon;

    public Favoritos(){
    }

    public Favoritos(String nombre, String urlImage, String urlPokemon) {
        this.nombre = nombre;
        this.urlImage = urlImage;
        this.urlPokemon = urlPokemon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlPokemon() {
        return urlPokemon;
    }

    public void setUrlPokemon(String urlPokemon) {
        this.urlPokemon = urlPokemon;
    }
}
