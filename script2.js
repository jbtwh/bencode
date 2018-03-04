    var myInstance = {
      scene: new THREE.Scene(),
      camera: new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000),
      gameObjects: [],
      gameObjectsDict: {},
    };
    (function(myInstance) {
      myInstance.camera.position.x = 0;
      myInstance.camera.position.y = 10;
      myInstance.camera.position.z = 5;
      var webgl = new THREE.WebGLRenderer();
      webgl.setSize(window.innerWidth, window.innerHeight);
      document.body.appendChild(webgl.domElement);

      function onWindowResize() {
        myInstance.camera.aspect = window.innerWidth / window.innerHeight;
        myInstance.camera.updateProjectionMatrix();
        renderer.setSize(window.innerWidth, window.innerHeight);
      }
      window.addEventListener('resize', onWindowResize, false);
      renderer = webgl
      var light = new THREE.HemisphereLight(0xeeeeee, 0x888888, 1);
      light.position.set(0, 20, 0);
      myInstance.scene.add(light);
      counter = 0;

      function render() {
        myInstance.gameObjects.forEach(function(item) {
          item.update(counter);
        });
        requestAnimationFrame(render);
        renderer.render(myInstance.scene, myInstance.camera);
      }
      render();
    })(myInstance);

    (function() {
      var gameObject = function(name, x, y, z, col, rx, ry) {
        this.name = name
        this.geometry = new THREE.BoxGeometry(3, 3, 3);
        uniforms = {
          iGlobalTime: {
            type: "f",
            value: 1.0
          },
          iResolution: {
            type: "v2",
            value: new THREE.Vector2()
          },
        };
        uniforms.iResolution.value.x = 1; // window.innerWidth;
        uniforms.iResolution.value.y = 1; // window.innerHeight;
        this.material = new THREE.ShaderMaterial({
          uniforms: uniforms,
          vertexShader: document.getElementById('general').textContent,
          fragmentShader: document.getElementById('frag1').textContent
        });
        this.obj = new THREE.Mesh(this.geometry, this.material);
        this.obj.startTime = Date.now();
        this.obj.uniforms = uniforms;
        this.rx = rx
        this.ry = ry
        this.obj.name = name
        myInstance.scene.add(this.obj);
        this.obj.position.x = x;
        this.obj.position.y = y;
        this.obj.position.z = z;
        this.vel = [0, 0, 0]
      }
      gameObject.prototype.update = function() {
        this.obj.rotation.x += this.rx
        this.obj.rotation.y += this.ry
        this.obj.position.x += this.vel[0]
        this.obj.position.y += this.vel[1]
        this.obj.position.z += this.vel[2]
        var elapsedMilliseconds = Date.now() - this.obj.startTime;
        var elapsedSeconds = elapsedMilliseconds / 1000.;
        this.obj.uniforms.iGlobalTime.value = elapsedSeconds;
      }
      myInstance.gameObjectsDict['cube3'] = myInstance.gameObjects.push(new gameObject("cube3", 0, 10, -3, 0x0000ff, 0, 0.0001))
    })();
