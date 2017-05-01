
  /////////////////////////////////
  // COS 344 - Computer Graphics //
  //         Final Project       //
  // Author : Regan Koopmans     //
  /////////////////////////////////

var worldMatrix = mat4.create();
var mvMatrixStack = [];
var viewMatrix = mat4.create();
var projMatrix = mat4.create();
var gl;
var canvas;
var matWorldUniformLocation;
var matViewUniformLocation;
var matProjUniformLocation;

var view_translate_x = 0;
var view_translate_y = 0;
var view_translate_z = 0;

var view_rotate_x = 0;
var view_rotate_y = 0;

var init = function() {


    worldMatrix = mat4.create();
    mvMatrixStack = [];
    viewMatrix = mat4.create();
    projMatrix = mat4.create();

    canvas = document.getElementById("canvas-gl");
    canvas.width = canvas.clientWidth;
    canvas.height = canvas.clientHeight;

    orig_mouse_x = 0.5 * canvas.clientWidth;
    orig_mouse_y = 0.5 * canvas.clientHeight;
    
    gl = canvas.getContext("webgl");
    gl.viewport(0, 0, canvas.width, canvas.height);
    canvas.addEventListener("resize", init);
    document.addEventListener("keydown", handleKeyDown);
    document.addEventListener("keyup", handleKeyUp);

    if (!gl) { alert("Could not get WebGL context!"); }

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
        -1.0, 1.0, -1.0,     0, 0,
        -1.0, 1.0, 1.0,      0, 1,
        1.0, 1.0, 1.0,       1, 1,
        1.0, 1.0, -1.0,      1, 0,

        // LEFT
        -1.0, 1.0, 1.0,      0, 0,
        -1.0, -1.0, 1.0,     1, 0,
        -1.0, -1.0, -1.0,    1, 1,
        -1.0, 1.0, -1.0,     0, 1,

        // RIGHT
        1.0, 1.0, 1.0,       1, 1,
        1.0, -1.0, 1.0,      0, 1,
        1.0, -1.0, -1.0,     0, 0,
        1.0, 1.0, -1.0,      1, 0,

        // FRONT
        1.0, 1.0, 1.0,       1, 1, 
         1.0, -1.0, 1.0,     1, 0, 
        -1.0, -1.0, 1.0,     0, 0,
        -1.0, 1.0, 1.0,      0, 1, 

        // BACK
        1.0, 1.0, -1.0,      0, 0, 
        1.0, -1.0, -1.0,     0, 1, 
        -1.0, -1.0, -1.0,    1, 1, 
        -1.0, 1.0, -1.0,     1, 0,

        // BOTTOM
        -1.0, -1.0, -1.0,    1, 1, 
        -1.0, -1.0, 1.0,     1, 0, 
        1.0, -1.0, 1.0,      0, 0,
        1.0, -1.0, -1.0,     0, 1, 
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

            // BACK1
            16,17,18,
            16,18,19,

            // BOTTOM
            21, 20, 22,
            22, 20, 23
        ];

    var seaVertices = [
        -1.0, 1.0, -1.0,     0, 0,
        -1.0, 1.0, 1.0,      1, 0,
        1.0, 1.0, 1.0,       1, 1,
        1.0, 1.0, -1.0,      0, 1,
    ];

    var seaIndices = [
        0,1,2,
        0,2,3
    ];


    var boxVertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, boxVertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(boxVertices),
                  gl.STATIC_DRAW);

    var boxIndexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, boxIndexBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(boxIndices),
                  gl.STATIC_DRAW);

    // Sea

    var seaVertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, seaVertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(seaVertices),
                  gl.STATIC_DRAW);

    var seaIndexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, seaIndexBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(seaIndices),
                  gl.STATIC_DRAW);


    var box_positionAttribLocation =
        gl.getAttribLocation(program, 'vertPosition');

    var box_texCoordAttribLocation =
        gl.getAttribLocation(program, 'vertTexCoord');

    var sea_positionAttribLocation =
        gl.getAttribLocation(program, 'vertPosition');

    var sea_texCoordAttribLocation =
        gl.getAttribLocation(program, 'vertTexCoord');


    gl.enableVertexAttribArray(box_positionAttribLocation);
    gl.enableVertexAttribArray(box_texCoordAttribLocation);
    gl.enableVertexAttribArray(sea_positionAttribLocation);
    gl.enableVertexAttribArray(sea_texCoordAttribLocation);
    gl.useProgram(program);

    // Textures

    var boxTexture = gl.createTexture();
    gl.bindTexture(gl.TEXTURE_2D, boxTexture);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, document.getElementById("box-tex"));

    var seaTexture = gl.createTexture();
    gl.bindTexture(gl.TEXTURE_2D, seaTexture);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.MIRROR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.MIRROR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, document.getElementById("sea-tex"));
    
    var skyTexture = gl.createTexture();
    gl.bindTexture(gl.TEXTURE_2D, skyTexture);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, document.getElementById("sky-tex"));
    gl.bindTexture(gl.TEXTURE_2D, null);


    matWorldUniformLocation = gl.getUniformLocation(program, 'mWorld');
    matViewUniformLocation = gl.getUniformLocation(program, 'mView');
    matProjUniformLocation = gl.getUniformLocation(program, 'mProj');
    worldMatrix = mat4.create();
    viewMatrix =  mat4.create();
    projMatrix =  mat4.create();
    mat4.identity(worldMatrix);
    mat4.lookAt(viewMatrix, [0,0,-10], [0,0,0], [0,1,0]);
    mat4.perspective(projMatrix, 45, canvas.width / canvas.height, 0.1, 100);

    sea_bob = 0;
    bob_direction = -1;

    setMatrixUniforms();
    var identityMatrix = new Float32Array(16);
    mat4.identity(identityMatrix);
    

    gl.clearColor(0.494,0.753,0.93,1.0);
    var loop = function() {

        // mat4.rotate(viewMatrix, viewMatrix, -view_rotate_y, [0,1,0]);
        mat4.translate(viewMatrix, viewMatrix, [view_translate_x, view_translate_y, view_translate_z]);

        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        mat4.identity(worldMatrix);

        // SEA //

        mvPushMatrix();
        gl.bindTexture(gl.TEXTURE_2D, seaTexture);
        gl.activeTexture(gl.TEXTURE0);
        mat4.translate(worldMatrix, worldMatrix, [0, -0.5 + sea_bob, 0]);
        if (sea_bob > 0.3 || sea_bob < -0.3) { bob_direction *= -1; }
        sea_bob += bob_direction * 0.001;
        mat4.scale(worldMatrix, worldMatrix, [100,0,100]);
        gl.bindBuffer(gl.ARRAY_BUFFER, seaVertexBufferObject);
        gl.vertexAttribPointer(sea_positionAttribLocation,3,gl.FLOAT,gl.FALSE,
                               5 * Float32Array.BYTES_PER_ELEMENT, 0);
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, seaIndexBufferObject);
        gl.vertexAttribPointer(sea_texCoordAttribLocation,3,gl.FLOAT,gl.FALSE,
                               2 * Float32Array.BYTES_PER_ELEMENT,3 *
                               Float32Array.BYTES_PER_ELEMENT);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, seaIndices.length,
                        gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        // SKY

        mvPushMatrix();
        gl.bindTexture(gl.TEXTURE_2D, skyTexture);
        gl.activeTexture(gl.TEXTURE0);
        mat4.translate(worldMatrix, worldMatrix, [0, 35, 50]);
        mat4.scale(worldMatrix, worldMatrix, [70,40,0]);
        mat4.rotate(worldMatrix, worldMatrix, Math.PI / 2, [1, 0, 0]);
        mat4.rotate(worldMatrix, worldMatrix, 3 *Math.PI / 2, [0, 1, 0]);
        gl.bindBuffer(gl.ARRAY_BUFFER, seaVertexBufferObject);
        gl.vertexAttribPointer(sea_positionAttribLocation,3,gl.FLOAT,gl.FALSE,
                               5 * Float32Array.BYTES_PER_ELEMENT, 0);
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, seaIndexBufferObject);
        gl.vertexAttribPointer(sea_texCoordAttribLocation,3,gl.FLOAT,gl.FALSE,
                               2 * Float32Array.BYTES_PER_ELEMENT,3 *
                               Float32Array.BYTES_PER_ELEMENT);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, seaIndices.length,
                        gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        /// BOX //////////

        mvPushMatrix();
        gl.bindTexture(gl.TEXTURE_2D, boxTexture);
        gl.activeTexture(gl.TEXTURE0);
        mat4.translate(worldMatrix, worldMatrix, [-3, 0, 2]);
        gl.bindBuffer(gl.ARRAY_BUFFER, boxVertexBufferObject);
        gl.vertexAttribPointer(box_positionAttribLocation,3,gl.FLOAT,gl.FALSE,
                               5 * Float32Array.BYTES_PER_ELEMENT, 0);
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, boxIndexBufferObject);
        gl.vertexAttribPointer(box_texCoordAttribLocation,2,
                                gl.FLOAT,
                                gl.FALSE,
                               5 * Float32Array.BYTES_PER_ELEMENT,
                               3 * Float32Array.BYTES_PER_ELEMENT);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, boxIndices.length,
                        gl.UNSIGNED_SHORT, 0);
        
        mvPopMatrix();

        // SHIP

        // mvPushMatrix();
        // gl.bindTexture(gl.TEXTURE_2D, null);
        // gl.bindBuffer(gl.ARRAY_BUFFER, shipVertexBufferObject);
        // gl.vertexAttribPointer(ship_positionAttribLocation,3,gl.FLOAT,gl.FALSE,
        //                        3 * Float32Array.BYTES_PER_ELEMENT, 0);
        // gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, shipIndexBufferObject);
        // setMatrixUniforms();
        // // console.log("Num indices " + ship_object.meshes[0].faces.length)
        // gl.drawElements(gl.TRIANGLE_STRIP, 12, gl.UNSIGNED_SHORT, 0);
        // mvPopMatrix();
        
        requestAnimationFrame(loop);
    };
    requestAnimationFrame(loop);
};

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
    gl.uniformMatrix4fv(matProjUniformLocation, gl.FALSE, projMatrix);
}

function mvPushMatrix() {
    var copy = mat4.create();
    copy = worldMatrix.slice();
    mvMatrixStack.push(copy);
}

function handleKeyDown(event) {
    switch(event.key) {
    case 'a' : view_translate_x = -0.05; break;
    case 'd' : view_translate_x = 0.05; break;
    case 'w' : view_translate_z = -0.05; break;
    case 's' : view_translate_z = 0.05; break;
    case 'k' : view_rotate_y = 0.05; break;
    case 'l' : view_rotate_y = -0.05; break;
    }
}

function handleKeyUp(event) {
    switch(event.key) {
    case 'a' : view_translate_x = 0; break;
    case 'd' : view_translate_x = 0; break;
    case 'w' : view_translate_z = 0; break;
    case 's' : view_translate_z = 0; break;
    case 's' : view_translate_z = 0; break;
    case 's' : view_translate_z = 0; break;
    case 'k' : view_rotate_y = 0; break;
    case 'l' : view_rotate_y = 0; break;
    }
}

function handleMouseMove(event) {
    diff_mouse_x = orig_mouse_x - event.clientX;
    diff_mouse_y = orig_mouse_y - event.clientY;
}