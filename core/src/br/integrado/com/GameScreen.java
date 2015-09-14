package br.integrado.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
    private Label lbGameOver;
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
    private Array<Texture> texturaExplosao = new Array<Texture>();
    private Array<Explosao> explosoes = new Array<Explosao>();
    private Sound somTiro;
    private Sound somExplosao;
    private Sound somGameOver;
    private Music somFundo;


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
        initSom();
    }

    private void initSom() {
        somTiro = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
        somExplosao = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));
        somGameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/gameover.mp3"));
        somFundo = Gdx.audio.newMusic(Gdx.files.internal("Sounds/background.mp3"));
        somFundo.setLooping(true);

    }

    private void initTexturas() {
        texturaShot = new Texture("sprites/shot.png");
        meteor_1 = new Texture("sprites/enemie-1.png");
        meteor_2 = new Texture("sprites/enemie-2.png");

        for (int i = 1;i<=17;i++){
            Texture text = new Texture("sprites/explosion-" + i + ".png");
            texturaExplosao.add(text);
        }
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
        lbGameOver = new Label("Game Over",lbEstilo);
        lbGameOver.setVisible(false);
        palco.addActor(lbGameOver);
        palco.addActor(lbPontuacao);
    }

    /**
     * Instancia a fonte
     */
    private void initFonte() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.WHITE;

        fonte = new BitmapFont();
        generator.dispose();
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
        lbPontuacao.setText(String.valueOf(pontuacao));
        if (gameover == false) {
            capturaTeclas();
            atualizarPlayer(delta);
            // Atualiza a situação do palco
            palco.act(delta);
            // Desenha o palco na tela
            palco.draw();
            atualizarShot(delta);
            atualizaMeteoros(delta);
            detectarColizoes();
        }
        }
    private Rectangle jogador = new Rectangle();
    private Rectangle tiror = new Rectangle();
    private Rectangle meteoro = new Rectangle();
    private int pontuacao;
    private boolean gameover;

    private void detectarColizoes() {
        for (Image m : meteoros_1){
            meteoro.set(m.getX(),m.getY(),m.getWidth(),m.getHeight());
            for (Image tiro: shotings){
                tiror.set(tiro.getX(),tiro.getY(),tiro.getWidth(),tiro.getHeight());
                if (meteoro.overlaps(tiror)){
                    pontuacao +=5;
                    tiro.remove();
                    shotings.removeValue(tiro, true);
                    m.remove();
                    meteoros_1.removeValue(m, true);
                    criarExplosao(meteoro.getX() + meteoro.getWidth()
                            , meteoro.getY() + meteoro.getHeight() / 2);
                }
            }
        }
        if(jogador.overlaps(meteoro)){
            gameover = true;
        }
        for (Image m : meteoros_2){
            meteoro.set(m.getX(),m.getY(),m.getWidth(),m.getHeight());
            for (Image tiro: shotings){
                tiror.set(tiro.getX(),tiro.getY(),tiro.getWidth(),tiro.getHeight());
                if (meteoro.overlaps(tiror)){
                    pontuacao +=10;
                    tiro.remove();
                    shotings.removeValue(tiro, true);
                    m.remove();
                    meteoros_2.removeValue(m,true);
                }
            }
        }
        if(jogador.overlaps(meteoro)){
            gameover = true;
        }

    }

    private void criarExplosao(float x, float y) {
    Image ator = new Image(texturaExplosao.get(0));
        ator.setPosition(x - ator.getWidth() / 2, y);
        palco.addActor(ator);

        Explosao explosao = new Explosao(ator, texturaExplosao);

        explosoes.add(explosao);
    }

    private void atualizarExplosao(float delta){
        for (Explosao ex: explosoes){
            if (ex.getEstagio() >= 16){
                explosoes.removeValue(ex,true);
                ex.getAtor();
            }
        }
    }

    private final float MAX_INTERVALO_TIROS = 0.2f;
    private float intervaloTiros = 0;

    private void atualizarShot(float delta) {
        intervaloTiros = intervaloTiros + delta;
        if (shoting){
            if (intervaloTiros >= MAX_INTERVALO_TIROS){
                Image shot = new Image(texturaShot);
                somTiro.play();
                float x = player.getX()+player.getWidth()/2 - shot.getWidth()/2;
                float y = player.getY()+player.getHeight();
                shot.setPosition(x,y);
                shotings.add(shot);
                palco.addActor(shot);
                intervaloTiros = 0;
            }
        }
        float velocidade = 50;
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
        int qtMeteoros = meteoros_1.size + meteoros_2.size;

        if(qtMeteoros < 15) {
            int tipo = MathUtils.random(1, 3);
            if (tipo == 1) {
                Image meteoro = new Image(meteor_1);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoros_1.add(meteoro);
                palco.addActor(meteoro);
            } else {
                Image meteoro = new Image(meteor_2);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoros_2.add(meteoro);
                palco.addActor(meteoro);
            }
        }
            float velocidade = 100;
            for (Image m : meteoros_1){
                float x = m.getX();
                float y = m.getY() - velocidade * delta;
                m.setPosition(x,y);
                if(m.getY() + m.getHeight() < 0 ){
                    meteoros_1.removeValue(m,true); // remove da lista
                    m.remove(); // rmeove do palco
                }
            }
            velocidade = 300;
            for (Image m : meteoros_2){
                float x = m.getX();
                float y = m.getY() - velocidade * delta;
                m.setPosition(x,y);
                if(m.getY() + m.getHeight() < 0){
                    meteoros_2.removeValue(m,true); // remove da lista
                    m.remove(); // rmeove do palco
                }
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
        for(Texture t : texturaExplosao){
            t.dispose();
        }

    }
}
