var init = function() {
  var canvas = document.getElementById("canvas-gl");
  var gl = canvas.getContext("webgl");

  if (!gl) {
    alert("Could not get WebGL context!");
  }


  // This code sets the canvas and context to the window size.
  /*
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
  gl.viewport(0,0, window.innerWidth, window.innerHeight);
  */

  // set color
  gl.clearColor(0.9,0.9,0.9,1.0)

  // clear both color and depth buffers
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

  // create Shader
  var vertexShader = gl.createShader(gl.VERTEX_SHADER);
  var fragmentShader = gl.createShader(gl.FRAGMENT_SHADER);

  // load and compile Shaders
  gl.shaderSource(vertexShader, get_shader_text("shader-vs"));
  gl.shaderSource(fragmentShader, get_shader_text("shader-fs"));
  gl.compileShader(vertexShader);
  gl.compileShader(fragmentShader);


  // check compile status for vertex shader
  if (!gl.getShaderParameter(vertexShader, gl.COMPILE_STATUS)) {
    console.error("ERROR could not compile vertex shader!");
    return;
  }

  // check compile status for fragment shader
  if (!gl.getShaderParameter(fragmentShader, gl.COMPILE_STATUS)) {
    console.error("ERROR could not compile fragment shader!");
    return;
  }

  // creates program for GPU
  var program = gl.createProgram();
  gl.attachShader(program, vertexShader);
  gl.attachShader(program, fragmentShader);

  gl.linkProgram(program);
  if (!gl.getProgramParameter(program, gl.LINK_STATUS)) {
    console.error("ERROR linking program!", gl.getProgramInfoLog(program));
    return;
  }
  gl.validateProgram(program);
  if (!gl.getProgramParameter(program, gl.VALIDATE_STATUS)) {
    console.error("ERROR validating program!");
    return;
  }

  //
  // Create buffer
  //
  var triangleVertices =
  [ //X, Y
    0.0, 0.5,
    -0.5, -0.5,
    0.5, -0.5
  ];

  var triangelVertexBufferObject = gl.createBuffer();

  gl.bindBuffer(gl.ARRAY_BUFFER, triangelVertexBufferObject);
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(triangleVertices), gl.STATIC_DRAW);

  var positionAttribLocation = gl.getAttribLocation(program, 'vertPosition');
  gl.vertexAttribPointer(
    positionAttribLocation,             // Attribute location
    2,                                  // Number of elements per Attribute
    gl.FLOAT,                           // type of data
    gl.FALSE,                           // normalised?
    2 * Float32Array.BYTES_PER_ELEMENT, // Size of individual vertex
    0 // Offset from the beginning vertex to this attribute
  );


  gl.enableVertexAttribArray(positionAttribLocation);
  gl.useProgram(program);
  gl.drawArrays(gl.TRIANGLES, 0, 3);
}

function get_shader_text(id) {
  text = document.getElementById(id).innerHTML;
  return text;
}
