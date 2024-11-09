package io.github.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public Vector2 position;
    public Vector2 position_bullet;
    public Sprite sprite_bullet;
    public Sprite sprite;
    public float speed = 300;
    public float speed_bullet = 1000;


    public Player(Texture img, Texture img_bullet, Color color) {
        sprite = new Sprite(img);
        sprite_bullet = new Sprite(img_bullet);
        sprite_bullet.setScale(3);
        sprite_bullet.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sprite.setSize(sprite.getWidth() * 0.2f, sprite.getHeight() * 0.2f);
        sprite.setOriginCenter();
        position = new Vector2(
            (float) Gdx.graphics.getWidth() / 2,            // Center horizontally
            sprite.getHeight() / 2                  // Place the sprite's center near the bottom
        );
        position_bullet = new Vector2(0,10000);
    }


    public void Update(float deltaTime) {
        if (Gdx.input.isButtonJustPressed(0) && position_bullet.y>=Gdx.graphics.getHeight()) {
            position_bullet.x = position.x;
            position_bullet.y = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) position.x -= deltaTime * speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) position.x += deltaTime * speed;

        // Left boundary check
        if (position.x - sprite.getWidth() / 2 <= 0) {
            position.x = sprite.getWidth() / 2;
        }

        // Right boundary check
        if (position.x + sprite.getWidth() / 2 >= Gdx.graphics.getWidth()) {
            position.x = Gdx.graphics.getWidth() - sprite.getWidth() / 2;
        }

        position_bullet.y += deltaTime * speed_bullet;
    }

    public void Draw(SpriteBatch batch) {
        Update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2); // Position using the center point
        sprite.draw(batch);
        sprite_bullet.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sprite_bullet.setPosition(position_bullet.x, position_bullet.y);
        sprite_bullet.draw(batch);
    }
}
