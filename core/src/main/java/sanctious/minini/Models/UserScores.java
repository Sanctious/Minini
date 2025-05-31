package sanctious.minini.Models;

public class UserScores {
    private int maxSurviveSeconds = 0 ;
    private int totalKills = 0;
    private int totalPoints = 0;

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void modifyTotalPoints(int value){
        this.totalPoints += value;
    }

    public void modifyKills(int amount){
        this.totalKills += amount;
    }

    public int getMaxSurviveSeconds() {
        return maxSurviveSeconds;
    }

    public void setMaxSurviveSeconds(int maxSurviveSeconds) {
        this.maxSurviveSeconds = maxSurviveSeconds;
    }
}
