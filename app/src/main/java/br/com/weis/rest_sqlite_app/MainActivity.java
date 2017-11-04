package br.com.weis.rest_sqlite_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.weis.rest_sqlite_app.dao.FilmesDb;
import br.com.weis.rest_sqlite_app.model.Film;

public class MainActivity extends AppCompatActivity {
    private ListView lstFilmes;
    List<Film> filmes = new ArrayList<Film>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        if (SQliteItems()){
            carregaListViewSQlite();

            Toast.makeText(this, "Dados carregados do banco local", Toast.LENGTH_LONG).show();
        }
        else {
            ListarTask taks = new ListarTask();
            taks.execute();

            Toast.makeText(this, "Dados carregados do StarWarsAPI", Toast.LENGTH_LONG).show();
        }
    }

    public void sincronizar(View view) {
        if (!filmes.isEmpty() || filmes.size() > 0){
            FilmesDb filmesDb = new FilmesDb(this);

            for (Film filme : filmes){
                filmesDb.insert(filme);
            }

            Toast.makeText(this, "Dados Sincronizados... Carregando dados do banco local", Toast.LENGTH_SHORT).show();
        }

        carregaListViewSQlite();
    }

    private class ListarTask extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "Aguarde", "Buscando Filmes Incriveis");
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://swapi.co/api/films/");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == 200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String linha;
                    StringBuilder builder = new StringBuilder();
                    while ((linha = reader.readLine()) != null){
                        builder.append(linha);
                    }

                    connection.disconnect();

                    return builder.toString();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if (s != null){
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("results");

                    for (int i = 0; i < array.length(); i++){
                        JSONObject filme = (JSONObject) array.get(i);

                        int episode_id = filme.getInt("episode_id");;
                        String title = filme.getString("title");;
                        String director = filme.getString("director");;
                        String producer = filme.getString("producer");
                        String release_date = filme.getString("release_date");

                        filmes.add(new Film(director, episode_id, producer, release_date, title));
                    }

                    ArrayAdapter<Film> adapter = new ArrayAdapter<Film>(MainActivity.this, android.R.layout.simple_list_item_1, filmes);

                    lstFilmes.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(MainActivity.this, "Algo interferiu a Força. Tente mais tarde.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean SQliteItems() {
        FilmesDb filmeDb = new FilmesDb(this);
        return filmeDb.list().size() > 0;
    }

    private void carregaListViewSQlite() {
        FilmesDb filmeDb = new FilmesDb(this);
        List<Film> list = filmeDb.list();

        ArrayAdapter<Film> adapter = new ArrayAdapter<Film>(this, android.R.layout.simple_list_item_1, list);

        lstFilmes.setAdapter(adapter);
    }

    public void setupUI(){
        lstFilmes = (ListView) findViewById(R.id.lstFilmes);
    }
}
