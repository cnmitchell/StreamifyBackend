package com.streamify.backend.dto;

public class AddMovieRequest
{
    private String content_id;
    private String content_name;
    private String release_date;
    private String IMDB_link;
    private String genre;
    private String poster_url;
    private String sequel_to;

    public AddMovieRequest(){

    }

    public AddMovieRequest(String content_id, String content_name, String release_date,
                    String IMDB_link, String genre, String poster_url, String sequel_to) {
        this.content_id = content_id;
        this.content_name = content_name;
        this.release_date = release_date;
        this.IMDB_link = IMDB_link;
        this.genre = genre;
        this.poster_url = poster_url;
        this.sequel_to = sequel_to;
    }

    public String getContent_id(){return content_id;}
    public  void setContent_id(String content_id){this.content_id = content_id;}
    public String getContent_name(){return content_name;}
    public  void setContent_name(String content_name){this.content_name = content_name;}
    public String getRelease_date(){return release_date;}
    public  void setRelease_date(String release_date){this.release_date = release_date;}
    public String getIMDB_link(){return IMDB_link;}
    public  void setIMDB_link(String IMDB_link){this.IMDB_link = IMDB_link;}
    public String getGenre(){return genre;}
    public  void setGenre(String genre){this.genre = genre;}
    public String getPoster_url(){return poster_url;}
    public  void setPoster_url(String poster_url){this.poster_url = poster_url;}
    public String getSequel_to(){return sequel_to;}
    public  void setSequel_to(String sequel_to){this.sequel_to = sequel_to;}
}
