package co.com.grupoasd.pokedexdemoasd;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PoIExplorer extends ListActivity {

    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private ArrayAdapter<String> adaptador;
    private String directorioRaiz;
    private TextView carpetaActual;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        carpetaActual = (TextView) findViewById(R.id.rutaActual);

        directorioRaiz = Environment.getExternalStorageDirectory().getPath();

        verArchivosDirectorio(directorioRaiz);
    }

    /**
     * Muestra los archivos del directorio pasado como parametro en un listView
     *
     * @param rutaDirectorio
     */
    private void verArchivosDirectorio(String rutaDirectorio) {
        carpetaActual.setText("Estas en: " + rutaDirectorio);
        listaNombresArchivos = new ArrayList<String>();
        listaRutasArchivos = new ArrayList<String>();
        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        int x = 0;

        // Si no es nuestro directorio raiz creamos un elemento que nos
        // permita volver al directorio padre del directorio actual
        if (!rutaDirectorio.equals(directorioRaiz)) {
            listaNombresArchivos.add("../");
            listaRutasArchivos.add(directorioActual.getParent());
            x = 1;
        }

        // Almacenamos las rutas de todos los archivos y carpetas del directorio
        for (File archivo : listaArchivos) {
            listaRutasArchivos.add(archivo.getPath());
        }

        // Ordenamos la lista de archivos para que se muestren en orden alfabetico
        Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);


        // Recorredos la lista de archivos ordenada para crear la lista de los nombres
        // de los archivos que mostraremos en el listView
        for (int i = x; i < listaRutasArchivos.size(); i++) {
            File archivo = new File(listaRutasArchivos.get(i));
            if (archivo.isFile()) {
                listaNombresArchivos.add(archivo.getName());
            } else {
                listaNombresArchivos.add("/" + archivo.getName());
            }
        }

        // Si no hay ningun archivo en el directorio lo indicamos
        if (listaArchivos.length < 1) {
            listaNombresArchivos.add("No hay ningun archivo");
            listaRutasArchivos.add(rutaDirectorio);
        }


        // Creamos el adaptador y le asignamos la lista de los nombres de los
        // archivos y el layout para los elementos de la lista
        adaptador = new ArrayAdapter<String>(this,
                R.layout.text_view_lista_archivos, listaNombresArchivos);
        setListAdapter(adaptador);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        // Obtenemos la ruta del archivo en el que hemos hecho click en el
        // listView
        File archivo = new File(listaRutasArchivos.get(position));

        // Si es un archivo se muestra un Toast con su nombre y si es un directorio
        // se cargan los archivos que contiene en el listView
        if (archivo.isFile()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Uri uri = Uri.parse(archivo.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String ext = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
                String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
                intent.setDataAndType(Uri.fromFile(new File(uri.getPath())), mime);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Open with"));
            }
        } else {
            // Si no es un directorio mostramos todos los archivos que contiene
            verArchivosDirectorio(listaRutasArchivos.get(position));
        }

    }
}