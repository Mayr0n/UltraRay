package xyz.nyroma.gelp;

public class Joke {
    private String title;
    private String joke;

    public Joke(String title, String joke){
        this.title = title;
        this.joke = joke;
        new JokeCache().add(this);
    }

    public String getJoke() {
        return joke;
    }

    public String getTitle() {
        return title;
    }
}
