precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

uniform vec3 u_Color1;
uniform vec3 u_Color2;
uniform vec3 u_Color3;

float max ( float a, float b, float c )
{
    return max ( max ( a, b ), c );
}
float min ( float a, float b, float c )
{
    return min ( min ( a, b ), c );
}

void main ( )
{
    gl_FragColor = (texture2D(u_Texture, v_TexCoordinate));

    float maxcolor = max ( gl_FragColor.r, gl_FragColor.g, gl_FragColor.b );
    float mincolor = min ( gl_FragColor.r, gl_FragColor.g, gl_FragColor.b );
    float colordelta = maxcolor - mincolor;
    float saturation = colordelta / maxcolor;
    vec3 luminance = vec3 ( maxcolor );
    vec3 weights = ( gl_FragColor.rgb - mincolor ) / colordelta;

    vec3 newcolor = u_Color1 * gl_FragColor.r * weights.r +
                    u_Color2 * gl_FragColor.g * weights.g +
                    u_Color3 * gl_FragColor.b * weights.b;

    gl_FragColor.rgb = mix ( luminance, gl_FragColor.rgb * luminance, saturation );

}