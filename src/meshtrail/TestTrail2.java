package meshtrail;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author german
 */
public class TestTrail2 extends SimpleApplication {
    
    private boolean enable;
    private Node parent;
    private float count;
    
    public static void main(String[] args) {
        TestTrail2 t = new TestTrail2();
        t.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(0, 5, 4));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        
        parent = new Node();
        rootNode.attachChild(parent);
        
        Node trialNode = new Node("Trial");
        trialNode.setLocalTranslation(2, 0, 0);
        parent.attachChild(trialNode);
        
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Blue);
        
        Geometry g = new Geometry("Sphere", new Sphere(16, 16, 0.2f), m);
        trialNode.attachChild(g);
        
        Node p1 = new Node("p1");
        p1.setLocalTranslation(-0.2f, 0, 0);
        trialNode.attachChild(p1);
        
        Node p2 = new Node("p2");
        p2.setLocalTranslation(0.2f, 0, 0);
        trialNode.attachChild(p2);
        
        MeshTrail mt = new MeshTrail(
                assetManager,
                p1,
                p2,
                40,
                assetManager.loadTexture("Textures/Trail3.png"),
                ColorRGBA.Blue);
        rootNode.attachChild(mt);
        
        inputManager.addMapping("Enable", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
            if (name.equals("Enable") && !isPressed) {
                enable = !enable;
            }
        }, "Enable");
        
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText text = new BitmapText(font);
        text.setSize(font.getCharSet().getRenderedSize());
        text.setText("Space -> Start/Stop Sphere Motion");
        text.setLocalTranslation(settings.getWidth() / 2f - text.getLineWidth() / 2f, text.getLineHeight() + 20, 0);
        guiNode.attachChild(text);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (enable) {
            parent.rotate(0, tpf * 2, 0);
            parent.setLocalTranslation(0, FastMath.abs(FastMath.sin(count)), 0);
            count += (tpf * 4);
        }
    }
}
