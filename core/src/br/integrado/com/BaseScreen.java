package br.integrado.com;

import com.badlogic.gdx.Screen;

/**
 * Created by Dorga on 03/08/2015.
 */
public abstract class BaseScreen implements Screen {

    private MainGame mg;
    public BaseScreen(MainGame mg){
        this.mg = mg;
    }

    @Override
    public void hide() {
        dispose();
    }
}
