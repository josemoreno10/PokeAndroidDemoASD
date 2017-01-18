package co.com.grupoasd.pokedexdemoasd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import co.com.grupoasd.pokedexdemoasd.R;
import co.com.grupoasd.pokedexdemoasd.object.Pokemon;
import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.Favoritos;

/**
 * Created by ASD on 28/12/2016.
 */

public class AdapterPokeRecycler extends RecyclerView.Adapter<AdapterPokeRecycler.ItemViewHolder> implements View.OnClickListener{

    private List<Pokemon> datos;
    View.OnClickListener listener;
    private Context context;
    List<Favoritos> favoritosList;

    public AdapterPokeRecycler(List<Pokemon> datos, List<Favoritos> favoritosList, Context context){
        this.datos = datos;
        this.favoritosList = favoritosList;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pokemon, parent, false);
        itemView.setOnClickListener(this);
        ItemViewHolder tvh = new ItemViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Pokemon item = datos.get(position);
        holder.bindItem(item);
        holder.getView().setTag(item);
        Glide.with(context)
                .load(datos.get(position).getPokemonDetalle().getFrontDefaultImage())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.getImageViewPoke());

        if(isPokemonFavorito(datos.get(position).getNombre())){
            holder.getImageViewFavorito().setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
        }else{
            holder.getImageViewFavorito().setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_favorite));
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

    @Override
    public int getItemCount() {
        return datos.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public static class ItemViewHolder
            extends RecyclerView.ViewHolder {

        private ImageView imageViewPoke;
        private TextView textViewNombre;
        private ImageView imageViewFavorito;
        View v;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageViewPoke = (ImageView) itemView.findViewById(R.id.imageViewPoke);
            textViewNombre = (TextView) itemView.findViewById(R.id.textViewNombrePoke);
            imageViewFavorito = (ImageView) itemView.findViewById(R.id.imageViewFavorito);
            v = itemView;
        }

        public ImageView getImageViewPoke(){
            return imageViewPoke;
        }

        public ImageView getImageViewFavorito() {
            return imageViewFavorito;
        }

        public View getView(){
            return v;
        }

        public void bindItem(Pokemon t) {
            textViewNombre.setText(t.getNombre());
        }
    }
}
