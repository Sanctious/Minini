#ifdef GL_ES
precision mediump float;
#endif

uniform float u_time;
varying vec2 v_texCoords;

void main() {
    // Create a moving wave pattern using sine and cosine
    float wave = sin(v_texCoords.x * 10.0 + u_time) * 0.5 + 0.5;
    float wave2 = cos(v_texCoords.y * 10.0 + u_time * 1.5) * 0.5 + 0.5;

    // Combine waves for color channels
    vec3 color = vec3(wave, wave2, 1.0 - wave);

    gl_FragColor = vec4(color, 1.0);
}
