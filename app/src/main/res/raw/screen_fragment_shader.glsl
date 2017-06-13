precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main()
{
    gl_FragColor = (texture2D(u_Texture, v_TexCoordinate));
    vec3 color = vec3 (1.0 - gl_FragColor.r, 1.0 - gl_FragColor.g, 1.0 - gl_FragColor.b);
    color = vec3 (color.r *color.r, color.g * color.g, color.b * color.b);
    color = vec3 (1.0 - color.r, 1.0 - color.g, 1.0 - color.b);
    gl_FragColor.rgb = vec3 (color);
}