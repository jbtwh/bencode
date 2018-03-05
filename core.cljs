(ns singlepage-app-om.core
  )

(enable-console-print!)

;; helper

(defn by-id [elem-id]
  (.getElementById js/document elem-id))

(println "Hello world!")
 ;; ADDED
 (defn foo [a b] (+ a b))

(defn initStats []
(let [stats (js/Stats.)]
(.setMode stats 0)
(set! (.-position (.-style (.-domElement stats))) "absolute")
(set! (.-left (.-style (.-domElement stats))) "0px")
(set! (.-top (.-style (.-domElement stats))) "0px")
(.appendChild (.getElementById js/document "Stats-output") (.-domElement stats))
stats
))

(def stats (initStats))


(def THREE js/THREE)

(def clock (THREE.Clock.))

(def tuniform {:iTime {:type "f" :value 0.1}
               :iResolution {:type "v2" value (THREE.Vector2.)}})

(def w  (.-innerWidth js/window))


(def h  (- (.-innerHeight js/window) 25))

(def aspect (/ w h))

(set! (.-x (.-value (.-iResolution tuniform))) w)

(set! (.-y (.-value (.-iResolution tuniform))) h)


(def scene (THREE.Scene.))

(def camera (THREE.OrthographicCamera. (/ w -2)  (/ w 2) (/ h 2) (/ h -2) 1 1000))


(set! (.-z (.-position camera)) 1000) 

(def renderer (THREE.WebGLRenderer. {:antialias true}))

(.setClearColor renderer (THREE.Color. 0x000000 1.0))

(.setSize renderer w h)

(.appendChild (.getElementById js/document "WebGL-output") (.-domElement renderer))

(def mat (THREE.ShaderMaterial. {
:uniforms tuniform
:vertexShader (.-textContent (.getElementById js/document "vertexshader"))
:fragShader (.-textContent (.getElementById js/document "fragmentshader"))
})) 

(def tobject (THREE.Mesh. (THREE.PlaneGeometry. w h 1 1) mat))

(.add scene tobject)

(defn loop []
(.update stats)
(.requestAnimationFrame loop)
(set! (.-value (.-iGlobalTime tuniform)) (+ (.-value (.-iGlobalTime tuniform)) (.getDelta clock))))

(loop)
   

(def vertshader "varying vec2 vUv; 
void main()
{
    vUv = uv;

    vec4 mvPosition = modelViewMatrix * vec4(position, 1.0 );
    gl_Position = projectionMatrix * mvPosition;
}")

(def fragshader "uniform float iGlobalTime;
uniform sampler2D iChannel0;
uniform sampler2D iChannel1;

varying vec2 vUv;
void main(void)
{
    //vec2 p = gl_FragCoord.xy / iResolution.xy;
    vec2 p = -1.0 + 2.0 *vUv;
    vec2 q = p - vec2(0.5, 0.5);

    q.x += sin(iGlobalTime* 0.6) * 0.2;
    q.y += cos(iGlobalTime* 0.4) * 0.3;

    float len = length(q);

    float a = atan(q.y, q.x) + iGlobalTime * 0.3;
    float b = atan(q.y, q.x) + iGlobalTime * 0.3;
    float r1 = 0.3 / len + iGlobalTime * 0.5;
    float r2 = 0.2 / len + iGlobalTime * 0.5;

    float m = (1.0 + sin(iGlobalTime * 0.5)) / 2.0;
    vec4 tex1 = texture2D(iChannel0, vec2(a + 0.1 / len, r1 ));
    vec4 tex2 = texture2D(iChannel1, vec2(b + 0.1 / len, r2 ));
    vec3 col = vec3(mix(tex1, tex2, m));
    gl_FragColor = vec4(col * len * 1.5, 1.0);
}")

(println vertshader)
(println fragshader)

