precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main()
{
    gl_FragColor = (texture2D(u_Texture, v_TexCoordinate));
    gl_FragColor.rgb = 1.0 - gl_FragColor.rgb;
}