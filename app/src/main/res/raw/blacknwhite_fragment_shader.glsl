precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;
const vec3 c_LuminanceWeight = vec3 ( 0.2126, 0.7152, 0.072 );

void main ( )
{
    gl_FragColor = (texture2D(u_Texture, v_TexCoordinate));
    vec3 color_weighted = gl_FragColor.rgb * c_LuminanceWeight;
    float luminance = color_weighted.r + color_weighted.g + color_weighted.b;
    vec4 luminance_color = vec4 ( luminance, luminance, luminance, gl_FragColor.a );
    gl_FragColor = luminance_color;
}