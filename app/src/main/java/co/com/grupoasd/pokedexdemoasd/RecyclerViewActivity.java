package co.com.grupoasd.pokedexdemoasd;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.com.grupoasd.pokedexdemoasd.adapter.AdaptadorItem;
import co.com.grupoasd.pokedexdemoasd.adapter.AdapterItemRecycler;
import co.com.grupoasd.pokedexdemoasd.object.Item;

public class RecyclerViewActivity extends AppCompatActivity implements View.OnClickListener{

    EditText textBuscar;
    ImageButton buttonBuscar;
    TextView textViewNombre;
    private RecyclerView recView;
    private List<Item> datos;
    SharedPreferences prefs;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        textBuscar = (EditText) findViewById(R.id.editTextID);
        buttonBuscar = (ImageButton) findViewById(R.id.imageButtonBuscar);
        buttonBuscar.setOnClickListener(this);
        textViewNombre = (TextView) findViewById(R.id.textView);
        recView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recView.setHasFixedSize(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fragment, menu);
        setDataMenuDefault(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = prefs.edit();
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, "Se ha seleccionado el boton buscar del menu", Toast.LENGTH_SHORT).show();
            case R.id.grilla:
                String vistaGrilla = prefs.getString("vista", "vacio");
                if(!vistaGrilla.equals("grilla")){
                    editor.putString("vista", "grilla");
                    item.setChecked(true);
                    recView.setLayoutManager(new GridLayoutManager(this,3));
                }
                break;
            case R.id.lista:
                String vistaLista = prefs.getString("vista", "vacio");
                if(!vistaLista.equals("lista")){
                    editor.putString("vista", "lista");
                    item.setChecked(true);
                    recView.setLayoutManager(
                            new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
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
                recView.setLayoutManager(new GridLayoutManager(this,3));
                break;
            case "lista":
                recView.setLayoutManager(
                        new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                break;
            default:
                recView.setLayoutManager(
                        new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if(!textBuscar.getText().toString().equals("")){
            int tamanio = Integer.parseInt(textBuscar.getText().toString());
            datos = getItems(tamanio);
            final AdapterItemRecycler adaptador = new AdapterItemRecycler(datos);
            recView.setAdapter(adaptador);
            setModelLista();
        }else{
            Toast.makeText(this,"Por favor ingrese un valor", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList getItems(int tamanio){
        ArrayList datos = new ArrayList();
        for (int i = 0; i<tamanio;i++){
            datos.add(new Item("Título "+(i+1), "Subtítulo "+(i+1)));
        }
        return datos;
    }
}
