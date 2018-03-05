function initStats() {
            var stats = new Stats();
            stats.setMode(0); // 0: fps, 1: ms
            // Align top-left
            stats.domElement.style.position = 'absolute';
            stats.domElement.style.left = '0px';
            stats.domElement.style.top = '0px';
            document.getElementById("Stats-output").appendChild(stats.domElement);
            return stats;
        }

window.addEventListener("load", function () {

var stats = initStats();
  var clock = new THREE.Clock();

  // Set up uniform.
  var tuniform = {
      iTime: { type: 'f', value: 0.1 },
	iDate:{type: "v2", value:new THREE.Vector4()},
      iResolution: {type: "v2", value: new THREE.Vector2()}
  };
  var w = window.innerWidth;
  var h = window.innerHeight;
  var aspect = w/h;
  var frustumSize = 1000;
  tuniform.iResolution.value.x = w; 
  tuniform.iResolution.value.y = h; 
  

  // Set up our scene.
  var scene = new THREE.Scene();

var camera = new THREE.OrthographicCamera( w / - 2, w / 2, h / 2, h / - 2, 1, 1000 );
  //var camera = new THREE.PerspectiveCamera(45, w / h, 0.1, 20000);
  camera.position.z = 1000;

  var renderer = new THREE.WebGLRenderer({antialias: true});
  renderer.setClearColor(new THREE.Color(0x000000, 1.0));
  renderer.setSize(w, h);
  //renderer.shadowMapEnabled = true;

  document.getElementById("WebGL-output").appendChild(renderer.domElement);




  var mat = new THREE.ShaderMaterial( {
      uniforms: tuniform,
      vertexShader: $('#vertexshader').text(),
      fragmentShader: $('#fragmentshader').text(),
      //transparent: true
      //side:THREE.DoubleSide
  } );

  var tobject = new THREE.Mesh( new THREE.PlaneGeometry(w, h, 1, 1), mat);
  //var tobject = new THREE.Mesh( new THREE.PlaneBufferGeometry (w, h, 1, 1), mat);

scene.add(tobject);

document.getElementById("change").onclick = function click() {
    console.log("CLICKED");
    mat.fragmentShader = $('#fragmentshader2').text();
    mat.needsUpdate = true;
};

  var loop = function loop() {
    stats.update();
    requestAnimationFrame(loop);
    tuniform.iTime.value += clock.getDelta();
    renderer.render(scene, camera);
  };

  loop();

}, false);
