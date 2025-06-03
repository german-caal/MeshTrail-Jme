package meshtrail;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.AbstractControl;
import com.jme3.texture.Texture;
import util.list.List;

/**
 *
 * @author german
 */
public class MeshTrail extends Geometry {

    private Node p1;
    private Node p2;

    private List<Vector3f> points1;
    private List<Vector3f> points2;

    private Vector3f prevM;

    private int nv;
    private int frames;
    private int trailLength;

    /**
     * Constructs a dynamic mesh trail between two moving nodes.
     * <p>
     * This class creates and manages a ribbon-like mesh that visually connects
     * two points (`p1` and `p2`) over time, creating a trail effect. The trail
     * is composed of a series of quads, and dynamically updates each frame to
     * reflect the movement of the two points.
     * </p>
     *
     * @param assetManager The AssetManager used to load materials and shaders.
     * @param p1 The first Node whose position is tracked to form one edge of
     * the trail.
     * @param p2 The second Node whose position is tracked to form the opposite
     * edge of the trail.
     * @param frames The number of historical positions to store (controls trail
     * length over time).
     * @param texture Texture for generated geometry
     */
    public MeshTrail(AssetManager assetManager, Node p1, Node p2, int frames, Texture texture) {
        super("Trial", createPlane(frames));
        mesh.setDynamic();
        this.p1 = p1;
        this.p2 = p2;
        this.frames = frames;

        trailLength = frames + 2;

        nv = trailLength * 6;

        Material mat = new Material(assetManager, "MatDefs/Trial/Trial.j3md");
        mat.setTexture("Texture", texture);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
//        mat.getAdditionalRenderState().setWireframe(true);

        setMaterial(mat);
        setQueueBucket(RenderQueue.Bucket.Transparent);
        addControl(control);

        points1 = new List<>();
        points2 = new List<>();
    }

    /**
     * Constructs a dynamic mesh trail between two moving nodes.
     * <p>
     * This class creates and manages a ribbon-like mesh that visually connects
     * two points (`p1` and `p2`) over time, creating a trail effect. The trail
     * is composed of a series of quads, and dynamically updates each frame to
     * reflect the movement of the two points.
     * </p>
     *
     * @param assetManager The AssetManager used to load materials and shaders.
     * @param p1 The first Node whose position is tracked to form one edge of
     * the trail.
     * @param p2 The second Node whose position is tracked to form the opposite
     * edge of the trail.
     * @param frames The number of historical positions to store (controls trail
     * length over time).
     * @param texture Texture for generated geometry
     * @param color The color of the trail material.
     */
    public MeshTrail(AssetManager assetManager, Node p1, Node p2, int frames, Texture texture, ColorRGBA color) {
        super("Trial", createPlane(frames));
        mesh.setDynamic();
        this.p1 = p1;
        this.p2 = p2;
        this.frames = frames;

        trailLength = frames + 2;

        nv = trailLength * 6;

        Material mat = new Material(assetManager, "MatDefs/Trail/Trail.j3md");
        mat.setTexture("Texture", texture);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
//        mat.getAdditionalRenderState().setWireframe(true);

        setMaterial(mat);
        setQueueBucket(RenderQueue.Bucket.Transparent);
        addControl(control);

        points1 = new List<>();
        points2 = new List<>();
    }

    private final AbstractControl control = new AbstractControl() {
        @Override
        protected void controlUpdate(float tpf) {
            boolean moved = true;

            Vector3f v1 = p1.getWorldTranslation().clone();
            Vector3f v2 = p2.getWorldTranslation().clone();
            Vector3f m = v1.add(v2).divide(2.0f);

            if (points1.getSize() > 1) {
                if (prevM.isSimilar(m, 0.001f)) {
                    moved = false;
                } else {
                    points1.insert(v1);
                    points2.insert(v2);
                    prevM = m;
                }
            } else {
                points1.insert(v1);
                points2.insert(v2);
                prevM = m;
                return;
            }

            if (points1.getSize() > frames || !moved) {
                points1.remove(0);
                points2.remove(0);
            }

            int availablePoints = points1.getSize();

            float[] pos = new float[nv];
            int c = 0;
            Vector3f fallback1 = points1.get(0);
            Vector3f fallback2 = points2.get(0);

            for (int i = 0; i < trailLength; i++) {
                Vector3f v = i < availablePoints ? points1.reverse().get() : fallback1;
                pos[c++] = v.getX();
                pos[c++] = v.getY();
                pos[c++] = v.getZ();
            }

            for (int i = 0; i < trailLength; i++) {
                Vector3f v = i < availablePoints ? points2.reverse().get() : fallback2;
                pos[c++] = v.getX();
                pos[c++] = v.getY();
                pos[c++] = v.getZ();
            }

            mesh.setBuffer(VertexBuffer.Type.Position, 3, pos);
            updateModelBound();

            if (availablePoints <= trailLength) {
                float[] uv = new float[trailLength * 2 * 2];
                c = 0;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < trailLength; j++) {
                        uv[c++] = (float) j / (availablePoints - 1);
                        uv[c++] = i;
                    }
                }
                mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, uv);
            }

        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    };
    
    private static Mesh createPlane(int n) {
        Mesh m = new Mesh();
        m.setDynamic();

        int nd = n + 2;

        float[] pos = new float[nd * 2 * 3];

        int c = 0;
        float df = nd;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < nd; j++) {
                pos[c] = 0;
                pos[c + 1] = 0;
                pos[c + 2] = 0;
                c += 3;
            }
        }

        m.setBuffer(VertexBuffer.Type.Position, 3, pos);

        int[] mv1 = new int[nd];
        int[] mv2 = new int[nd];

        for (int i = 0; i < nd; i++) {
            mv1[i] = i;
            mv2[i] = i + nd;
        }

        int[] ind = new int[(n + 1) * 6];

        c = 0;
        for (int i = 0; i < n + 1; i++) {
            ind[c] = mv1[i];
            ind[c + 1] = mv2[i];
            ind[c + 2] = mv2[i + 1];
            ind[c + 3] = mv1[i];
            ind[c + 4] = mv2[i + 1];
            ind[c + 5] = mv1[i + 1];
            c += 6;
        }

        m.setBuffer(VertexBuffer.Type.Index, 3, ind);

        float[] uv = new float[nd * 4];
        c = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < nd; j++) {
                uv[c] = (1 / df) * j;
                uv[c + 1] = i;
                c += 2;
            }
        }
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, uv);

        return m;
    }
}
