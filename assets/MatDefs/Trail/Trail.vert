uniform mat4 g_WorldViewProjectionMatrix;
attribute vec2 inTexCoord;
attribute vec3 inPosition;
varying vec2 texCoord;

void main() {
    texCoord = inTexCoord;
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}