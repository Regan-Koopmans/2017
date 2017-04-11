var worldMatrix = mat4.create();
var mvMatrixStack = [];
var viewMatrix = mat4.create();
var projMatrix = mat4.create();
var gl;
var canvas;
var matWorldUniformLocation;
var matViewUniformLocation;
var matProjUniformLocation;

var init = function() {
    worldMatrix = mat4.create();
    mvMatrixStack = [];
    viewMatrix = mat4.create();
    projMatrix = mat4.create();

    canvas = document.getElementById("canvas-gl");
    gl = canvas.getContext("webgl");

    if (!gl) { alert("Could not get WebGL context!"); }

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
          0.0,  1.0,  0.0,      1.0,0.0,0.0,
        -1.0, 0.0, 1.0,       1.0,0.0,0.0,
         1.0, 0.0, 1.0,       1.0,0.0,0.0,
        // Right face
         0.0,  1.0,  0.0,       0.0,1.0,0.0,
         1.0, 0.0,  1.0,       0.0,1.0,0.0,
         1.0, 0.0, -1.0,       0.0,1.0,0.0,
        // Back face
         0.0,  1.0,  0.0,       0.0,0.0,1.0,
         -1.0, 0.0, -1.0,       0.0,0.0,1.0,
         1.0, 0.0, -1.0,       0.0,0.0,1.0,
        // Left face
         0.0,  1.0,  0.0,       0.0,1.0,1.0,
        -1.0, 0.0, -1.0,       0.0,1.0,1.0,
        -1.0, 0.0,  1.0,       0.0,1.0,1.0,
    ];

    var pyramidIndices =
    [
        0,1,2,
        3,4,5,
        6,7,8,
        9,10,11,

    ];

    // Bind box to array buffer

    var boxVertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, boxVertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(boxVertices), gl.STATIC_DRAW);

    var boxIndexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, boxIndexBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(boxIndices), gl.STATIC_DRAW);

    // PYRAMID

    var pyramidVertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, pyramidVertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(pyramidVertices), gl.STATIC_DRAW);

    var pyramidIndexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, pyramidIndexBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(pyramidIndices), gl.STATIC_DRAW);

    // Bind pyramid to array buffer

    var box_positionAttribLocation = gl.getAttribLocation(program, 'vertPosition');
    var box_colorAttribLocation = gl.getAttribLocation(program, 'vertColor');
    var pyramid_positionAttribLocation = gl.getAttribLocation(program, 'vertPosition');
    var pyramid_colorAttribLocation = gl.getAttribLocation(program, 'vertColor');

    gl.enableVertexAttribArray(box_positionAttribLocation);
    gl.enableVertexAttribArray(box_colorAttribLocation);
    gl.enableVertexAttribArray(pyramid_positionAttribLocation);
    gl.enableVertexAttribArray(pyramid_colorAttribLocation);

    gl.vertexAttribPointer(box_positionAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT, 0);
    gl.vertexAttribPointer(box_colorAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT,3 * Float32Array.BYTES_PER_ELEMENT);
    gl.vertexAttribPointer(pyramid_positionAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT, 0);
    gl.vertexAttribPointer(pyramid_colorAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT,3 * Float32Array.BYTES_PER_ELEMENT);

    // Tell WebGL state machine which program should be active
    gl.useProgram(program);

    // set matrices
    matWorldUniformLocation = gl.getUniformLocation(program, 'mWorld');
    matViewUniformLocation = gl.getUniformLocation(program, 'mView');
    matProjUniformLocation = gl.getUniformLocation(program, 'mProj');

    // init matrices (4x4 = 16)
    worldMatrix = mat4.create();
    viewMatrix =  mat4.create();
    projMatrix =  mat4.create();

    mat4.identity(worldMatrix);

    // [camera] [look at] [direction of up]
    mat4.lookAt(viewMatrix, [0,0,-10], [0,0,0], [0,1,0]);
    // perspective matrix
    //mat4.perspective(projMatrix, glMatrix.toRadian(45), canvas.width/canvas.height, 0.1, 1000.0);
    projMatrix = ortho(projMatrix,-5.0, 5.0, -5.0, 5.0, -5.0, 5.0);
    gl.uniformMatrix4fv(matWorldUniformLocation, gl.FALSE, worldMatrix);
    gl.uniformMatrix4fv(matViewUniformLocation,  gl.FALSE, viewMatrix);
    gl.uniformMatrix4fv(matProjUniformLocation,  gl.FALSE, projMatrix);

    ///
    /// MAIN RENDER LOOP
    ///
    var identityMatrix = new Float32Array(16);
    mat4.identity(identityMatrix);

    gl.clearColor(0.9, 0.9, 0.9, 1.0);
    var loop = function() {

        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        //mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, projMatrix);
        mat4.identity(worldMatrix);

        /// BOX //////////

        mvPushMatrix();
        mat4.translate(worldMatrix, worldMatrix, [2, 0, -10]);
        gl.bindBuffer(gl.ARRAY_BUFFER, boxVertexBufferObject);
        gl.vertexAttribPointer(box_positionAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT, 0);
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, boxIndexBufferObject);
        gl.vertexAttribPointer(box_colorAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT,3 * Float32Array.BYTES_PER_ELEMENT);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, boxIndices.length, gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        //// PYRAMID ////////////////////

        mvPushMatrix();
        mat4.translate(worldMatrix, worldMatrix, [-2, 0, -10]);
        mat4.rotateY(worldMatrix, worldMatrix, Math.PI / 4);
        gl.bindBuffer(gl.ARRAY_BUFFER, pyramidVertexBufferObject);
        gl.vertexAttribPointer(pyramid_positionAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT, 0);
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, pyramidIndexBufferObject);
        gl.vertexAttribPointer(pyramid_colorAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT,3 * Float32Array.BYTES_PER_ELEMENT);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, pyramidIndices.length, gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        requestAnimationFrame(loop);
    };
    requestAnimationFrame(loop);
};

// PROJECTION FUNCTIONS

// function to create an orthographic parallel projectoin to be
// stored in "dest".

function ortho(dest, left, right, bottom, top, near, far) {
    mat4.identity(dest);
    dest[0] = 2 / (right - left);
    dest[5] = 2 / (top - bottom);
    dest[10] = -2 / (far - near);

    dest[12] = - ((left + right)/(right - left));
    dest[13] = - ((top + bottom)/(top - bottom));
    dest[14] = - ((far + near)/(far - near));
    return dest;
}

function oblique() {
    // TODO: implement
}

function perspective() {
    // TODO: implement
}

function get_shader_text(id) { return document.getElementById(id).innerHTML; }

function mvPopMatrix() {
    if (mvMatrixStack.length == 0) {
        console.log("empty!");
    }
    worldMatrix = mvMatrixStack.pop();
}

function setMatrixUniforms() {
    gl.uniformMatrix4fv(matWorldUniformLocation, gl.FALSE, worldMatrix);
    gl.uniformMatrix4fv(matViewUniformLocation,  gl.FALSE, viewMatrix);
}

function mvPushMatrix() {
    var copy = mat4.create();
    copy = worldMatrix.slice();
    mvMatrixStack.push(copy);
}