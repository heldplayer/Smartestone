uniform sampler2D texture1;
varying vec4 vertColor;

void main() {
    vec4 color = texture2D(texture1, gl_TexCoord[0].st);
    vec4 appliedColor = vertColor;
    float grey = 0;
    
    if(textureSize(texture1, 0).x > 0) {
        grey = (color.r * vertColor.r + color.g * vertColor.g + color.b * vertColor.b) / 3;
    } else {
        grey = (vertColor.r + vertColor.g + vertColor.b) / 3;
    }
    
    color = vec4(grey * 0.4375 * 2, grey * 0.2578125 * 2, grey * 0.078125 * 2, color.a * vertColor.a * 0.5);
    
    gl_FragColor = color;
}