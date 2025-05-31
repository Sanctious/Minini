package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import sanctious.minini.GameMain;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreBoardMenuScreen extends ManagedScreen {
    private Stage uiStage;
    private Skin skin;
    private Table scoreDisplayTable;

    private final Color goldColor = new Color(1f, 0.843f, 0f, 1f); // Gold
    private final Color silverColor = new Color(0.753f, 0.753f, 0.753f, 1f); // Silver
    private final Color bronzeColor = new Color(0.804f, 0.498f, 0.196f, 1f); // Bronze
    private final Color currentUserColor = new Color(0.5f, 0.8f, 1f, 1f); // Light Blue for current user if not top 3

    public ScoreBoardMenuScreen() {
        super();
        this.uiStage = new Stage(new ScreenViewport());
        this.skin = GameAPI.getAssetManager().getSkin();
        addInputProcessor(uiStage);
        setupUI();
    }

    public void setupUI() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        rootTable.pad(20f);

        // Test User Data (ensure GameAPI.getUserRegistry() is populated for testing)
        // Example:
         User currentUser = new User("CurrentPlayer", "pass", "current@example.com");
         currentUser.getData().modifyTotalPoints(2500);
         currentUser.getData().modifyKills(8);
         currentUser.getData().setMaxSurviveSeconds(180);
         GameAPI.getUserRegistry().addUser(currentUser);
         GameAPI.getUserRegistry().setActiveUser(currentUser); // Assuming such a method exists

         for(int i = 0; i < 15; i++) {
             User u = new User("Player" + i, "pass", "p"+i+"@example.com");
             u.getData().modifyTotalPoints(com.badlogic.gdx.math.MathUtils.random(500, 5000));
             u.getData().modifyKills(com.badlogic.gdx.math.MathUtils.random(0, 50));
             u.getData().setMaxSurviveSeconds(com.badlogic.gdx.math.MathUtils.random(30, 600));
             GameAPI.getUserRegistry().addUser(u);
         }
        // End Test User Data

        TextButton sortByUsernameButton = new TextButton("Sort: Username", skin);
        sortByUsernameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateScoreBoard("username");
            }
        });

        TextButton sortByScoreButton = new TextButton("Sort: Score", skin);
        sortByScoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateScoreBoard("score");
            }
        });

        TextButton sortByKillsButton = new TextButton("Sort: Kills", skin);
        sortByKillsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateScoreBoard("kills");
            }
        });

        TextButton sortBySurvivalButton = new TextButton("Sort: Survival", skin);
        sortBySurvivalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateScoreBoard("survival");
            }
        });

        TextButton backButton = new TextButton("Back to Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain gameMain = GameMain.getInstance();
                gameMain.changeScreen(
                    new MainMenuScreen(),
                    new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        Table controlsTable = new Table();
        controlsTable.defaults().pad(5f).minWidth(140f); // Adjusted minWidth slightly
        controlsTable.add(sortByUsernameButton);
        controlsTable.add(sortByScoreButton);
        controlsTable.add(sortByKillsButton);
        controlsTable.add(sortBySurvivalButton);

        rootTable.add(controlsTable).padBottom(20f).row();

        scoreDisplayTable = new Table(skin);
        scoreDisplayTable.setBackground(skin.newDrawable("white", new Color(0.2f, 0.2f, 0.2f, 0.8f)));

        rootTable.add(scoreDisplayTable).expand().fill().pad(10f).row();
        rootTable.add(backButton).padTop(20f).minWidth(200f);

        uiStage.addActor(rootTable);
        updateScoreBoard("score"); // Initial sort by score
    }

    public void updateScoreBoard(String sortCriteria) {
        if (scoreDisplayTable == null) return;

        List<User> allUsers = new ArrayList<>(GameAPI.getUserRegistry().getUsers()); // Create a mutable copy for sorting

        switch (sortCriteria.toLowerCase()) {
            case "username":
                allUsers.sort(Comparator.comparing(User::getUsername));
                break;
            case "score":
                allUsers.sort(Comparator.comparingInt((User u) -> u.getData().getTotalPoints()).reversed());
                break;
            case "kills":
                allUsers.sort(Comparator.comparingInt((User u) -> u.getData().getTotalKills()).reversed());
                break;
            case "survival":
                allUsers.sort(Comparator.comparingInt((User u) -> u.getData().getMaxSurviveSeconds()).reversed());
                break;
            default:
                allUsers.sort(Comparator.comparingInt((User u) -> u.getData().getTotalPoints()).reversed()); // Default to score
                break;
        }

        scoreDisplayTable.clearChildren();
        float padAmount = 8f;
        int numberOfColumns = 5; // Rank, Username, Score, Kills, Survival

        scoreDisplayTable.add(new Label("Rank", skin)).pad(padAmount);
        scoreDisplayTable.add(new Label("Username", skin)).pad(padAmount);
        scoreDisplayTable.add(new Label("Score", skin)).pad(padAmount);
        scoreDisplayTable.add(new Label("Kills", skin)).pad(padAmount);
        scoreDisplayTable.add(new Label("Survival (s)", skin)).pad(padAmount);
        scoreDisplayTable.row();

        User activeUser = GameAPI.getUserRegistry().getActiveUser(); // Assumed method

        if (allUsers.isEmpty()) {
            scoreDisplayTable.add(new Label("No scores yet!", skin)).colspan(numberOfColumns).pad(20f).row();
        } else {
            int displayLimit = 10;
            List<User> usersToDisplay = allUsers.subList(0, Math.min(allUsers.size(), displayLimit));

            int rank = 1;
            for (User user : usersToDisplay) {
                boolean isCurrentUser = activeUser != null && activeUser.getUsername().equals(user.getUsername());
                String usernameDisplay = isCurrentUser ? user.getUsername() + " (You)" : user.getUsername();

                Color rowColor = null;
                if (rank == 1) rowColor = goldColor;
                else if (rank == 2) rowColor = silverColor;
                else if (rank == 3) rowColor = bronzeColor;
                else if (isCurrentUser) rowColor = currentUserColor;


                Label rankLabel = new Label(String.valueOf(rank), skin);
                Label usernameLabel = new Label(usernameDisplay, skin);
                Label scoreLabel = new Label(String.valueOf(user.getData().getTotalPoints()), skin);
                Label killsLabel = new Label(String.valueOf(user.getData().getTotalKills()), skin);
                Label survivalLabel = new Label(String.valueOf(user.getData().getMaxSurviveSeconds()), skin);

                if (rowColor != null) {
                    rankLabel.setColor(rowColor);
                    usernameLabel.setColor(rowColor);
                    scoreLabel.setColor(rowColor);
                    killsLabel.setColor(rowColor);
                    survivalLabel.setColor(rowColor);
                }

                scoreDisplayTable.add(rankLabel).pad(padAmount).center();
                scoreDisplayTable.add(usernameLabel).pad(padAmount).left();
                scoreDisplayTable.add(scoreLabel).pad(padAmount).center();
                scoreDisplayTable.add(killsLabel).pad(padAmount).center();
                scoreDisplayTable.add(survivalLabel).pad(padAmount).center();
                scoreDisplayTable.row();
                rank++;
            }

            if (allUsers.size() > displayLimit) {
                scoreDisplayTable.add(new Label("Displaying Top " + displayLimit + " out of " + allUsers.size() + " users", skin))
                    .colspan(numberOfColumns).center().padTop(10f).row();
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.12f, 0.16f, 1f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (uiStage != null) {
            uiStage.dispose();
        }
    }
}
