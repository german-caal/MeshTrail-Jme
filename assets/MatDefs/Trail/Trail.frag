#ifdef COLOR
    uniform vec4 m_Color;
#endif
uniform sampler2D m_Texture;

varying vec2 texCoord;

void main() {
    gl_FragColor = texture2D(m_Texture, texCoord);
    #ifdef COLOR
        gl_FragColor *= m_Color;
    #endif
}