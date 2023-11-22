package Cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

public class EventListener implements GLEventListener {
    //Pontos de referência ortogonal da tela
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    //Delimitações X-Y do contorno quadrado ao redor do jogo em si
    private float xMinTela = -90.0f;
    private float xMaxTela = 90.0f;
    private float yMinTela = -65.0f;
    private float yMaxTela = 95.0f;
    //Delimitações do eixo X da barra
    public float posicaoBarraXFrontal;
    public float posicaoBarraXTraseira;
    //Bloqueio ou liberação do movimento da barra
    public boolean liberarMovimentoBarra;
    //Delimitações do eixo X-Y do centro da esfera
    private float centroEsferaX;
    private float centroEsferaY;
    //Delimitações do raio total da esfera nos eixos X-Y
    private float raioX = 10.0f;
    private float raioY = 1.75f * raioX;
    //Velocidade de translação da esfera nos eixos X-Y
    private float velocidadeY;
    private float velocidadeX;
    private float reservaVelocidadeY;
    private float reservaVelocidadeX;
    private float vidasXMin = 35.0f;
    private float vidasXMax = 45.0f;
    private int numVidas;
    private int numPlacar;
    private Textura textura;
    private TextRenderer textRenderer;
    public static GL2 gl = null;
    GLU glu;

    public void inicializarVariaveis() {
        posicaoBarraXFrontal = 50.0f;
        posicaoBarraXTraseira = -50.0f;
        liberarMovimentoBarra = false;
        centroEsferaX = 0.0f;
        centroEsferaY = 50.0f;
        velocidadeY = 0.0f;
        velocidadeX = 0.0f;
        reservaVelocidadeY = 0.0f;
        reservaVelocidadeX = 0.0f;
        numVidas = 5;
        numPlacar = 0;
    }

    private void esfera(GL2 gl) {
        double limite = 2*Math.PI;
        double i;
        gl.glBegin(GL2.GL_POLYGON);
        for(i = 0; i < limite; i+= 0.01) {
            gl.glVertex2d(centroEsferaX + raioX * Math.cos(i), centroEsferaY + raioY * Math.sin(i));
        }
        gl.glEnd();
    }

    /*
    private void esfera(GL2 gl) {
        double limite = 2*Math.PI;
        double i, cX, cY, rX, rY;
        cX = 0.0f;
        cY = 50.0f;
        rX = 10.0f;
        rY = 1.75*rX;
        gl.glBegin(GL2.GL_POLYGON);
        for(i = 0; i < limite; i+= 0.01) {
            gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
        }
        gl.glEnd();
    }
     */
    private void barra(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(posicaoBarraXTraseira, -35.0f);
        gl.glVertex2f(posicaoBarraXFrontal, -35.0f);
        gl.glVertex2f(posicaoBarraXFrontal, -55.0f);
        gl.glVertex2f(posicaoBarraXTraseira, -55.0f);
        gl.glEnd();
    }

    private void tela(GL2 gl) {
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex2f(xMinTela, yMinTela);
        gl.glVertex2f(xMaxTela, yMinTela);
        gl.glVertex2f(xMaxTela, yMaxTela);
        gl.glVertex2f(xMinTela, yMaxTela);
        gl.glEnd();
    }

    private void vidas(GL2 gl, float auxMin, float auxMax, int tentativas) {
        for (int i = 0; i < tentativas; i++){
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(auxMin, -75.0f); //80.0f
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(auxMax, -75.0f); //90.0f
            gl.glTexCoord2f(1, 1);
            gl.glVertex2f(auxMax, -90.0f); //90.0f
            gl.glTexCoord2f(0, 1);
            gl.glVertex2f(auxMin, -90.0f); //80.0f
            gl.glEnd();

            auxMin = auxMin + 11.0f;
            auxMax = auxMax + 11.0f;
        }
    }

    public void desenhaTexto(String texto, int x, int y, Color cor) {
        textRenderer.beginRendering(Renderer.larguraJanela, Renderer.alturaJanela);
        textRenderer.setColor(cor);
        textRenderer.draw(texto, x, y);
        textRenderer.endRendering();
    }

    public void moverBarraEsquerda(){
        System.out.println("Morreu para a esquerda");
        if(posicaoBarraXTraseira - 3 <= xMinTela){
            posicaoBarraXTraseira = xMinTela;
            posicaoBarraXFrontal = posicaoBarraXTraseira + 100;
        }
        else{
            posicaoBarraXFrontal -= 3;
            posicaoBarraXTraseira -= 3;
            System.out.println(posicaoBarraXFrontal + " / " + posicaoBarraXTraseira);
        }
    }

    public void moverBarraDireita(){
        System.out.println("Morreu para a direita");
        if(posicaoBarraXFrontal + 3 >= xMaxTela){
            posicaoBarraXFrontal = xMaxTela;
            posicaoBarraXTraseira = posicaoBarraXFrontal - 100;
        }
        else{
            posicaoBarraXFrontal += 3;
            posicaoBarraXTraseira += 3;
            System.out.println(posicaoBarraXFrontal + " / " + posicaoBarraXTraseira);
        }
    }

    public void marcarPonto() {
        System.out.println("Ponto contabilizado.");
        numVidas -= 1;
        centroEsferaX = 0.0f;
        centroEsferaY = 50.0f;
        System.out.println(numVidas);
    }

    public void começarJogo() {
        if(reservaVelocidadeX == 0 && reservaVelocidadeY == 0){
            velocidadeX = 1.0f;
            velocidadeY = 1.0f;
        }
        else {
            velocidadeY = reservaVelocidadeY;
            velocidadeX = reservaVelocidadeX;
        }

        liberarMovimentoBarra = true;
    }

    public void pausarJogo() {
        reservaVelocidadeY = velocidadeY;
        reservaVelocidadeX = velocidadeX;
        velocidadeX = 0.0f;
        velocidadeY = 0.0f;
        liberarMovimentoBarra = false;
    }

    public void pararJogo() {
        inicializarVariaveis();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        //Dados iniciais da cena
        glu = new GLU();
        //Cria uma instância da classe Textura
        textura = new Textura();
        //Carrega a textura das vidas
        textura.carregarTextura("../Imgs/Cora.png");

        //Define a fonte, seus parâmetros e instancia passando esses mesmos parâmetros para o objeto
        Font fonte = new Font("Comic Sans MS Negrito", Font.PLAIN, 20);
        textRenderer = new TextRenderer(fonte, true, true);

        //Atribui o valor das variáveis essenciais para o funcionamento do jogo
        inicializarVariaveis();

        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -100;
        xMax = yMax = zMax = 100;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        //
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();

        //Inicialização com a tela de fundo azul
        gl.glClearColor(0, 0, 1, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        //Definição da cor da barra
        gl.glColor3f(1, 0, 0);

        barra(gl);

        textura.aplicarTextura(gl);
        textura.configurarTextura(gl);

        vidas(gl, vidasXMin, vidasXMax, numVidas);

        textura.desabilitarTextura(gl);

        gl.glLineWidth(3.0f);
        tela(gl);

        desenhaTexto("Vidas:", 700, 100, Color.WHITE);
        desenhaTexto("Pontuação:", 550, 100, Color.WHITE);
        desenhaTexto("" + numPlacar, 600, 50, Color.WHITE);

        centroEsferaY += velocidadeY;
        centroEsferaX += velocidadeX;

        //Colisão horizontal da esfera com os limites da tela
        if (centroEsferaX + raioX >= xMaxTela || centroEsferaX - raioX <= xMinTela){
            velocidadeX = -velocidadeX;
        }

        //Colisão vertical da esfera com os limites da tela
        if (centroEsferaY + raioY >= yMaxTela || centroEsferaY - raioY <= yMinTela){
            velocidadeY = -velocidadeY;
        }

        //Colisão vertical da barra com a esfera
        if (centroEsferaY - raioY <= -35.0f && centroEsferaX >= posicaoBarraXTraseira && centroEsferaX <= posicaoBarraXFrontal){
            float distanciaVertical = Math.abs(centroEsferaY - (-35.0f)); // Distância vertical entre o centro da esfera e a parte superior da barra
            float distanciaHorizontal = Math.abs(centroEsferaX - (posicaoBarraXTraseira + posicaoBarraXFrontal) / 2.0f); // Distância horizontal entre o centro da esfera e o centro da barra

            if (distanciaVertical < raioY && distanciaHorizontal < (posicaoBarraXFrontal - posicaoBarraXTraseira) / 2.0f) {
                // Ajusta a posição da esfera para fora da barra na direção vertical
                centroEsferaY = -35.0f + raioY + 0.1f;
                velocidadeY = -velocidadeY;
                numPlacar += 10;
            }
            /*
            centroEsferaY = -60.0f + raioY + 0.1f;
            velocidadeY = -velocidadeY;
             */
        }

        if (centroEsferaY <= -35.0f && centroEsferaY >= -55.0f){
            if (centroEsferaX - raioX <= posicaoBarraXFrontal && centroEsferaX >= posicaoBarraXTraseira){
                centroEsferaX = posicaoBarraXFrontal + raioX + 0.1f;
                velocidadeX = -velocidadeX;
            } else if (centroEsferaX + raioX >= posicaoBarraXTraseira && centroEsferaX <= posicaoBarraXFrontal) {
                centroEsferaX = posicaoBarraXTraseira - raioX - 0.1f;
                velocidadeX = -velocidadeX;
            }
        }

        //Retira a vida do jogador caso a bala ultrapasse a barra
        if (centroEsferaY - raioY <= -65.0f){
            marcarPonto();
        }

        //Definição da cor da esfera
        gl.glColor3f(0, 1, 0);

        esfera(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
}
