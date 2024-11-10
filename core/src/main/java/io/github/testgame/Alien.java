package io.github.testgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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
    }

    public void Draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    public com.badlogic.gdx.math.Rectangle getCollisionBounds() {
        return sprite.getBoundingRectangle();
    }
}
