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

var image03 = new Image(); image03.src = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAgAAZABkAAD/7AARRHVja3kAAQAEAAAAZAAA/+4AJkFkb2JlAGTAAAAAAQMAFQQDBgoNAAAEowAAC1QAABAuAAAX4v/bAIQAAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQICAgICAgICAgICAwMDAwMDAwMDAwEBAQEBAQECAQECAgIBAgIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMD/8IAEQgAQABAAwERAAIRAQMRAf/EAMwAAAMBAAMBAAAAAAAAAAAAAAQGBwUBAwgCAQADAQEBAQAAAAAAAAAAAAADBAUCAQAGEAABBAMAAgICAwEAAAAAAAACAQMEBQAREiEGMRMiFDIVByMRAAIBAwMEAQIFAwUBAAAAAAECAxESBAAhEzEiFAVBUTJhcYFSI5GhBmIzQ2MkJRIAAQIFBAICAwAAAAAAAAAAAAERITFBYRIQUQIiIDJxQvCBExMBAAICAgEEAgMBAAAAAAAAAREhADFBUWFxgZGh8LHB0fHh/9oADAMBAAIRAxEAAAHwRBvaQd4Pe7YTV4PkBrKoqcwy+HvS0TLOqUvQXyc4ps91M6jhsl64C2nXI1JwXwMXiBQX7AH6hu49GVXWQqkOndphFXXaFWl+XuObXcyeiMU865fP2h99pDakcEbA5vSMjLWe6hQ2/wCdrr1Fco66cFzF713RNOL0QvOmxBwpteyqHiOGM33bCMKBVmU5o3K45GLY4mRPaHHr6bS//9oACAEBAAEFArr28rWA97BIsYgXNtAL+6mrZR6n1azOzpaKLBAmqeXU2kmZ7HbxahuXCFPtpm4UlqHWNSjbppbUmDIjVk6TU2lpDRl5kHQeitq6Yt1EOA4xMhU7TLDLRZa1z8KQ7NsL8I1dZy3buQs4vSr316opquMzFekwYLCynIUBv/Rfa6R6mie0T4MiZ7lbSzjyIqQ4wpBkV/6mpZCxbymqePHgxGLSZbQv6WX67EiWiKLJO1+8oobL63TbhP1z1tAjs2D5MG4a5FhRkq7liwbmRIoLWxreTHCLKsYNfX30C5YaaifrlDE5zgvIzGrj9knx/S3vYpnrP+Re53uOUF1+5WMvTZNvpooYTIjV5b3N3P8A/9oACAECAAEFAuN4AcYoIuEAqHShh9Z4PD8oheC+HDXaqWjeRUXZjygJ2i4GiXlMfJRVl10sL5+0FRS0pCKCA44K6f2uIPWL9qrHXkDbQlFgRzZ4q9C510CIovCOKvGB/wBcecRvBVdHkpxRxhOgId4ooi8Jhb+wBQUNdKTAEqGnRprD3tD/AA+refYgiLfeEo52GixEXNIuCnIf/9oACAEDAAEFAuvyIusQtZ0va/DZdgiEmC2mcLiYIePxwRzYjiO/YvCphLpNrppN46A7T4Ro9AACglvFXQiqY3pE+wtigKidKjf4orqri8a1yoa4XeNH5P8AjvkeHCxUHY4ymOGu+9KnnBLO/Cn0oecRwkzjxssDzhCnQH4+tejQgxFUc85vWb1iliIm/wD/2gAIAQICBj8CagqvMsIioMZDGKEZ6IwqTF2GeJivhLqdocdh1af451IyHeHhsrwFVan8lSO+5kfJBCExdxmSZY6mXIdPcgMP9R6kZmVNEYhInBiGj/U6UHP61QyHG2E5KzkJDDU0/9oACAEDAgY/As1mWLnwZJQdZuZESxCAqqIog9B+KjMQ0uPx3GO50L+Cv6Noq8zAYxUiOg5YeaHQh6jKPo1DGg1df2M3YiQOvud1mWGoY1ERC4ySIiKXMePqf//aAAgBAQEGPwLChOBDjShI1neI75KoW42kQkXMAB/TWB6/CV8dsWGKPzlaTGdCCHrIUe2wSfu+NZeN5uaVOTSeWDKvivJY3KA3EwJY/gdSSYcsUA7Fk4ysPNarR3ypW8cwZq9Bv01g5OFm5GPmYuL/APUjaPjjTKoaQevMB+2p+djrEypvaednz57xS4CgxkY1bRK09LRuf7axJ1WKc48rMZBc0aNT+Iy1qDYR9CDqT3fsM6FZYIcjKDSIsiSMoLJAsUdLAWNKfH5adfVytJgnHiZuTjlyPJMYEqMWZYljDHY0JHzqwN45laxGUh1Zt71t2W5CB2V+dtZUEmPlNmOzWyRUf+MfcIoS0QqaU+fy21jvHww8LqbJCseTmd1liCfseQU6fFa6hbHdORIhxlQRG8GTE4YuU/jGSIk7t7tuldP5GAMwjtjhfZxKqfxALKBVA1DuBdsde1/yl3xsSFMwQviXIjnnBkHDBWsiC09dtFRHRJ0YWfbJbtUtuzBbhUV600eRWxyzvHJGVN1VtZOxd7AaDc6WNRfScMeVKRFCvHGXFteQN9H2/PTytkmPMRWEbqsce6ZEYcywV5JVML7MtKW1OilRJmbSPmR5DcK9jccFGAj8h1oTtsB+OpFizJVxoIlVvLvZJeWYGbwGVUDPE9PncHSTLBIBS+GaI8nOnJbyEH/bdKdProCQyGfGhRI444rUnKs2zqv/ACAdW1HxYrz5Et/j47w1lliRLqg0RCFp+mpvaY3L4nHArczq8vJT7iy0butNCOmvb4PuPVYfscj3GMLMrJS5fV8qsYMmE1J5zXpQ67J4pkumhlaQrauO8HMzsSUdJI7gAvyRr1yYeZD7V/aY+NLmwSs1kQ4+Li7meWKvZ3rsB9KU1ievjVJZPXvwZp5uRJVxpRLJHGULLGZQSFNV26npr0mN6bDxY5IozmSyKVbITyH5BjZcm7SSoT3UP66keFI4b2ozRpbUsrBrW+26x+n11iZKSHHHqoxDBlQL47SxA98Zt25aMdZvrppHkLTKmE4KxxBIxKxLlhc5NaBf9WoTPEMmORJY58bbtUlrqSKOw2bhhU/prHjbIh8KGaRIpmHJLGjsjNd2fbxJcDufrtp43z5mgxg/iZtONeAmsbRxcZdhIn2nbrqBfQDM9hmNGZvZZUL3eLBIrBYzUOXCi6pr0O+njzMgBJI5IY8qWvZkturyqBvS76dRTpqf18TxvxTKVlFk0FYkKlineCa/2+NT+r9pm4+H69eScZnct7HtEcLWElGJrRiu39NTDIJQx/xhEUA3UojftAGxP4aTIEcU6Y7qB5KI8U8tslIrRZIyUG4+dQUZI8uSReGOWMWZJiqXO9LUY7D47aaHFGZYMdXgyGWMkGkj3Wm6lqw7RbVDLQgazsj1+TJgw5MfA/mmJJc3FWrLFGxXd9qbU3GpcWNEEuRkxPJK4RTG8XIbZJHoQSG+tf11a7pM3eQLruBmJUjetHoPnfWbJkrMriwKpC0MkjllPEfuSgqPu1kYmXH/AOj18xgnKxhEACiJTI0fbWSzrXc6MkljZZzo0XGAYSCMQNK2QZFCx2LstvWraN6WCNJTB2DlhjkLKywyMCwS40rvTTLil5MbJYSCZgXsyAakVfsL933E1oT9dT+v/wAj5EzsPEcep8XHgRBllIlPlbD+IBO75r06nVch5VM0nCjyMyROVVjGdlJujk6fNOulhxyMhpO8tF9vKpbcoRbNw0/I11D6DwEhf2M+PD5WQLZYc0ylLceW8skLdrn6W0rrGklIx4YkZPa5k70XZWAmUyUud0Hb1N3TUHqP8Yx3meKPJmeTKdceKaG6mJWQoFjnFf3EMx669i+OmHjt6yaTGy4/YZXEIyjhSEcmx7qH53pr2fq8dnkHr7xlq8wCDiPf49e00FRt8aTE4m8iaMiM9iW8f3h2Y7bDf89QJbFH46szRpIXXmDWlk/63TY/NRpPaRMoImsEyMzNG3VFMarUllHUXaVs2aUPEVW51slh2pfaBeStOuv/2gAIAQEDAT8huaClHgMUpTQbnI7kblGQowK9hhrfpj2/dLR3EY1urh7sSkzgIom0wRYsURYgixVkYcVCQyQBFpdoPOZxmZAPHKmZfZg6hxydQDggkiJRBo0bmuZnt4kJ23HWZaWNOOQPOgshbOgqL5tps4EdEm8FAGvYS4QSL3OIoK6sSohSUsrsyPonVdGjJXuCYlhw8w33ZknRIS0noBu0dY6BEFHEb2gmagm8ewRsRI2uTslDZyZig3dFWll8JkxwpIpshabwaLhKPbgWrKwa3nIo/tpCeycNGlGTFbSlmFLcmgggrAthCLkZoaiLeiZcBeJsVVf07s2l0YDJ0KAwQ1t1WYx0L1VmeXoBlY1WCwhiEkWkYIggZAkIEQFxaMEs2yEYMJTjkQ7CxK3gga6ZFlCfK7zOH27AnrZfWMq+Qtwt1HfwMjl4dNYEm6Q5cKAZxVBOAayjTZSyHjZXLJiwQkqk4iZAEJtVQi2vIL1lLNOtkoneCYajSTIn4PjECbCRRBM1FqNlhJ1OLTfjwsS2EBPFlzW8ss1WxCKaGslVQXTshXFwpSljHHgLKknjbQCx5lBd0AQU9bMidYUdQ+nNXvAezHT5WyksQLcdmO+hx6BDapjKIhMtGZkNlrznFNtD1irhkXWGexNyRHxsSI4cWZOqA5J92plIwgCQh+QTkx6ymEOEZ5z8uwRFjIDE0smKHGKDREpb8YXVP19EVrWOo3zk1YLRKpzLtZj7KhSUOQUoEcM0I6lrILVID5kCbW+rkGCEr+k8WRBGOiyBNhgusSye7CckAK/0mkeze5QiO0PCZpq49AICAayhNcf/2gAIAQIDAT8hpZ0yfInhkaMeGb+XV/fjC+ue8IpEJgotgylPaE40NPrjGTcfn44SdX5vNoS+DJKP5SfxhtB2NYHFOFqbHKluCssvrIgSe/jjPEGluf6xsAk8N+Phh8NvvGgj+2QlD1azQdOIr/0xOyahI7xBGhSO557i97w1fw96H253jARDcfwZDO8qN9Nx6YRSUNuSQo4OUSMj8+8C7FjcuadaRP3hCNpmPHWKB1e9veEhrLWBLzv+8Tek9bDJsCk2cf5kfCGD+fzgDWHU05MHH99YgySfzjiQ19eud8Dv+8HhZSLaSen1MBDT12YEubEYDs41lcH5xOSTbo8ZNKx0ZEWxzfGOn6sKTcMFa2wS2VqcU/Vls5//2gAIAQMDAT8hoLctD7ZKa9WEh35f1ksQrnzZdOGMmrQicbKnBZ7O3nKY/wA8YwQIjI1cv6f5wDCPz+sVvG8Rj1hPj84x5e94wI7v8/HIIyA/LwasZS5V2o5PmPfFOHHHX/MM2nHbNr7/AD64x0WpMBE+r+OCi7kZ6wNBok2e3rWtZQNR+v561gWu8buVzg0izrn1zZZeMookwIJR+frIHcgn0/vJZAk8arix/wBzU5++jrASxjbxkzoP3xgkOn253RLx3g/a+3+awO3LuNY3154K7xYiqn2wEAFFvqXxz3zjM7fZ84Eb5TM6wGNI98lF075GcJCJDvJhaqH54ynnsvFeOYyiYm7/AC8iHuZSBq8YF2zZml+VguWYj2zoY3GUJyInxiJED6z/2gAMAwEAAhEDEQAAEJyH3uCi08ynZPtnp5/Ke/8A9hSZuo/xSaD/2gAIAQEDAT8Qv1fWxwiL3JCOOBEj3nB1hgDITS5npK3IJjqJBWswxMUpLgwAxBBYZYNDSaF6ZCc4W99OnEySw9I6KHdAsgFLApU2XqKsAipmD9Jk0WgbgxEIcNZihyX5UystT4caBUEEvKk8KEzl/cFdvEjUoYYproKi3fGaQAZjBPPKkahaCgqY/bVjEC1PwwSURMFD8JJCQIFBIEnZizsdkdUC7UjJ1zyGJCgyBiqRzFMvTxfJCTg9JZluUYAIuBicyzSSUjlIMwrvsteWscQdBG1PXtByqjtpyICaloVLWo2QFsACjvIQRmTYkgTglLRAih6YzBSIwyCLsEHfU0tM2/1j7TeaLyUzXNq5JdO/Eg1wHJMRIaa54QEBgrEzsk0BKursJEE2LsJ9iG0jVWhIiNXIwSjIAkdy6QHSoaaWGBYMFbjMzvKkX2mGH2ZDvhJpT4iwMSA4vPUCTikmHYvJbxBYSXttpIfczlDabrgLAASVXSWrswCQTDOAlvIxyxLM1QwMyURq55rjKtDh7G2bxCIRuVFzY4iIcQwNKhBJMitnM7c6UkkQMZ62V8x5kgYDRm5+FEEZ4xUAERMVsuMEdRqwwywBDYdxFARSaSCwcSc6jEMhN54gCwPnU4Ds6iP5aDAoWzugHgGCIVAFJGyqTr0CMlcpzN1mNEZrhEDGCqz3AnhVWMCYp5QyRATNFiJgmspWOSDC78YpUtP8Velo3Igy0Yq8WxCLkDuItdIixBwiU4dkuGHu9Js9NldayoEoQUipBSYwGS67rE90i1EHwHYRJx4xCQ5gKMf/2gAIAQIDAT8QXtF6RROvX1MCbOYyQdaX14jeThomjcdu/v2cg6golBevYjzfFLhnCqk0LO2LekxsUtalRLCOZ+fFYtZgRqWGa16cPfOMhY4RHEylzxHzWOdW+TtQW1vU1kwYi4GY2Pfc+iTI8zz5UoRL6RvlJwaZDFWjZq4lCp0bwcYDD5IkDexBRgc7HZB9OeK1ZOQmADu5u+yutY7XCvZXBo0w9YWPQV8jbU1Kh61WBKxKiG52gzpOIj2jJfzUtuxLRLMkLZxkcUIE5CWmgyb76zdyl4CEAM3JmQhKw1MgdEICYY7/AFZOM1lX0EF6s6HxlDygxsWGrbm++eMMhJUvXqXVSSO6wJM3Vsgk6jqIajJkZ2jnAQU1K8EyXk4MPEDOVIBJpRXcitTzRCRLJdlIUmIIkJQK0hILQIAa58VhFNWm6k4biQvrjClMEqlkb24l0d5AkFyrMho1Vycl41Lwwc+ju9lYN6NbByDS3oxA8XkxDRW0Ms0jsZiLvHsGgARLUI0OiQ3RgSWIgOBEH1I5pmlHLxRISyEgaR+/NYkPABfunDBEg3F85FbrJWAmR3O487IwgqrmkoXyX9Y7aDRmb7y7czIxBhykRCoUN+Vu9jIuCoLdoSJW9O7kvB6qGASqQsnkuk06wkAMLHDZrjhiryfEGQ3MC44LpmO94HUhULMzcbQT8RkxEMNSMsRc83fGGGV7CIRCDCkaAq9uIKnSKAG4Lo4CmMDRR7RZbo+FiPVw0FZRQoUJCxCb4oglylVyA7hip3yzA1rBVUDAaYTCaXZ5mYwBEgi9pOi/QwtWAEOEPPNAdcXiBS0VgXcbi51HvhdiKE/MfPrgCWolLtdJXqmkisQbFyRMJPyPsD75WcgmQF9y8TxXc3k0Mu04ei3rjjV8/wD/2gAIAQMDAT8QCdkKW6liZfF96nFJlWaRDHPBfeLEV4KMTyGvr3MWJJDgKGp2u/zaDFlaBkqUI1dE8ZFqOECFlRl4j4+cXYFVzMapb6HseHKSq1CMzxA1HM34vJRTLsReiVo+GH1yEDBQQQimfqDRw5DQObslaGoYufisQUMmyFimZ5QUmNuqwJp4hGAGZSmBDvUerIErhAZOL/1TvAgDMoB0sdLPFSYYiwk0y5m+SZPfRjWQFA+EKZgoJ9rvFJYNhUaFI2PI2y/L7oklmHMERKHu453pggfcniPnCMCYyw3KUlUiNjDnBVZuKYW+fowMYPnSE6WX77TzAXkBCQzGhdtFJr1csUGWrfo93wjWQhboghkYmzzMlzhVuFRdWR3bB2xcGTAWjMgAWmSBKQiSryEMAyZWaKQTaFlo3cKnEsBLITQvIxNd2zQtUBQXth06mGA2PM41gBA0AdDmt+ME0YY4QQu3d0zw1krWETovk1Tki4CdMoSlkxtDNQ94bapNEIeIpwJTMk5ITYFHVyN7uW1rKYAw0WszAtvaFnIRhOBJiSAm6bDv0vm4PFchKEoHklmF7rEvthQTRPCJifCxeDtNIQhBIDdc7vDSHKbAUI8QOuNPpk1BFVzR17UgMJrCcJYQJImI8gxV5SyFMqASqfMaufVjBy8QmhslfsWGoneVBo1gjbxBRgDgkgVLFq3Dl/c4sQbgKE8Rs1xOI47kAED0xMXu/OssJQSdAY3F9rZPFFtljA8KnvE9cYzYgEyCwoyEytHrOskAGCbRCzwHReUwfAAiZViHhEPtvNYhVQ73Dw2YnIGwYImdvJc+fOUhSgW1UsnCb4ajEElU3VH6bPGBCBQgKG5uZo9ZxIhiHZEjB6ib8/GBJpCCpKGKOtsO42ZAUiCYpzrrv6M//9k=";
var image07 = new Image(); image07.src = "data:image/jpeg;base64,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";

var texture03 = new THREE.Texture(); texture03.image = image03; texture03.needsUpdate = true;
var texture07 = new THREE.Texture(); texture07.image = image07; texture07.needsUpdate = true;

  // Set up uniform.
  var tuniform = {
      iGlobalTime: { type: 'f', value: 0.1 },
      iChannel0:   { type: 't', value: texture03 /* THREE.ImageUtils.loadTexture( 'https://www.shadertoy.com/presets/previz/tex03.jpg') */ },
      iChannel1:   { type: 't', value: texture07 /* THREE.ImageUtils.loadTexture( 'https://www.shadertoy.com/presets/previz/tex07.jpg' ) */ },
  };
  tuniform.iChannel0.value.wrapS = tuniform.iChannel0.value.wrapT = THREE.RepeatWrapping;
  tuniform.iChannel1.value.wrapS = tuniform.iChannel1.value.wrapT = THREE.RepeatWrapping;

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
