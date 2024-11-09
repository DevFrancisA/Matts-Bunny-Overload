package io.github.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Alien {
    public Vector2 position;
    public Sprite sprite;
    public Vector2 position_initial;
    public Boolean Alive = true;

    public Alien(Vector2 position, Texture img) {
        this.position = position;
        position_initial = position;
        sprite = new Sprite(img);
        sprite.setScale(2);

        // Add buffer by reducing the collision bounds
        float bufferScale = 0.8f; // Adjust this value between 0 and 1 to change hitbox size
        sprite.setSize(sprite.getWidth() * bufferScale, sprite.getHeight() * bufferScale);
        // Center the smaller hitbox within the sprite
        sprite.setOriginCenter();
    }

    public void Draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    public com.badlogic.gdx.math.Rectangle getCollisionBounds() {
        com.badlogic.gdx.math.Rectangle bounds = sprite.getBoundingRectangle();
        // Add some padding if needed
        bounds.setSize(bounds.width * 0.8f, bounds.height * 0.8f);
        // Center the bounds
        bounds.setPosition(
            position.x + (sprite.getWidth() - bounds.width) / 2,
            position.y + (sprite.getHeight() - bounds.height) / 2
        );
        return bounds;
    }
}
