package br.com.weis.rest_sqlite_app.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Film {
    private int filmeId;
    private String director;
    private int episode_id;
    private String producer;
    private String release_date;
    private String title;

    public Film(String director, int episode_id, String producer, String release_date, String title) {
        this.director = director;
        this.episode_id = episode_id;
        this.producer = producer;
        this.release_date = release_date;
        this.title = title;
    }

    public Film(int filmeId, String director, int episode_id, String producer, String release_date, String title) {
        this.filmeId = filmeId;
        this.director = director;
        this.episode_id = episode_id;
        this.producer = producer;
        this.release_date = release_date;
        this.title = title;
    }

    public int getFilmeId() {
        return filmeId;
    }

    public void setFilmeId(int filmeId) {
        this.filmeId = filmeId;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(int episode_id) {
        this.episode_id = episode_id;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Star Wars: Episode " + episode_id + " - " + title + "\nProduzido Por: " + producer + "\nDirigido Por: " + director + "\nData de Lançamento: " + release_date;
    }
}
