package io.github.testgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private com.badlogic.gdx.audio.Music gameMusic;
    private Texture image;
    Player player;
    Texture img_bullet;
    Texture img_alien;
    Alien[] aliens;
    int NumWidth_aliens = 11;
    int NumHeight_aliens = 5;
    int spacing_aliens = 43;
    int minX_aliens;
    int minY_aliens;
    int maxX_aliens;
    int maxY_aliens;
    int direction_aliens = 1;
    private int level = 1;
    private int speed_on_death_increment = 0;
    private float BASE_SPEED = 30;
    private float SPEED_INCREASE = 20;
    Vector2 offset_aliens;
    float speed_aliens;
    float speedIncrement;
    private BitmapFont font;
    private GlyphLayout layout;
    private boolean gameOver = false;
    private BitmapFont gameOverFont;
    private GlyphLayout gameOverLayout;
    private float gameOverY;
    private float retryY;
    private float exitY;
    private static final float MAX_ALIEN_SPEED = 250f;
    private static final float Y_DROP_AMOUNT = 0.15f;
    int amount_alive_aliens = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("matt.png");
        img_bullet = new Texture("final_bullet.jpg");
        img_alien = new Texture("this_cropped_bunny.png");
        player = new Player(image, img_bullet, Color.WHITE);
        aliens = new Alien[NumWidth_aliens * NumHeight_aliens];
        offset_aliens = new Vector2(0, 0);
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
        layout = new GlyphLayout();
        gameOverFont = new BitmapFont();
        gameOverFont.setColor(Color.RED);
        gameOverFont.getData().setScale(3);
        gameOverLayout = new GlyphLayout();
        gameOverY = Gdx.graphics.getHeight() * 0.7f;
        retryY = Gdx.graphics.getHeight() * 0.5f;
        exitY = Gdx.graphics.getHeight() * 0.4f;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("necrofantasia.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.2f);
        gameMusic.play();

        speed_aliens = BASE_SPEED;
        speedIncrement = 10;
        speed_on_death_increment = 0;
        level = 1;

        int i = 0;
        for (int y = 0; y < NumHeight_aliens; y++) {
            for (int x = 0; x < NumWidth_aliens; x++) {
                Vector2 position = new Vector2(x * spacing_aliens, y * spacing_aliens);
                position.x += (float) Gdx.graphics.getWidth() / 2;
                position.y += (float) Gdx.graphics.getHeight();
                position.x -= ((float) NumWidth_aliens / 2) * spacing_aliens;
                position.y -= ((float) NumHeight_aliens) * spacing_aliens;
                aliens[i] = new Alien(position, img_alien);
                i++;
            }
        }
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        switch (level) {
            case 1:
                ScreenUtils.clear(255/255f, 192/255f, 203/255f, 1);
                break;
            case 2:
                ScreenUtils.clear(147/255f, 112/255f, 219/255f, 1);
                break;
            case 3:
                ScreenUtils.clear(75/255f, 0/255f, 130/255f, 1);
                break;
            case 4:
                ScreenUtils.clear(238/255f, 130/255f, 238/255f, 1);
                break;
            default:
                ScreenUtils.clear(255/255f, 99/255f, 71/255f, 1);
                break;
        }

        batch.begin();

        if (!gameOver) {
            player.Draw(batch);

            for (int i = 0; i < aliens.length; i++) {
                if (aliens[i].Alive) {
                    if (player.sprite_bullet.getBoundingRectangle().overlaps(aliens[i].getCollisionBounds())) {
                        player.position_bullet.y = 1000000;
                        aliens[i].Alive = false;
                        break;
                    }
                }
            }

            minX_aliens = 54; minY_aliens = 54;
            maxX_aliens = 0;
            maxY_aliens = 0;
            amount_alive_aliens = 0;

            for (int i = 0; i < aliens.length; i++) {
                if (aliens[i].Alive) {
                    int indexX = i % NumWidth_aliens;
                    int indexY = i / NumWidth_aliens;
                    if (indexX > maxX_aliens) maxX_aliens = indexX;
                    if (indexX < minX_aliens) minX_aliens = indexX;
                    if (indexY > maxY_aliens) maxY_aliens = indexY;
                    if (indexY < minY_aliens) minY_aliens = indexY;
                    amount_alive_aliens++;
                }
            }

            if (amount_alive_aliens == 0) {
                level++;
                for (int i = 0; i < aliens.length; i++) {
                    aliens[i].Alive = true;
                }
                offset_aliens = new Vector2(0, 0);
                speed_aliens = Math.min(BASE_SPEED + (SPEED_INCREASE * (level - 1)), MAX_ALIEN_SPEED);
                speedIncrement = Math.min(10 + (level * 2), 20);
                direction_aliens = 1;
            }

            offset_aliens.x += direction_aliens * deltaTime * speed_aliens;
            if (aliens[maxX_aliens].position.x >= Gdx.graphics.getWidth()) {
                direction_aliens = -1;
                offset_aliens.y -= aliens[0].sprite.getHeight() * aliens[0].sprite.getScaleY() * Y_DROP_AMOUNT;
                speed_aliens = Math.min(speed_aliens + speedIncrement / 4, MAX_ALIEN_SPEED);
                speedIncrement *= 0.98f;
            }
            if (aliens[minX_aliens].position.x <= 0) {
                direction_aliens = 1;
                offset_aliens.y -= aliens[0].sprite.getHeight() * aliens[0].sprite.getScaleY() * Y_DROP_AMOUNT;
                speed_aliens = Math.min(speed_aliens + speedIncrement / 4, MAX_ALIEN_SPEED);
                speedIncrement *= 0.98f;
            }
            if (aliens[minY_aliens].position.y <= 0) {
                gameOver = true;
                gameMusic.stop();
            }

            for (int i = 0; i < aliens.length; i++) {
                aliens[i].position = new Vector2(aliens[i].position_initial.x + offset_aliens.x,
                    aliens[i].position_initial.y + offset_aliens.y);
                if (aliens[i].Alive) {
                    aliens[i].Draw(batch);
                    if (aliens[i].getCollisionBounds().overlaps(player.sprite.getBoundingRectangle())) {
                        float playerTop = player.sprite.getBoundingRectangle().y + player.sprite.getBoundingRectangle().height;
                        float playerBottom = player.sprite.getBoundingRectangle().y;
                        float alienTop = aliens[i].getCollisionBounds().y + aliens[i].getCollisionBounds().height;
                        float alienBottom = aliens[i].getCollisionBounds().y;

                        float overlap = 10f;
                        if (Math.abs(playerTop - alienBottom) < overlap ||
                            Math.abs(playerBottom - alienTop) < overlap) {
                            gameOver = true;
                            gameMusic.stop();
                        }
                    }
                }
            }

            String levelText = "Level: " + level;
            layout.setText(font, levelText);
            font.draw(batch, levelText,
                Gdx.graphics.getWidth() - layout.width - 20,
                Gdx.graphics.getHeight() - 20);
        } else {
            String gameOverText = "GAME OVER";
            gameOverLayout.setText(gameOverFont, gameOverText);
            gameOverFont.draw(batch, gameOverText,
                (Gdx.graphics.getWidth() - gameOverLayout.width) / 2,
                gameOverY);

            String retryText = "Press SPACE to try again";
            gameOverLayout.setText(gameOverFont, retryText);
            gameOverFont.draw(batch, retryText,
                (Gdx.graphics.getWidth() - gameOverLayout.width) / 2,
                retryY);

            String exitText = "Press ESC to exit";
            gameOverLayout.setText(gameOverFont, exitText);
            gameOverFont.draw(batch, exitText,
                (Gdx.graphics.getWidth() - gameOverLayout.width) / 2,
                exitY);

            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
                resetGame();
            }
            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
                Gdx.app.exit();
            }
        }

        batch.end();
    }

    private void resetGame() {
        gameOver = false;
        level = 1;
        speed_aliens = BASE_SPEED;
        speedIncrement = 10;
        speed_on_death_increment = 0;
        direction_aliens = 1;
        offset_aliens = new Vector2(0, 0);

        gameMusic.play();

        for (int i = 0; i < aliens.length; i++) {
            aliens[i].Alive = true;
            aliens[i].position = new Vector2(aliens[i].position_initial.x, aliens[i].position_initial.y);
        }

        player.position = new Vector2(
            (float) Gdx.graphics.getWidth() / 2,
            player.sprite.getHeight() / 2
        );
        player.position_bullet = new Vector2(0, 1000000);
    }

    public void setMusicVolume(float volume) {
        gameMusic.setVolume(Math.min(1.0f, Math.max(0.0f, volume)));
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        font.dispose();
        gameOverFont.dispose();
        gameMusic.dispose();
    }
}
