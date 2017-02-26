package com.badman.slingmango.data;

import com.badlogic.gdx.math.MathUtils;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by roncon on 2/25/17.
 */

public class ConveyorBelt extends Box2DSprite
{
    public interface ConveyorBeltListener {
        void spawnFruit(boolean spawnMango);
        void increaseSpeed();
    }

    public ConveyorBeltListener listener;

    private float timer = MathUtils.random(2);

    private float difficultyModifier = 0;

    private int mangoCounter = 0;

    public void update(float delta)
    {
        if (timer > 0)
        {
            timer -= delta;

            if (timer <= 0)
            {
                if (listener != null)
                {
                    boolean isMango = MathUtils.random(1) == 0;

                    listener.spawnFruit(isMango);

                    if (isMango)
                    {
                        if (++mangoCounter >= 3)
                        {
                            mangoCounter = 0;

                            listener.increaseSpeed();
                        }
                    }

                    timer = MathUtils.random(1 - difficultyModifier, 3 - difficultyModifier);

                    if (difficultyModifier > 0)
                        difficultyModifier--;
                }
            }
        }
    }
}
