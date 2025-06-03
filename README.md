# MeshTrail

**MeshTrail** is a utility class for **jMonkeyEngine (jME)** that creates a dynamic ribbon-like trail between two moving `Node`s. This class generates and updates a mesh over time to reflect the changing positions of the two endpoints.

The class extends `Geometry`, making it easy to attach to the scene graph just like any other spatial.

---

## Features

* Creates a real-time animated trail between two `Node`s.
* Configurable trail length through a `frames` parameter.
* Easily customizable color and texture for a wide range of visual styles.
* Example textures included in the `Textures/` folder for quick experimentation.

---

## Class Overview

```java
public class MeshTrail extends Geometry {
    public MeshTrail(
        AssetManager assetManager,
        Node p1,
        Node p2,
        int frames,
        Texture texture,
        ColorRGBA color
    )

    public MeshTrail(
        AssetManager assetManager,
        Node p1,
        Node p2,
        int frames,
        Texture texture
    )
}
```

### Parameters

| Parameter      | Description                                                                     |
| -------------- | ------------------------------------------------------------------------------- |
| `assetManager` | Used to load materials and shaders.                                             |
| `p1`, `p2`     | The two moving `Node`s whose positions define the edges of the trail.           |
| `frames`       | Number of historical positions to store (controls trail length).                |
| `texture`      | The texture used for the trail’s surface.                                       |
| `color`        | (Optional) Tint color for the trail material.                                   |

---

## Usage Example

```java
Node attachment = ...;

Node p1 = new Node();
attachment.attachChild(p1);

Node p2 = new Node();
p2.setLocalTranslation(0, 2.0f, 0);
attachment.attachChild(p2);

MeshTrail trail = new MeshTrail(
    assetManager,
    p1,
    p2,
    50,
    assetManager.loadTexture("Textures/Trail1.png"),
    ColorRGBA.Magenta
);

rootNode.attachChild(trail);
```

Attach `p1` and `p2` to any animated or dynamically updated nodes in your scene to generate a visually responsive trail.

---

## Customization

* **Trail Length:** Adjust the `frames` parameter to increase or shorten the duration of the trail effect.
* **Textures:** Use different textures to create glowing trails, smoke lines, magic effects, and more. Check out the `Textures/` folder for example assets.
* **Colors:** Combine tint color with semi-transparent textures for glowing or fading effects.

---

## Included Assets

The `Textures/` folder includes several sample textures you can use out of the box. Try swapping them in the constructor to see how the visual style changes.

In addition, the `MatDefs/Trail/` folder contains the custom **Material Definition**, **Vertex Shader**, and **Fragment Shader** used to render the trail. You can modify these to customize the visual behavior or integrate additional effects like glow, fade, or distortion.

---

## Notes

* The class extends `Geometry`, so it behaves like any other `Spatial` and can be added to the scene graph directly.
* Ensure the nodes `p1` and `p2` are updated each frame (typically by animation or manual transformation).
