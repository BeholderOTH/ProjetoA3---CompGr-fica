package Cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.io.InputStream;

public class Textura {
    private Texture textura;

    public void carregarTextura(String path) {
        try {
            InputStream stream = getClass().getResourceAsStream(path); //"./Imgs/Cora.png"
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "png");
            textura = TextureIO.newTexture(data);
        } catch (IOException | GLException e) {
            e.printStackTrace();
        }
    }

    public void aplicarTextura(GL2 gl) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        textura.bind(gl);

        textura.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        textura.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        textura.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        textura.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        textura.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_R, GL2.GL_CLAMP_TO_EDGE);
        textura.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

        textura.enable(gl);
    }

    public void configurarTextura (GL2 gl) {
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void desabilitarTextura (GL2 gl) {
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }
}
