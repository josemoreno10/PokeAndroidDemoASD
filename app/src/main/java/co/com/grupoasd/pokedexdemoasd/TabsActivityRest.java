package co.com.grupoasd.pokedexdemoasd;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.com.grupoasd.pokedexdemoasd.adapter.AdapterPokeRecycler;
import co.com.grupoasd.pokedexdemoasd.object.Pokemon;
import co.com.grupoasd.pokedexdemoasd.object.PokemonResults;
import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.Favoritos;
import co.com.grupoasd.pokedexdemoasd.service.iface.PokeApiIface;
import co.com.grupoasd.pokedexdemoasd.service.impl.PokeApiImpl;

import static co.com.grupoasd.pokedexdemoasd.service.impl.PokeApiImpl.PREFIJO_POKEMON;
import static co.com.grupoasd.pokedexdemoasd.service.modelo.BaseService.PREFIJO_API;

public class TabsActivityRest extends AppCompatActivity {

    EditText textBuscar;
    ImageButton buttonBuscar;
    private RecyclerView recView;
    ProgressDialog progressDialog;
    private PokeApiIface pokeApi;
    RadioButton radioButtonPorID;
    RadioButton radioButtonTodos;
    RadioGroup radioGroup;
    List<Pokemon> pokemons;
    PokemonResults pokemonResults = new PokemonResults();
    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tabs);
        textBuscar = (EditText) findViewById(R.id.editTextID);
        buttonBuscar = (ImageButton) findViewById(R.id.imageButtonBuscar);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuscarAsyncTask buscarAsyncTask = new BuscarAsyncTask();
                buscarAsyncTask.execute(pokemonResults.getUrlNext());
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radio_criterio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButtonPorID.isChecked()) {
                    textBuscar.setEnabled(true);
                } else {
                    textBuscar.setEnabled(false);
                }
            }
        });
        radioButtonPorID = (RadioButton) findViewById(R.id.radioButtonPorID);
        radioButtonTodos = (RadioButton) findViewById(R.id.radioButtonTodos);
        radioButtonPorID.setChecked(true);
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionBuscar();
            }
        });
        recView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recView.setHasFixedSize(true);
        Resources res = getResources();

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("BUSCAR",
                res.getDrawable(android.R.drawable.ic_btn_speak_now));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("TAB2",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Toast.makeText(TabsActivityRest.this, "Pulsada pestaña: " + tabId, Toast.LENGTH_SHORT).show();
            }
        });
        pokeApi = new PokeApiImpl();
    }

    public void actionBuscar() {
        if(radioButtonPorID.isChecked()){
            if (!textBuscar.getText().toString().equals("")) {
                int id = Integer.parseInt(textBuscar.getText().toString());
                if (id > 0) {
                    BuscarAsyncTask buscarAsyncTask = new BuscarAsyncTask();
                    buscarAsyncTask.execute(PREFIJO_API + PREFIJO_POKEMON, id+"");
                } else {
                    Toast.makeText(this, "Por favor ingrese un valor mayor a 0", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Por favor ingrese un valor mayor a 0", Toast.LENGTH_LONG).show();
            }
        }else{
            BuscarAsyncTask buscarAsyncTask = new BuscarAsyncTask();
            buscarAsyncTask.execute();
        }
    }

    private void inicializarProgressDialog() {
        TextView title = new TextView(this);
        title.setText("Buscando pokemons");
        //title.setBackgroundColor(Color.argb(255, 3, 169, 244));
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        progressDialog = new ProgressDialog(TabsActivityRest.this);
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

    private class BuscarAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            inicializarProgressDialog();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String urlApi;
            String id;
            if (params.length > 0 && !TextUtils.isEmpty(params[0])) {
                urlApi = params[0];
                if(!TextUtils.isEmpty(params[1])){
                    id = params[1];
                    pokemonResults = pokeApi.getPokemonsData(urlApi, Integer.parseInt(id));
                }else{
                    pokemonResults = pokeApi.getPokemonsData(urlApi, 0);
                }

            } else {
                pokemonResults = pokeApi.getPokemonsData(PREFIJO_API + PREFIJO_POKEMON, 0);
            }
            pokemons = pokemonResults.getPokemons();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dismissProgressDialog();
            if (result) {
                if(!pokemons.isEmpty()){
                    final AdapterPokeRecycler adaptador = new AdapterPokeRecycler(pokemons, new ArrayList<Favoritos>(), TabsActivityRest.this);
                    recView.setAdapter(adaptador);
                    recView.setLayoutManager(
                            new LinearLayoutManager(TabsActivityRest.this,LinearLayoutManager.VERTICAL,false));
                }
            }
        }
    }
}
