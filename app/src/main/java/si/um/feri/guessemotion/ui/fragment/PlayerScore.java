package si.um.feri.guessemotion.ui.fragment;

public class PlayerScore {
    private String name;
    private int score;
    private String country;

    public PlayerScore(String _name, int _score){
        name = _name;
        score = _score;
        country = null;
    }

    public PlayerScore(String _name, int _score, String _country){
        name = _name;
        score = _score;
        country = _country;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
