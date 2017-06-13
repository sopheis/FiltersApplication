precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main()
{
    gl_FragColor.rgb = texture2D(u_Texture, v_TexCoordinate).rgb;
    vec3 color = vec3 (gl_FragColor.r *gl_FragColor.r, gl_FragColor.g * gl_FragColor.g, gl_FragColor.b * gl_FragColor.b);
    gl_FragColor.rgb = vec3 (color);
}