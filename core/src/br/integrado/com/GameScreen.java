package br.integrado.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * Created by Dorga on 03/08/2015.
 */
public class GameScreen extends BaseScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage palco;
    private BitmapFont fonte;
    private Label lbPontuacao;
    private Image player;
    private Texture playerTextureMid;
    private Texture playerTextureLeft;
    private Texture playerTextureRight;
    private boolean goingLeft;
    private boolean goingRight;
    private boolean goingUp;
    private boolean goingDown;
    float velocidade = 10; // velocidade de movimento do jogador.


    /**
     * Construtor padrão da tela de jogo
     * @param mg Referência para a classe principal
     */
    public GameScreen(MainGame mg) {
        super(mg);
    }

    /**
     * Chamado quando a tela é iniciada
     */
    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        palco = new Stage(new FillViewport(camera.viewportWidth,camera.viewportHeight, camera));
        initFonte();
        initInformacoes();
        initPlayer();
    }

    private void initPlayer() {
        playerTextureMid = new Texture("sprites/player.png");
        playerTextureLeft = new Texture("sprites/player-left.png");
        playerTextureRight = new Texture("sprites/player-right.png");
        player = new Image(playerTextureMid);
        float x = camera.viewportWidth / 2 - player.getWidth() / 2;
        float y = 10;
        player.setPosition(x, y);
        palco.addActor(player);
    }

    private void initInformacoes() {
        Label.LabelStyle lbEstilo = new Label.LabelStyle();
        lbEstilo.font = fonte;
        lbEstilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 Pontos", lbEstilo);

        palco.addActor(lbPontuacao);
    }

    private void initFonte() {
        fonte = new BitmapFont();
    }


    /**
     * Chamado a todo quadro de atualização do jogo (fps)
     * @param delta Tempo entre um quadro e outro em segundos
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f,.15f,.25f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lbPontuacao.setPosition(10, camera.viewportHeight - 20);
        capturaTeclas();
        atualizarPlayer(delta);
        palco.act(delta);
        palco.draw();

    }

    /**
     * Atualiza a posição dos jogadores
     * @param delta
     */
    private void atualizarPlayer(float delta) {
        if(goingRight && player.getX() < camera.viewportWidth - player.getImageWidth()){
            float x = player.getX() + velocidade * delta;
            float y = player.getY();

            player.setPosition(x,y);
        }
        if (goingLeft && player.getX() > 0) {
            float x = player.getX() - velocidade * delta;
            float y = player.getY();
            player.setPosition(x,y);
        }
        if (goingUp && player.getY() < camera.viewportHeight - player.getImageHeight()){
            float x = player.getX();
            float y = player.getY() + velocidade * delta;
            player.setPosition(x,y);
        }
        if (goingDown && player.getY() > 0){
            float x = player.getX();
            float y = player.getY() - velocidade * delta;
            player.setPosition(x,y);
        }

        if(goingRight){
            player.setDrawable(new SpriteDrawable(new Sprite(playerTextureRight)));
        }else if(goingLeft){
            player.setDrawable(new SpriteDrawable(new Sprite(playerTextureLeft)));
        }else{
            player.setDrawable(new SpriteDrawable(new Sprite(playerTextureMid)));

        }


    }

    /**
     * Verifica se as teclas estão pressionadas
     */
    private void capturaTeclas() {
        goingRight = false;
        goingLeft = false;
        goingUp = false;
        goingDown = false;
        int tecla;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            tecla = Input.Keys.LEFT;
            if (tecla == Input.Keys.LEFT && velocidade <= 300){velocidade += 10;}
            else{
                velocidade = 10;
            };
            goingLeft = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            goingRight = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            goingUp = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            goingDown = true;
        }
    }

    /**
     * É chamado sempre que há uma alteração no tamanho da tela
     * @param width Largura
     * @param height Altura
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    /**
     * É chamado sempre que o jogo for minimizado
     */
    @Override
    public void pause() {

    }

    /**
     * É chamado sempre que o jogo voltar para o primeiro plano
     */
    @Override
    public void resume() {

    }

    /**
     * É chamado quando a tela for destruida
     */
    @Override
    public void dispose() {
        batch.dispose();
        palco.dispose();
        fonte.dispose();
        playerTextureMid.dispose();
        playerTextureLeft.dispose();
        playerTextureRight.dispose();
    }
}
