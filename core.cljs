(ns singlepage-app-om.core
  (:require [simple-xhr :as sxhr]))

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
 stats))

(def stats (initStats))


(def THREE js/THREE)

(def clock (THREE.Clock.))

(def tuniform (clj->js {:iTime {:type "f" :value 0.1}
                        :iResolution {:type "v2" :value (THREE.Vector2.)}
                        :iMouse {:type "v4" :value (THREE.Vector4.)}
}))

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

;;(.appendChild (.getElementById js/document "WebGL-output") (.-domElement renderer))

(def mat (THREE.ShaderMaterial. (clj->js {
:uniforms tuniform
:vertexShader (.-textContent (.getElementById js/document "vertexshader"))
:fragmentShader (.-textContent (.getElementById js/document "fragmentshader"))
}))) 

(def tobject (THREE.Mesh. (THREE.PlaneGeometry. w h 1 1) mat))

(.add scene tobject)

(defn mainloop []
 (.update stats)
 (.requestAnimationFrame js/window mainloop)
 (set! (.-value (.-iTime tuniform)) (+ (.-value (.-iTime tuniform)) (.getDelta clock)))
 (.render renderer scene camera))

(defn clickc [evt]
 (set! (.-fragmentShader mat) (.-textContent (.getElementById js/document "fragmentshader2")))
 (set! (.-needsUpdate mat) true)
 (get-shadertoy))

(set! (.-onclick (.getElementById js/document "change")) clickc)

(defn mousem [evt]
(set! (.-x (.-value (.-iMouse tuniform))) (.-pageX evt))
(set! (.-y (.-value (.-iMouse tuniform))) (.-pageY evt))
)

(set! (.-onmousemove js/document) mousem)

(defn moused [evt]
(set! (.-z (.-value (.-iMouse tuniform))) (.-pageX evt))
(set! (.-w (.-value (.-iMouse tuniform))) (.-pageY evt))
)

(set! (.-onmousedown js/document) moused)

(defn mouseu [evt]
(set! (.-z (.-value (.-iMouse tuniform))) 0)
(set! (.-w (.-value (.-iMouse tuniform))) 0)
)

(set! (.-onmouseup js/document) mouseu)

;;(mainloop)

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
void main()
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

;;(println vertshader)
;;(println fragshader)

(defn get-shadertoy [] 
(sxhr/request
  :url "https://www.shadertoy.com/api/v1/shaders/4ttSDM?key=Bdrtwz"
  :method "GET"
  :complete
  (fn [xhrio]
    (let [content (-> xhrio
                      .getResponseJson
                      (js->clj :keywordize-keys true))]
      (when (.isSuccess xhrio)
        (.log js/console content)
        (parse content))))))

(def unis ["iTimeDelta" "iFrame" "iChannelTime" "iChannelResolution" "iChannel" "iDate" "iSampleRate"])

(defn parse [shadertoy] 
(println shadertoy)
(let [renderpass (-> shadertoy :Shader :renderpass)
      code (delay (-> renderpass (nth 0) :code))]
(if (> (count renderpass) 1) (throw (js/Error. "renderpass > 1")))
(if (> (-> renderpass (nth 0) :inputs count) 0) (throw (js/Error. "inputs > 0")))
(if (some #(not= -1 (.indexOf code %)) (throw (js/Error. "code has unsupported uniform")))
(println "passed parse")
(println @code)
))

(println "passed")
(get-shadertoy)
