package co.com.grupoasd.pokedexdemoasd.fragments;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import co.com.grupoasd.pokedexdemoasd.R;
import co.com.grupoasd.pokedexdemoasd.adapter.AdapterFavoritosRecycler;
import co.com.grupoasd.pokedexdemoasd.adapter.AdapterPokeRecycler;
import co.com.grupoasd.pokedexdemoasd.object.Pokemon;
import co.com.grupoasd.pokedexdemoasd.persistencia.PokemonDBController;
import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.Favoritos;
import co.com.grupoasd.pokedexdemoasd.service.iface.PokeApiIface;
import co.com.grupoasd.pokedexdemoasd.service.impl.PokeApiImpl;


public class FavoritosFragment extends Fragment {

    private RecyclerView recView;
    List<Favoritos> favoritosList;
    AdapterFavoritosRecycler adaptador;
    SharedPreferences prefs;


    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptador = new AdapterFavoritosRecycler(favoritosList, getContext());
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
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("EBE8C2C7AE936D2CF8E6A42E8C40CDD1").build();
        mAdView.loadAd(adRequest);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        recView.setAdapter(adaptador);
        recView.setHasFixedSize(true);
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

    public void setFavoritosList(List<Favoritos> favoritosList1) {
        this.favoritosList = favoritosList1;
    }
}
