window.addEventListener("load", function () {

  // Camera variables.
  var width = window.innerWidth;
  var height = window.innerHeight;
  var aspect = width/height;
  var fov = 65;
  var clipPlaneNear = 0.1;
  var clipPlaneFar = 1000;
  var clearColor = 0x221f26;
  var clearAlpha = 1.0;

  // main container.
  var $container = $('#container');

  // Clock
  var clock = new THREE.Clock();

  // Set up uniform.
  var tuniform = {
      iGlobalTime: { type: 'f', value: 0.1 },
      iResolution: {type: "v2", value: new THREE.Vector2()}
  };
  tuniform.iResolution.value.x = 1; // window.innerWidth;
  tuniform.iResolution.value.y = 1; // window.innerHeight;

  // Set up our scene.
  var scene = new THREE.Scene();

  var camera = new THREE.PerspectiveCamera(fov,  aspect, clipPlaneNear, clipPlaneFar);
  camera.position.z = 1000;

  var renderer = new THREE.WebGLRenderer({antialias: true});
  renderer.setSize(width, height);
  renderer.setClearColor(new THREE.Color(clearColor, clearAlpha));

  $container.append(renderer.domElement);

  var mat = new THREE.ShaderMaterial( {
      uniforms: tuniform,
      vertexShader: $('#vertexshader').text(),
      fragmentShader: $('#fragmentshader').text(),
      //side:THREE.DoubleSide
  } );

  //var tobject = new THREE.Mesh( new THREE.PlaneGeometry(700, 394, 1, 1), mat);
  var tobject = new THREE.Mesh( new THREE.PlaneBufferGeometry (700, 394, 1, 1), mat);

  scene.add(tobject);

  var loop = function loop() {
    requestAnimationFrame(loop);
    tuniform.iGlobalTime.value += clock.getDelta();
    renderer.render(scene, camera);
  };

  loop();

}, false);
