package Cena;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL2;

public class Keyboard implements KeyListener {
    private EventListener el;

    public Keyboard (EventListener el){
        this.el = el;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Chegou até aqui");
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                //System.out.println("Registrou a esquerda");
                if(el.liberarMovimentoBarra){
                    el.moverBarraEsquerda();
                }
                break;
            case KeyEvent.VK_RIGHT:
                //System.out.println("Registrou a direita");
                if(el.liberarMovimentoBarra){
                    el.moverBarraDireita();
                }
                break;
            case KeyEvent.VK_R:
                el.começarJogo();
                break;
            case KeyEvent.VK_T:
                el.pausarJogo();
                break;
            case KeyEvent.VK_Y:
                el.pararJogo();
                break;
            case KeyEvent.VK_ENTER:
                if(!el.sairTelaInicial){
                    el.sairTelaInicial = true;
                }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }
}
