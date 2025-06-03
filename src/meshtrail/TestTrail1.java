package meshtrail;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author german
 */
public class TestTrail1 extends SimpleApplication {
    
    public static void main(String[] args) {
        TestTrail1 t = new TestTrail1();
        t.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(0, 3, 7));
        cam.lookAt(Vector3f.UNIT_Y, Vector3f.UNIT_Y);
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        speed = 0.5f;
        
        PointLight l = new PointLight(new Vector3f(0, 10, 6), ColorRGBA.White);
        rootNode.addLight(l);
        
        Node sword = (Node) assetManager.loadModel("Models/sword.j3o");
        rootNode.attachChild(sword);
        
        AnimComposer composer = sword.getChild("Armature").getControl(AnimComposer.class);
        composer.setCurrentAction("sword");
        
        SkinningControl armature = sword.getChild("Armature").getControl(SkinningControl.class);

        Node attacchment = armature.getAttachmentsNode("sword");
        
        Node p1 = new Node();
        p1.setLocalTranslation(0, 3.4f, 0);
        attacchment.attachChild(p1);
        
        Node p2 = new Node();
        p2.setLocalTranslation(0.15f, 0, 0);
        attacchment.attachChild(p2);
        
        MeshTrail trail = new MeshTrail(
                assetManager,
                p1,
                p2,
                50,
                assetManager.loadTexture("Textures/Trail1.png"),
                ColorRGBA.Magenta);
        rootNode.attachChild(trail);
    }
}
