package co.com.grupoasd.pokedexdemoasd;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import co.com.grupoasd.pokedexdemoasd.fragments.FavoritosFragment;
import co.com.grupoasd.pokedexdemoasd.persistencia.PokemonDBController;
import co.com.grupoasd.pokedexdemoasd.persistencia.modelo.Favoritos;

public class ContentProviderActivity extends AppCompatActivity {

    RadioButton radioButtonPorID;
    RadioButton radioButtonTodos;
    RadioGroup radioGroup;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    EditText textBuscar;
    ImageButton buttonBuscar;
    ImageButton buttonDelete;
    ProgressDialog progressDialog;
    List<Favoritos> favoritosList;
    FavoritosFragment favoritosFragment;
    private Toolbar appbar;
    PokemonDBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);

        appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = (RadioGroup) findViewById(R.id.radio_criterio);
        radioButtonPorID = (RadioButton) findViewById(R.id.radioButtonPorID);
        radioButtonTodos = (RadioButton) findViewById(R.id.radioButtonTodos);
        radioButtonPorID.setChecked(true);
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
        textBuscar = (EditText) findViewById(R.id.editTextID);
        buttonBuscar = (ImageButton) findViewById(R.id.imageButtonBuscar);
        buttonDelete = (ImageButton) findViewById(R.id.imageButtonDelete);
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionBuscar();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionDelete();
            }
        });
        dbController = new PokemonDBController(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void actionBuscar() {
        if (radioButtonPorID.isChecked()) {
            if (!textBuscar.getText().toString().equals("")) {
                int id = Integer.parseInt(textBuscar.getText().toString());
                if (id > 0) {
                    BuscarAsyncTask buscarAsyncTask = new BuscarAsyncTask();
                    buscarAsyncTask.execute(id + "");
                } else {
                    Toast.makeText(this, "Por favor ingrese un valor mayor a 0", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Por favor ingrese un valor mayor a 0", Toast.LENGTH_LONG).show();
            }
        } else {
            BuscarAsyncTask buscarAsyncTask = new BuscarAsyncTask();
            buscarAsyncTask.execute("");
        }
    }

    private void actionDelete() {
        if (radioButtonPorID.isChecked()) {
            if (!textBuscar.getText().toString().equals("")) {
                int id = Integer.parseInt(textBuscar.getText().toString());
                if (id > 0) {

                } else {
                    Toast.makeText(this, "Por favor ingrese un valor mayor a 0", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Por favor ingrese un valor mayor a 0", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Por favor seleccione busqueda por ID", Toast.LENGTH_LONG).show();
        }
    }

    private class BuscarAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            inicializarProgressDialog();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String id;
            if (params.length > 0 && !TextUtils.isEmpty(params[0])) {
                id = params[0];
                favoritosList = dbController.getFavoritosCP(id);
            } else {
                favoritosList = dbController.getFavoritosCP("");
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dismissProgressDialog();
            if (result) {
                if (!favoritosList.isEmpty()) {
                    fragmentManager = getSupportFragmentManager();
                    if (favoritosFragment != null) {
                        transaction = fragmentManager.beginTransaction();
                        favoritosFragment = new FavoritosFragment();
                        favoritosFragment.setFavoritosList(favoritosList);
                        transaction.replace(R.id.contenedor_fragment, favoritosFragment);
                        transaction.commit();
                    } else {
                        transaction = fragmentManager.beginTransaction();
                        favoritosFragment = new FavoritosFragment();
                        favoritosFragment.setFavoritosList(favoritosList);
                        transaction.add(R.id.contenedor_fragment, favoritosFragment);
                        transaction.commit();
                    }
                }
            }
        }
    }

    private void inicializarProgressDialog() {
        TextView title = new TextView(this);
        title.setText("Buscando pokemon");
        //title.setBackgroundColor(Color.argb(255, 3, 169, 244));
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        progressDialog = new ProgressDialog(this);
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
