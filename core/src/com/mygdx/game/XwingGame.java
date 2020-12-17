package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

public class XwingGame extends Game {
    private Texture background;
    private Sprite xwing;
    private SpriteBatch batch;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private float shotSpeed = 5.0f;
    private int lastShotTime = 0;
    private int lastEnemyShot = 0;
    private float xwingSpeed=2.0f;
    private FileHandle shotFile;
    private List<Shot> shotList = new ArrayList<Shot>();
    private List<Shot> enemyShotList = new ArrayList<Shot>();
    
    @Override
    public void create()
    {
        FileHandle xwingFile = Gdx.files.internal("xwing.png"); 
        FileHandle tieFile = Gdx.files.internal("tiefighter.png");
        FileHandle tieAdvFile = Gdx.files.internal("tieAdvanced.png");
        FileHandle backgroundFile = Gdx.files.internal("background.jpg");
        
        
        background = new Texture(backgroundFile);
        
        shotFile = Gdx.files.internal("shot.png");
        xwing = new Sprite(new Texture(xwingFile));
        xwing.setScale(0.7f);
        xwing.setPosition(200,10);
        enemyList.add(new Enemy(new Texture(tieAdvFile), 50, 400, 20, -1, 4.0f, 50));
        enemyList.add(new Enemy(new Texture(tieFile), 50, 300, 0, 1, 2.0f, 100));
        enemyList.add(new Enemy(new Texture(tieFile), 200, 300, 0, 1, 2.0f, 100));
        batch = new SpriteBatch();   
    }
    
    @Override
    public void render()
    {   
        lastShotTime++;
        
        //Update the time since the last shot for each enemy
        for(Enemy en : enemyList) {
            en.setLastShot(en.getLastShot()+1);
        }
        
        //Check if a player shot hits an enemy
        for(Shot xwing_shot : shotList) {
            xwing_shot.translateY(shotSpeed);
            for(Enemy en : enemyList) {
                if(xwing_shot.isDisabled()==false && xwing_shot.getBoundingRectangle().overlaps(en.getBoundingRectangle())) {
                    if(en.isDisabled()==false)
                        xwing_shot.setDisabled(true);
                    en.decreaseShield(1);
                    System.out.println(en.getShieldStrength());
                    if(en.getShieldStrength()<1)
                        en.setDisabled(true);
                }        
            }
        }
        
        //Move the laser shots from the enemies
        for(Shot en_shot : enemyShotList) {
            en_shot.translateY(-2);
        }
        
        //Move each enemy and check if it is allowed to shoot.
        //If time since last shot is large enough, fire a new laser shot
        for(Enemy en : enemyList) {
            en.Move();
            if(en.getLastShot()>en.getFireRate()&&en.isDisabled()==false) {
                enemyShotList.add(en.Shoot());
                en.setLastShot(0);
            }
        }
        
        if(Gdx.input.isKeyPressed(Keys.LEFT) && xwing.getX()>0) {
            xwing.translateX(-xwingSpeed);
        }
        if(Gdx.input.isKeyPressed(Keys.RIGHT) && xwing.getX()<Gdx.graphics.getWidth()-xwing.getWidth())
            xwing.translateX(xwingSpeed);
        if(Gdx.input.isKeyPressed(Keys.UP) && xwing.getY()<Gdx.graphics.getHeight()-xwing.getHeight())
            xwing.translateY(xwingSpeed);
        if(Gdx.input.isKeyPressed(Keys.DOWN) && xwing.getY() > 0)
            xwing.translateY(-xwingSpeed);
        
        //If we press Space, the player ship will shoot if enough time
        //has passed since the last shot fired
        if(Gdx.input.isKeyPressed(Keys.SPACE) && lastShotTime > 10) 
        {
            lastShotTime=0;
            shotList.add(new Shot(new Texture(shotFile), 
                        (int)(xwing.getX()+50), 
                        (int)(xwing.getY()+110),
                        1, 2.0f));
        }

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        batch.draw(background,0,0);
        xwing.draw(batch);
        for(Enemy en : enemyList) {
            if(en.isDisabled()==false)
                en.draw(batch);
        }
        for(Shot xw_shot : shotList) {
            if(xw_shot.isDisabled()==false)
                xw_shot.draw(batch);
        }
        for(Shot en_shot : enemyShotList) {
            if(en_shot.isDisabled()==false)
                en_shot.draw(batch);
        }
        batch.end();
    }
}
