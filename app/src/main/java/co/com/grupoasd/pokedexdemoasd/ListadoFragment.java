package co.com.grupoasd.pokedexdemoasd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import co.com.grupoasd.pokedexdemoasd.adapter.AdapterPokeRecycler;
import co.com.grupoasd.pokedexdemoasd.object.Pokemon;
import co.com.grupoasd.pokedexdemoasd.persistencia.FavoritosDao;
import co.com.grupoasd.pokedexdemoasd.persistencia.PokemonDBController;
import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.Favoritos;
import co.com.grupoasd.pokedexdemoasd.service.iface.PokeApiIface;
import co.com.grupoasd.pokedexdemoasd.service.impl.PokeApiImpl;


public class ListadoFragment extends Fragment {

    private RecyclerView recView;

    ProgressDialog progressDialog;
    private PokeApiIface pokeApi;
    List<Pokemon> pokemons;
    AdapterPokeRecycler adaptador;
    SharedPreferences prefs;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    DetalleFragment detalleFragment;
    PokemonDBController dbController;
    List<Favoritos> favoritosList;

    public ListadoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbController = new PokemonDBController(getContext());
        favoritosList = dbController.getFavoritos();
        adaptador = new AdapterPokeRecycler(pokemons, favoritosList, getContext());
        prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poke, container, false);
        recView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        pokeApi = new PokeApiImpl();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        recView.setAdapter(adaptador);
        recView.setHasFixedSize(true);
        adaptador.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pokemon pokemon = (Pokemon) view.getTag();
                DetallePokeAsyncTask detallePokeAsyncTask = new DetallePokeAsyncTask();
                detallePokeAsyncTask.execute(pokemon);
            }
        });
        setModelLista();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        setDataMenuDefault(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = prefs.edit();
        switch (item.getItemId()) {
            case R.id.grilla:
                String vistaGrilla = prefs.getString("vista", "vacio");
                if(!vistaGrilla.equals("grilla")){
                    editor.putString("vista", "grilla");
                    item.setChecked(true);
                    recView.setLayoutManager(new GridLayoutManager(getContext(),3));
                }
                break;
            case R.id.lista:
                String vistaLista = prefs.getString("vista", "vacio");
                if(!vistaLista.equals("lista")){
                    editor.putString("vista", "lista");
                    item.setChecked(true);
                    recView.setLayoutManager(
                            new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        editor.commit();
        return true;
    }

    private void setDataMenuDefault(Menu menu) {
        String vista = prefs.getString("vista", "vacio");
        switch (vista) {
            case "grilla":
                final MenuItem menuGrilla = menu.findItem(R.id.grilla);
                menuGrilla.setChecked(true);
                break;
            case "lista":
                final MenuItem menuLista = menu.findItem(R.id.lista);
                menuLista.setChecked(true);
                break;
        }
    }

    private void setModelLista() {
        String vista = prefs.getString("vista", "vacio");
        switch (vista) {
            case "grilla":
                recView.setLayoutManager(new GridLayoutManager(getContext(),3));
                break;
            case "lista":
                recView.setLayoutManager(
                        new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                break;
            default:
                recView.setLayoutManager(new GridLayoutManager(getContext(),3));
                break;
        }
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    private class DetallePokeAsyncTask extends AsyncTask<Pokemon, Integer, Pokemon> {

        @Override
        protected void onPreExecute() {
            inicializarProgressDialog();
        }

        @Override
        protected Pokemon doInBackground(Pokemon... pokemons) {
            Pokemon pokemon = pokemons[0];
            if(pokemon.getPokemonDetalle().getPokemonEspecie() == null){
                pokeApi.setPokemonDetalle(pokemon.getUrl(), pokemon.getPokemonDetalle());
            }
            return pokemon;
        }

        @Override
        protected void onPostExecute(Pokemon pokemon) {
            dismissProgressDialog();
            fragmentManager = getActivity().getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            detalleFragment = new DetalleFragment();
            detalleFragment.setPokemon(pokemon);
            detalleFragment.setFavoritosList(favoritosList);
            transaction.replace(R.id.contenedor_fragment, detalleFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void inicializarProgressDialog() {
        TextView title = new TextView(getContext());
        title.setText("Cargando pokemon");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCustomTitle(title);
        progressDialog.setMessage("Por favor espere");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
