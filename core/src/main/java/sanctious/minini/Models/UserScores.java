package sanctious.minini.Models;

public class UserScores {
    private int maxSurviveSeconds;
    private int totalKills;
    private int totalPoints;

    public int getTotalPoints() {
        return totalPoints;
    }

    public void modifyTotalPoints(int value){
        this.totalPoints += value;
    }
}
