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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
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
    float velocidade = 300; // velocidade de movimento do jogador.
    private boolean shoting;
    private Array<Image> shotings = new Array<Image>();
    private Texture texturaShot;
    private Texture meteor_1;
    private Texture meteor_2;
    private Array<Image> meteoros_1 = new Array<Image>();
    private Array<Image> meteoros_2 = new Array<Image>();

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
        initTexturas();
        initFonte();
        initInformacoes();
        initPlayer();
    }

    private void initTexturas() {
        texturaShot = new Texture("sprites/shot.png");
        meteor_1 = new Texture("sprites/enemie-1.png");
        meteor_2 = new Texture("sprites/enemie-2.png");
    }

    /**
     * Instancia os objetos do jogador e adiciona um palco.
     */
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

    /**
     * Instancia as informações descritas na tela.
     */
    private void initInformacoes() {
        Label.LabelStyle lbEstilo = new Label.LabelStyle();
        lbEstilo.font = fonte;
        lbEstilo.fontColor = Color.WHITE;
        lbPontuacao = new Label("0 Pontos", lbEstilo);
        palco.addActor(lbPontuacao);
    }

    /**
     * Instancia a fonte
     */
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
        // Atualiza a situação do palco
        palco.act(delta);
        // Desenha o palco na tela
        palco.draw();
        atualizarShot(delta);
        atualizaMeteoros(delta);
    }
    private final float MAX_INTERVALO_TIROS = 0.4f;
    private float intervaloTiros = 0;

    private void atualizarShot(float delta) {
        intervaloTiros = intervaloTiros + delta;
        if (shoting){
            if (intervaloTiros >= MAX_INTERVALO_TIROS){
                Image shot = new Image(texturaShot);
                float x = player.getX()+player.getWidth()/2 - shot.getWidth()/2;
                float y = player.getY()+player.getHeight();
                shot.setPosition(x,y);
                shotings.add(shot);
                palco.addActor(shot);
                intervaloTiros = 0;
            }
        }
        float velocidade = 8;
        for (Image tiro: shotings){
            //movimenta os tiros
            float x = tiro.getX();
            float y = tiro.getY() + velocidade + delta;
            tiro.setPosition(x,y);
            //remove os tiros que sairam da tela
            if(tiro.getY() > camera.viewportHeight){
                shotings.removeValue(tiro,true); // remove da lista
                tiro.remove(); // rmeove do palco
            }

        }
    }

    private void atualizaMeteoros(float delta){
        int tipo = MathUtils.random(1,3);
        if (tipo == 1 ){
            Image meteoro = new Image(meteor_1);
            float x = MathUtils.random(0,camera.viewportWidth - meteoro.getWidth());
            float y = MathUtils.random(camera.viewportHeight,camera.viewportHeight*2);
            meteoro.setPosition(x,y);
            meteoros_1.add(meteoro);
            palco.addActor(meteoro);
        }else {

            //2
        }
        float velocidade = 100;
        for (Image m : meteoros_1){

        }
    }

    /**
     * Atualiza a posição dos jogadores
     * @param delta
     */
    private void atualizarPlayer(float delta) {
        if(goingRight && player.getX() < camera.viewportWidth - player.getImageWidth()){
            // Verifica se o jogador está dentro da tela.
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
        shoting = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            shoting = true;
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
        texturaShot.dispose();
        meteor_1.dispose();
        meteor_2.dispose();
    }
}
