
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

var ship_rotatation = 0;

var init = function() {
    worldMatrix = mat4.create();
    mvMatrixStack = [];
    viewMatrix = mat4.create();
    projMatrix = mat4.create();

    canvas = document.getElementById("canvas-gl");
    canvas.width = canvas.clientWidth;
    canvas.height = canvas.clientHeight;


    gl = canvas.getContext("webgl");
    gl.viewport(0, 0, canvas.width, canvas.height);
    document.addEventListener("keydown", handleKeyDown);
    document.addEventListener("keyup", handleKeyUp);
    document.addEventListener("keypress", handleKeyPress);

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

    box = JSON.parse(getFile("models/box.json"));
    var boxVertices = box.data.attributes.position.array;
    var boxIndices = box.data.index.array;
    var boxNormals = box.data.attributes.normal.array;

    var boxVertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, boxVertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(boxVertices),
                  gl.STATIC_DRAW);

    var boxIndexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, boxIndexBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(boxIndices),
                  gl.STATIC_DRAW);

    var boxNormalBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, boxNormalBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(boxNormals), gl.STATIC_DRAW);

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

    // Ship object

    ship = JSON.parse(getFile("models/ship-final.json"));
    var shipVertices = ship.data.attributes.position.array;
    var shipIndices = ship.data.index.array;
    var shipNormals = ship.data.attributes.normal.array;

    var shipVertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, shipVertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(shipVertices),
                  gl.STATIC_DRAW);

    var shipIndexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, shipIndexBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(shipIndices),
                  gl.STATIC_DRAW);

    var shipNormalBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, shipNormalBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(shipNormals), gl.STATIC_DRAW);

    // Pirate

    pirate = JSON.parse(getFile("models/pirate.json"));
    console.log(pirate);
    var pirateVertices = pirate.data.attributes.position.array;
    var pirateIndices = pirate.data.index.array;
    var pirateNormals = pirate.data.attributes.normal.array;

    var pirateVertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, pirateVertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(pirateVertices),
                  gl.STATIC_DRAW);

    var pirateIndexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, pirateIndexBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(pirateIndices),
                  gl.STATIC_DRAW);

    var pirateNormalBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, pirateNormalBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(pirateNormals), gl.STATIC_DRAW);


    var box_positionAttribLocation =
        gl.getAttribLocation(program, 'vertPosition');

    var box_texCoordAttribLocation =
        gl.getAttribLocation(program, 'vertTexCoord');

    var sea_positionAttribLocation =
        gl.getAttribLocation(program, 'vertPosition');

    var sea_texCoordAttribLocation =
        gl.getAttribLocation(program, 'vertTexCoord');

    var ship_positionAttribLocation =
        gl.getAttribLocation(program, 'vertPosition');

    var ship_texCoordAttribLocation =
        gl.getAttribLocation(program, 'vertTexCoord');

    var ship_normalAttribLocation =
        gl.getAttribLocation(program, 'vertNormal');

    gl.enableVertexAttribArray(box_positionAttribLocation);
    gl.enableVertexAttribArray(box_texCoordAttribLocation);
    gl.enableVertexAttribArray(sea_positionAttribLocation);
    gl.enableVertexAttribArray(sea_texCoordAttribLocation);
    gl.enableVertexAttribArray(ship_positionAttribLocation);
    gl.enableVertexAttribArray(ship_texCoordAttribLocation);
    gl.enableVertexAttribArray(ship_normalAttribLocation);
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
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.REPEAT);
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

    var shipTexture = gl.createTexture();
    gl.bindTexture(gl.TEXTURE_2D, shipTexture);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, document.getElementById("ship-tex"));

    var skinTexture = gl.createTexture();
    gl.bindTexture(gl.TEXTURE_2D, skinTexture);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.REPEAT);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, document.getElementById("skin-tex"));
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
    ship_rotation = 0;

    setMatrixUniforms();
    var identityMatrix = new Float32Array(16);
    mat4.identity(identityMatrix);

    gl.clearColor(0.494,0.753,0.93,1.0);
    mat4.translate(viewMatrix, viewMatrix, [0, -5, 2]);
    var loop = function() {

        mat4.translate(viewMatrix, viewMatrix, [view_translate_x, view_translate_y, view_translate_z]);
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        mat4.identity(worldMatrix);

        // SEA //

        mvPushMatrix();
        gl.bindTexture(gl.TEXTURE_2D, seaTexture);
        gl.activeTexture(gl.TEXTURE0);
        mat4.translate(worldMatrix, worldMatrix, [0, -0.5 + sea_bob, 0]);
        if (sea_bob > 1 || sea_bob < -0.7) { bob_direction *= -1; }
        sea_bob += bob_direction * 0.0017;
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
        mat4.translate(worldMatrix, worldMatrix, [0, 35, 80]);
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
        mat4.translate(worldMatrix, worldMatrix, [-7, 0, 10]);
        gl.bindBuffer(gl.ARRAY_BUFFER, boxVertexBufferObject);
        gl.vertexAttribPointer(ship_positionAttribLocation,3,gl.FLOAT,gl.FALSE,
                               3 * Float32Array.BYTES_PER_ELEMENT, 0);

        gl.bindBuffer(gl.ARRAY_BUFFER, boxNormalBufferObject);

        gl.vertexAttribPointer(
          ship_normalAttribLocation,
          3, gl.FLOAT,
          gl.TRUE,
          3 * Float32Array.BYTES_PER_ELEMENT,
          0
        );

        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, boxIndexBufferObject);
        gl.vertexAttribPointer(ship_texCoordAttribLocation,2,
                                gl.FLOAT,
                                gl.FALSE,
                              2 * Float32Array.BYTES_PER_ELEMENT,
                              0);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, boxIndices.length,
                        gl.UNSIGNED_SHORT, 0);

        mvPopMatrix();

        mvPushMatrix();
        gl.bindTexture(gl.TEXTURE_2D, shipTexture);
        gl.activeTexture(gl.TEXTURE0);
        mat4.translate(worldMatrix, worldMatrix, [0.5, 3, 10]);
        mat4.scale(worldMatrix, worldMatrix, [2,1,1]);
        mat4.rotate(worldMatrix, worldMatrix, Math.PI / 2.2, [0, 1, 0]);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, boxIndices.length,
                        gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        mvPushMatrix();
        mat4.translate(worldMatrix, worldMatrix, [2, 3, 5]);
        mat4.scale(worldMatrix, worldMatrix, [0.2,10,0.2]);
        mat4.rotate(worldMatrix, worldMatrix, Math.PI / 2.2, [0, 1, 0]);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, boxIndices.length,
                        gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        // ship

        mvPushMatrix();
        gl.bindTexture(gl.TEXTURE_2D, shipTexture);
        gl.activeTexture(gl.TEXTURE0);
        mat4.translate(worldMatrix, worldMatrix, [2, 0, 4]);
        mat4.rotate(worldMatrix, worldMatrix, Math.PI / 2.3, [0,1,0])
        mat4.rotate(worldMatrix, worldMatrix, Math.PI / 2, [1,0,0])
        mat4.scale(worldMatrix, worldMatrix, [2,2,2]);
        mat4.rotate(worldMatrix, worldMatrix, 3* Math.PI / 2, [1,0,0])
        mat4.rotate(worldMatrix, worldMatrix, ship_rotation, [0,1,0])
        gl.bindBuffer(gl.ARRAY_BUFFER, shipVertexBufferObject);
        gl.vertexAttribPointer(ship_positionAttribLocation,3,gl.FLOAT,gl.FALSE,
                               3 * Float32Array.BYTES_PER_ELEMENT, 0);

        gl.bindBuffer(gl.ARRAY_BUFFER, shipNormalBufferObject);

        gl.vertexAttribPointer(
          ship_normalAttribLocation,
          3, gl.FLOAT,
          gl.TRUE,
          3 * Float32Array.BYTES_PER_ELEMENT,
          0
        );

        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, shipIndexBufferObject);
        gl.vertexAttribPointer(ship_texCoordAttribLocation,2,
                                gl.FLOAT,
                                gl.FALSE,
                              2 * Float32Array.BYTES_PER_ELEMENT,
                              0);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, shipIndices.length,
                        gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        // Pirate

        mvPushMatrix();
        gl.bindTexture(gl.TEXTURE_2D, skinTexture);
        gl.activeTexture(gl.TEXTURE0);
        mat4.scale(worldMatrix, worldMatrix, [0.25,0.25,0.25]);
        mat4.translate(worldMatrix, worldMatrix, [12, 8, -12]);
        mat4.rotate(worldMatrix, worldMatrix, Math.PI / 2, [0,1,0])
        gl.bindBuffer(gl.ARRAY_BUFFER, pirateVertexBufferObject);
        gl.vertexAttribPointer(ship_positionAttribLocation,3,gl.FLOAT,gl.FALSE,
                               3 * Float32Array.BYTES_PER_ELEMENT, 0);
        gl.bindBuffer(gl.ARRAY_BUFFER, pirateNormalBufferObject);
        gl.vertexAttribPointer(
          ship_normalAttribLocation,
          3, gl.FLOAT,
          gl.TRUE,
          3 * Float32Array.BYTES_PER_ELEMENT,
          0
        );

        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, pirateIndexBufferObject);
        gl.vertexAttribPointer(ship_texCoordAttribLocation,2,
                                gl.FLOAT,
                                gl.FALSE,
                              2 * Float32Array.BYTES_PER_ELEMENT,
                              0);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, pirateIndices.length,
                        gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        mvPushMatrix()
        setMatrixUniforms();
        mat4.scale(worldMatrix, worldMatrix, [0.25,0.25,0.25]);
        mat4.translate(worldMatrix, worldMatrix, [11, 8, 0]);
        mat4.rotate(worldMatrix, worldMatrix, Math.PI / 2, [0,1,0]);
        setMatrixUniforms();
        gl.drawElements(gl.TRIANGLE_STRIP, pirateIndices.length,
                        gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();
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

    }
}

function handleKeyPress(event) {
    switch(event.key) {
    case 'j': ship_rotation += -0.1; break;
    case 'l': ship_rotation += 0.1; break;
    }
}

function handleKeyUp(event) {
    switch(event.key) {
    case 'a' : view_translate_x = 0; break;
    case 'd' : view_translate_x = 0; break;
    case 'w' : view_translate_z = 0; break;
    case 's' : view_translate_z = 0; break;
    }
}

function getFile(filename) {
  var request = new XMLHttpRequest();
  request.open("GET", filename, false);
  request.send(null);
  return request.responseText;
}
