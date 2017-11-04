package br.com.weis.rest_sqlite_app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.weis.rest_sqlite_app.model.Film;

public class FilmesDb extends SQLiteOpenHelper {
    private static final String BANCO = "starwars";
    private static final int VERSAO = 1;

    public FilmesDb(Context context) {
        super(context, BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql = new StringBuilder();
        sql.append("Create Table Filme ( ");
        sql.append(" filmeId integer primary key autoincrement, ");
        sql.append(" episodeId integer, ");
        sql.append(" title text, ");
        sql.append(" director text, ");
        sql.append(" producer text, ");
        sql.append(" releaseDate text ");
        sql.append(")");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "Drop Table If Exists Filme";

        sqLiteDatabase.execSQL(sql);

        onCreate(sqLiteDatabase);
    }

    public void insert(Film filme){
        ContentValues valores = getContentValues(filme);
        getWritableDatabase().insert("Filme", null, valores);
    }

    public void update(Film filme){
        ContentValues valores = getContentValues(filme);
        getWritableDatabase().update("Filme", valores,
                "filmeId = ?", new String[] { String.valueOf(filme.getFilmeId()) });
    }

    public void delete(int filmeId){
        getWritableDatabase().delete("Filme",
                "filmeId = ?", new String[] { String.valueOf(filmeId) });
    }

    public List<Film> list(){
        Cursor cursor = getReadableDatabase()
                .query("Filme", null, null, null, null, null, null);

        List<Film> filmes = new ArrayList<Film>();

        while (cursor.moveToNext()){
            int filmId = cursor.getInt(0);
            int episode_id = cursor.getInt(1);;
            String title = cursor.getString(2);;
            String director = cursor.getString(3);;
            String producer = cursor.getString(4);
            String release_date = cursor.getString(5);

            filmes.add(new Film(filmId, director, episode_id, producer, release_date, title));
        }

        return filmes;
    }

    private ContentValues getContentValues(Film filme) {
        ContentValues valores = new ContentValues();

        valores.put("episodeId", filme.getEpisode_id());
        valores.put("title", filme.getTitle());
        valores.put("director", filme.getDirector());
        valores.put("producer", filme.getProducer());
        valores.put("releaseDate", String.valueOf(filme.getRelease_date()));

        return valores;
    }
}
