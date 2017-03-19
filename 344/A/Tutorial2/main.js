var init = function() {
    var canvas = document.getElementById("canvas-gl");
    var gl = canvas.getContext("webgl");

    if (!gl) {
        alert("Could not get WebGL context!");
    }


    // This code sets the canvas and context to the window size.
    
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
      gl.viewport(0,0, window.innerWidth, window.innerHeight);
    

    // set color
    gl.clearColor(0.9,0.9,0.9,1.0);

    // clear both color and depth buffers
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
    gl.enable(gl.DEPTH_TEST);
    gl.cullFace(gl.BACK);

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

    var boxVertices= [

        // TOP

        -1.0, 1.0, -1.0,     1.0,0.0,0.0,
        -1.0, 1.0, 1.0,      1.0,0.0,0.0,
        1.0, 1.0, 1.0,       1.0,0.0,0.0,
        1.0, 1.0, -1.0,      1.0,0.0,0.0,

        // LEFT

        -1.0, 1.0, 1.0,       1.0,1.0,0.5,
        -1.0, -1.0, 1.0,      1.0,1.0,0.5,
        -1.0, -1.0, -1.0,     1.0,1.0,0.5,
        -1.0, 1.0, -1.0,      1.0,1.0,0.5,

        // RIGHT

        1.0, 1.0, 1.0,       1,0.5,0.5,
        1.0, -1.0, 1.0,      1.0,0.5,0.5,
        1.0, -1.0, -1.0,     1.0,0.5,0.5,
        1.0, 1.0, -1.0,      1.0,0.5,0.5,

        // FRONT

        1.0, 1.0, 1.0,        0.3,0.1,1.0,
         1.0, -1.0, 1.0,      0.3,0.1,1.0,
        -1.0, -1.0, 1.0,      0.3,0.1,1.0,
        -1.0, 1.0, 1.0,       0.3,0.1,1.0,

        // BACK

        1.0, 1.0, -1.0,       0.5,1.0,0.5,
        1.0, -1.0, -1.0,      0.5,1.0,0.5,
        -1.0, -1.0, -1.0,     0.5,1.0,0.5,
        -1.0, 1.0, -1.0,      0.5,1.0,0.5,

        // BOTTOM

        -1.0, -1.0, -1.0,     0.5,0.5,0.5,
        -1.0, -1.0, 1.0,      0.5,0.5,0.5,
        1.0, -1.0, 1.0,       0.5,0.5,0.5,
        1.0, -1.0, -1.0,      0.5,0.5,0.5,
    ];

    // Indices tell WebGL which sets create a single triange.

    var boxIndices =
        [
            // TOP
            0,1,2,
            0,2,3,

            // LEFT
            5,4,6,
            6,4,7,

            // RIGHT
            8,9,10,
            8,10,11,

            // FRONT
            13,12,14,
            15,14,12,

            // BACK
            16,17,18,
            16,18,19,

            // BOTTOM
            21, 20, 22,
            22, 20, 23
        ];

    var pyramidVertices = 
    [
          0.0,  2.0,  0.0,      1.0,0.0,0.0,
        -1.0, 1.0, 1.0,       1.0,0.0,0.0,
         1.0, 1.0, 1.0,       1.0,0.0,0.0,
        // Right face
         0.0,  2.0,  0.0,       0.0,1.0,0.0,
         1.0, 1.0,  1.0,       0.0,1.0,0.0,
         1.0, 1.0, -1.0,       0.0,1.0,0.0,
        // Back face
         0.0,  2.0,  0.0,       0.0,0.0,1.0,
         -1.0, 1.0, -1.0,       0.0,0.0,1.0,
         1.0, 1.0, -1.0,       0.0,0.0,1.0,
        // Left face
         0.0,  2.0,  0.0,       0.0,1.0,1.0,
        -1.0, 1.0, -1.0,       0.0,1.0,1.0,
        -1.0, 1.0,  1.0,       0.0,1.0,1.0,
    ];
    var offset = boxVertices.size;

    var pyramidIndices = 
    [
        0,1,2,
        3,4,5,
        6,7,8,
        9,10,11,

    ];

    // Bind box to array buffer

    allVertices = boxVertices.concat(pyramidVertices);
    allIndices = boxIndices.concat(pyramidIndices.map(x => x + 24));
    console.log(allVertices);
    console.log(allIndices);

    var vertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(allVertices), gl.STATIC_DRAW);

    var indexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, indexBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(allIndices), gl.STATIC_DRAW);

    // Bind pyramid to array buffer
    

    var positionAttribLocation = gl.getAttribLocation(program, 'vertPosition');
    var colorAttribLocation = gl.getAttribLocation(program, 'vertColor');

    gl.vertexAttribPointer(
        positionAttribLocation,             // Attribute location
        3,                                  // Number of elements per Attribute
        gl.FLOAT,                           // type of data
        gl.FALSE,                           // normalised?
        6 * Float32Array.BYTES_PER_ELEMENT, // Size of individual vertex
        0                                   // Offset from the beginning vertex 
                                            // to this attribute
    );


    gl.vertexAttribPointer(
      colorAttribLocation,                // Attribute location
      3,                                  // Number of elements per Attribute
      gl.FLOAT,                           // type of data
      gl.FALSE,                           // normalised?
      6 * Float32Array.BYTES_PER_ELEMENT, // Size of individual vertex
      3 * Float32Array.BYTES_PER_ELEMENT  // Offset from the beginning vertex 
                                          // to this attribute
    );

    gl.enableVertexAttribArray(positionAttribLocation);
    gl.enableVertexAttribArray(colorAttribLocation);
   

    // Tell WebGL state machine which program should be active
    gl.useProgram(program);

    // set matrices
    var matWorldUniformLocation = gl.getUniformLocation(program, 'mWorld');
    var matViewUniformLocation = gl.getUniformLocation(program, 'mView');
    var matProjUniformLocation = gl.getUniformLocation(program, 'mProj');


    // init matrices (4x4 = 16)
    var worldMatrix =   new Float32Array(16);
    var viewMatrix =    new Float32Array(16);
    var projMatrix =    new Float32Array(16);

    mat4.identity(worldMatrix);

    // [camera] [look at] [direction of up]
    mat4.lookAt(viewMatrix, [0,0,-10], [0,0,0], [0,1,0]);


    // perspective matrix
    mat4.perspective(projMatrix, glMatrix.toRadian(45), canvas.width/canvas.height, 0.1, 1000.0);

    gl.uniformMatrix4fv(matWorldUniformLocation, gl.FALSE, worldMatrix);
    gl.uniformMatrix4fv(matViewUniformLocation,  gl.FALSE, viewMatrix);
    gl.uniformMatrix4fv(matProjUniformLocation,  gl.FALSE, projMatrix);

    ///
    /// MAIN RENDER LOOP
    ///
    var identityMatrix = new Float32Array(16);
    mat4.identity(identityMatrix);
    var angle = 0;
    var loop = function() {

        angle = performance.now() / 1000 / 6 * 2 * Math.PI;
        mat4.rotate(worldMatrix, identityMatrix, angle, [0,1,0]);
        // update code
        gl.uniformMatrix4fv(matWorldUniformLocation, gl.FALSE, worldMatrix);
        gl.clearColor(0.9, 0.9, 0.9, 1.0);
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        gl.drawElements(gl.TRIANGLES, allIndices.length, gl.UNSIGNED_SHORT, 0);   
        requestAnimationFrame(loop);
    };
    requestAnimationFrame(loop);
};

function get_shader_text(id) {
    text = document.getElementById(id).innerHTML;
    return text;
}
