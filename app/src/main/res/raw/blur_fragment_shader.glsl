precision mediump float;                //doesn't work
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;
uniform vec2 u_Scale;

vec4 sample2points ( vec2 basecoord, vec2 shift )
{
return texture2D( u_Texture, basecoord + shift ) +
texture2D( u_Texture, basecoord - shift );
}

void main()
{
    gl_FragColor = ( sample2points ( v_TexCoordinate, u_Scale * 1.0 ) +
                     sample2points ( v_TexCoordinate, u_Scale * 0.1 ) +
                     sample2points ( v_TexCoordinate, u_Scale * 0.1 ) +
                     sample2points ( v_TexCoordinate, u_Scale * 0.1 ) +
                     sample2points ( v_TexCoordinate, u_Scale * 0.1 ) +
                     texture2D ( u_Texture, v_TexCoordinate ) ) / 11.0;
}
