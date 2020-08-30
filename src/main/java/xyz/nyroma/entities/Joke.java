package xyz.nyroma.entities;

public class Joke {
    private String title;
    private String joke;

    public Joke(String title, String joke){
        this.title = title;
        this.joke = joke;
    }

    public String getJoke() {
        return joke;
    }

    public String getTitle() {
        return title;
    }
}
