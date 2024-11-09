package io.github.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
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
    int direction_aliens = 3;
    Vector2 offset_aliens;
    float speed_aliens;
    float speedIncrement;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("matt.png");
        img_bullet = new Texture("final_bullet.jpg");
        img_alien = new Texture("bunny_fixed.png");
        player = new Player(image, img_bullet, Color.WHITE);
        aliens = new Alien[NumWidth_aliens * NumHeight_aliens];
        offset_aliens = new Vector2(0, 0);

        speed_aliens = 30;
        speedIncrement = 10;

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

    int amount_alive_aliens = 0;

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(255/255f, 192/255f, 203/255f, 1);
        batch.begin();
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
            for (int i = 0; i < aliens.length; i++) {
                aliens[i].Alive = true;
            }
            offset_aliens = new Vector2(0, 0);
            speed_aliens = 30;  // Reset to initial speed
            speedIncrement = 10;
        }

        offset_aliens.x += direction_aliens * deltaTime * speed_aliens;
        if (aliens[maxX_aliens].position.x >= Gdx.graphics.getWidth()) {
            direction_aliens = -1;
            offset_aliens.y -= aliens[0].sprite.getHeight() * aliens[0].sprite.getScaleY() * 0.1f;
            speed_aliens += speedIncrement;
            speedIncrement *= 0.98f;
        }
        if (aliens[minX_aliens].position.x <= 0) {
            direction_aliens = 1;
            offset_aliens.y -= aliens[0].sprite.getHeight() * aliens[0].sprite.getScaleY() * 0.1f;
            speed_aliens += speedIncrement;
            speedIncrement *= 0.98f;
        }
        if (aliens[minY_aliens].position.y <= 0) {
            Gdx.app.exit();
        }

        for (int i = 0; i < aliens.length; i++) {
            aliens[i].position = new Vector2(aliens[i].position_initial.x + offset_aliens.x,
                aliens[i].position_initial.y + offset_aliens.y);
            if (aliens[i].Alive) {
                aliens[i].Draw(batch);
                if (aliens[i].getCollisionBounds().overlaps(player.sprite.getBoundingRectangle())) {
                    System.out.println("Collision with alien at position: " + aliens[i].position.x + ", " + aliens[i].position.y);
                    Gdx.app.exit();
                }
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
