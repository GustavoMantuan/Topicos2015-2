package br.integrado.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.sun.prism.Texture;


/**
 * Created by Dorga on 14/09/2015.
 */
public class TelaMenu extends BaseScreen {

    private OrthographicCamera camera;
    private Stage cenario;
    private ImageTextButton iniciar;
    private Label titulo;
    private Label lbPontuacao;
    private BitmapFont fonteTitulo;
    private BitmapFont fontBotoes;
    private Texture botao;
    private Texture botaoP;

    public TelaMenu(MainGame mg) {
        super(mg);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

        camera.setToOrtho(false, width, height);
        camera.update();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
