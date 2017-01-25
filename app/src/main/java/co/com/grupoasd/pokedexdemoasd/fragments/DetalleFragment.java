package co.com.grupoasd.pokedexdemoasd.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import co.com.grupoasd.pokedexdemoasd.R;
import co.com.grupoasd.pokedexdemoasd.object.Pokemon;
import co.com.grupoasd.pokedexdemoasd.persistencia.PokemonDBController;
import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.Favoritos;


public class DetalleFragment extends Fragment {

    ImageView imageViewFav;
    ImageView imageViewIcono;
    TextView textViewNombre;
    TextView textViewTipo;
    TextView textViewAncho;
    TextView textViewAlto;
    TextView textViewColor;
    TextView textViewForma;
    TextView textViewHabitad;
    Pokemon pokemon;
    PokemonDBController dbController;
    List<Favoritos> favoritosList;

    public DetalleFragment() {
        // Required empty public constructor
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public void setFavoritosList(List<Favoritos> favoritosList) {
        this.favoritosList = favoritosList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poke_detalle, container, false);
        imageViewIcono = (ImageView) view.findViewById(R.id.imageViewPoke);
        textViewNombre = (TextView) view.findViewById(R.id.textViewNombre);
        textViewTipo = (TextView) view.findViewById(R.id.textViewTipo);
        textViewAncho = (TextView) view.findViewById(R.id.textViewAncho);
        textViewAlto = (TextView) view.findViewById(R.id.textViewAlto);
        textViewColor = (TextView) view.findViewById(R.id.textViewColor);
        textViewForma = (TextView) view.findViewById(R.id.textViewForma);
        textViewHabitad = (TextView) view.findViewById(R.id.textViewHabitad);
        imageViewFav = (ImageView) view.findViewById(R.id.imageViewFavorito);
        dbController = new PokemonDBController(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (pokemon.getPokemonDetalle() != null) {
            textViewNombre.setText(pokemon.getNombre().toUpperCase());
            textViewTipo.setText(textViewTipo.getText()+""+getTipos(pokemon.getPokemonDetalle().getType()));
            textViewAlto.setText(textViewAlto.getText()+""+pokemon.getPokemonDetalle().getAlto() + "");
            textViewAncho.setText(textViewAncho.getText()+""+pokemon.getPokemonDetalle().getAncho() + "");
            textViewColor.setText(textViewColor.getText()+""+pokemon.getPokemonDetalle().getPokemonEspecie().getColor() + "");
            textViewForma.setText(textViewForma.getText()+""+pokemon.getPokemonDetalle().getPokemonEspecie().getForma() + "");
            textViewHabitad.setText(textViewHabitad.getText()+""+pokemon.getPokemonDetalle().getPokemonEspecie().getHabitad()+ "");
            Glide.with(getActivity())
                    .load(pokemon.getPokemonDetalle().getFrontDefaultImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .error(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageViewIcono);
            imageViewFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isPokemonFavorito(pokemon.getNombre())){
                        if(dbController.eliminararFavorito(pokemon.getNombre())){
                            eliminarPokemonLista(pokemon.getNombre());
                            imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_favorite));
                            Toast.makeText(getContext(),"Favorito eliminado correctamente", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"Ocurrio un error eliminando favorito", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(dbController.insertarFavoritoContentProvider(pokemon.getNombre(),pokemon.getPokemonDetalle().getFrontDefaultImage(),pokemon.getUrl())){
                            favoritosList.add(new Favoritos(pokemon.getNombre(), pokemon.getPokemonDetalle().getFrontDefaultImage(), pokemon.getUrl()));
                            imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                            Toast.makeText(getContext(),"Pokemon agregado a favoritos correctamente", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"Ocurrio un error guardando favorito", Toast.LENGTH_SHORT).show();
                        }

                        /*if(dbController.insertarFavorito(pokemon.getNombre(),pokemon.getPokemonDetalle().getFrontDefaultImage(),pokemon.getUrl())){
                            favoritosList.add(new Favoritos(pokemon.getNombre(), pokemon.getPokemonDetalle().getFrontDefaultImage(), pokemon.getUrl()));
                            imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                            Toast.makeText(getContext(),"Pokemon agregado a favoritos correctamente", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"Ocurrio un error guardando favorito", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }
            });
            if(isPokemonFavorito(pokemon.getNombre())){
                imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
            }else{
                imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_favorite));
            }
        }
    }

    private boolean isPokemonFavorito(String nombre){
        for (Favoritos favoritos: favoritosList){
            if(favoritos.getNombre().equals(nombre)){
                return true;
            }
        }
        return false;
    }

    private void eliminarPokemonLista(String nombre){
        for (Favoritos favoritos: favoritosList){
            if(favoritos.getNombre().equals(nombre)){
                favoritosList.remove(favoritos);
                return;
            }
        }
    }

    private String getTipos(String[] type) {
        String tipos = "";
        for (int i = 0; i < type.length; i++) {
            tipos += type[i] + ", ";
        }
        return tipos;
    }
}
