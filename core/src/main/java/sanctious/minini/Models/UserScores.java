package sanctious.minini.Models;

public class UserScores {
    private int maxSurviveSeconds = 0 ;
    private int totalKills = 0;
    private int totalPoints = 0;

    public int getTotalPoints() {
        return totalPoints;
    }

    public void modifyTotalPoints(int value){
        this.totalPoints += value;
    }
}
