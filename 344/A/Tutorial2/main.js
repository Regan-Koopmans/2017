var worldMatrix = mat4.create();
var mvMatrixStack = [];
var viewMatrix = mat4.create();
var projMatrix = mat4.create();
var gl;
var canvas;
var matWorldUniformLocation;
var matViewUniformLocation;
var matProjUniformLocation;
var rotate_direction = 1;
var is_house = false;

var x_is_down = false;
var y_is_down = false;
var z_is_down = false;

var a_is_down = false;
var b_is_down = false;
var c_is_down = false;

var one_is_down = false;
var two_is_down = false;

var box_scale_x = 1;
var box_scale_y = 1;
var box_scale_z = 1;

var pyramid_shear_x = 0;
var pyramid_shear_y = 0;
var pyramid_shear_z = 0;

// Scaling Functions

function scaleX(matrix, amount) {
    var translation_matrix = mat4.create();
    translation_matrix[0] = amount
    mat4.multiply(matrix, matrix, translation_matrix);
}

function scaleY(matrix, amount) {
    var translation_matrix = mat4.create();
    translation_matrix[5] = amount
    mat4.multiply(matrix, matrix, translation_matrix);
}

function scaleZ(matrix, amount) {
    var translation_matrix = mat4.create();
    translation_matrix[10] = amount
    mat4.multiply(matrix, matrix, translation_matrix);
}


// Shearing Functions

function shearX(matrix, angle) {
    var translation_matrix = mat4.create();
    translation_matrix[4] = 1 / Math.tan(angle);
    mat4.multiply(matrix, matrix, translation_matrix);
}

function shearY(matrix, angle) {
    var translation_matrix = mat4.create();
    translation_matrix[1] = 1 / Math.tan(angle);
    mat4.multiply(matrix, matrix, translation_matrix);
}

function shearZ(matrix, angle) {
    var translation_matrix = mat4.create();
    translation_matrix[8] = 1 / Math.tan(angle);
    mat4.multiply(matrix, matrix, translation_matrix);
}

// Translate Function

function translate(matrix, vector) {
    if (vector != null) {
        var translation_matrix = mat4.create();
        translation_matrix[12] = vector[0];
        translation_matrix[13] = vector[1];
        translation_matrix[14] = vector[2];
        mat4.multiply(matrix, matrix, translation_matrix);
    }
}

// Rotate functions

function rotateX(m, a, angle) {
    var sin = Math.sin(angle),
        cos = Math.cos(angle);
    
    var a00 = a[0],
        a01 = a[1],
        a02 = a[2],
        a03 = a[3],
        a10 = a[4],
        a11 = a[5],
        a12 = a[6],
        a13 = a[7],
        a20 = a[8],
        a21 = a[9],
        a22 = a[10],
        a23 = a[11];

    m[4] = a10 * cos - a20 * sin;
    m[5] = a11 * cos - a21 * sin;
    m[6] = a12 * cos - a22 * sin;
    m[7] = a13 * cos - a23 * sin;

    m[8] = a10 * sin + a20 * cos;
    m[9] = a11 * sin + a21 * cos;
    m[10] = a12 * sin + a22 * cos;
    m[11] = a13 * sin + a23 * cos;
}

function rotateY(m, a, angle) {

    var sin = Math.sin(angle),
        cos = Math.cos(angle);
    
    var a00 = a[0],
        a01 = a[1],
        a02 = a[2],
        a03 = a[3],
        a10 = a[4],
        a11 = a[5],
        a12 = a[6],
        a13 = a[7],
        a20 = a[8],
        a21 = a[9],
        a22 = a[10],
        a23 = a[11];

        m[0] = a00 * cos - a20 * sin;
        m[1] = a01 * cos - a21 * sin;
        m[2] = a02 * cos - a22 * sin;
        m[3] = a03 * cos - a23 * sin;
        m[8] = a00 * sin + a20 * cos;
        m[9] = a01 * sin + a21 * cos;
        m[10] = a02 * sin + a22 * cos;
        m[11] = a03 * sin + a23 * cos;
}

function rotateZ(m, a, angle) {
    var sin = Math.sin(angle),
        cos = Math.cos(angle);
    
    var a00 = a[0],
        a01 = a[1],
        a02 = a[2],
        a03 = a[3],
        a10 = a[4],
        a11 = a[5],
        a12 = a[6],
        a13 = a[7],
        a20 = a[8],
        a21 = a[9],
        a22 = a[10],
        a23 = a[11];

    m[0] = a00 * cos - a10 * sin;
    m[1] = a01 * cos - a11 * sin;
    m[2] = a02 * cos - a12 * sin;
    m[3] = a03 * cos - a13 * sin;

    m[4] = a00 * sin + a10 * cos;
    m[5] = a01 * sin + a11 * cos;
    m[6] = a02 * sin + a12 * cos;
    m[7] = a03 * sin + a13 * cos;
}

var init = function() {
    canvas = document.getElementById("canvas-gl");
    gl = canvas.getContext("webgl");

    if (!gl) {
        alert("Could not get WebGL context!");
    }

    // This code sets the canvas and context to the window size.
    
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
      document.addEventListener("keypress", handleKeyPress);
      document.addEventListener("keydown", handleKeyDown);
      document.addEventListener("keyup", handleKeyUp);
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
    mat4.perspective(projMatrix, glMatrix.toRadian(45), canvas.width/canvas.height, 0.1, 1000.0);
    gl.uniformMatrix4fv(matWorldUniformLocation, gl.FALSE, worldMatrix);
    gl.uniformMatrix4fv(matViewUniformLocation,  gl.FALSE, viewMatrix);
    gl.uniformMatrix4fv(matProjUniformLocation,  gl.FALSE, projMatrix);

    ///
    /// MAIN RENDER LOOP
    ///
    var identityMatrix = new Float32Array(16);
    mat4.identity(identityMatrix);
    var angle_box = 0;
    var angle_pyramid = 0;
    var angle_house_x = 0;
    var angle_house_y = 0;
    gl.clearColor(0.9, 0.9, 0.9, 1.0);
    var loop = function() {
        // angle = performance.now() / 1000 / 6 * 2 * Math.PI;
        
        
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, projMatrix);
        mat4.identity(worldMatrix);

        if (is_house) {
                rotateX(worldMatrix, identityMatrix, angle_house_x);       
        }

        /// BOX //////////

        mvPushMatrix();   
            
            if (is_house) {
                if (one_is_down) {
                    angle_house_y =  angle_house_y + rotate_direction*(1/128)*Math.PI % 4;
                }
            } else {
                translate(worldMatrix, [2, 0, 0]);
                
                if (one_is_down) {
                    angle_box =  angle_box + rotate_direction*(1/128)*Math.PI % 4;
                }
                rotateY(worldMatrix, identityMatrix, angle_box);

                if (x_is_down) {
                    box_scale_x += 0.1;
                } else {
                    if (box_scale_x > 1) {
                        box_scale_x -= 0.2 * (box_scale_x - 1);
                    }
                }

                if (y_is_down) {
                    box_scale_y += 0.1;
                } else {
                    if (box_scale_y > 1) {
                        box_scale_y -= 0.2 * (box_scale_y - 1);
                    }
                }

                if (z_is_down) {
                    box_scale_z += 0.1;
                } else {
                    if (box_scale_z > 1) {
                        box_scale_z -= 0.2 * (box_scale_z - 1);
                    }
                }
                scaleX(worldMatrix, box_scale_x);
                scaleY(worldMatrix, box_scale_y);
                scaleZ(worldMatrix, box_scale_z);
            }


            gl.bindBuffer(gl.ARRAY_BUFFER, boxVertexBufferObject);
            gl.vertexAttribPointer(box_positionAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT, 0);
            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, boxIndexBufferObject);
            gl.vertexAttribPointer(box_colorAttribLocation,3,gl.FLOAT,gl.FALSE,6 * Float32Array.BYTES_PER_ELEMENT,3 * Float32Array.BYTES_PER_ELEMENT);   
            setMatrixUniforms();
            gl.drawElements(gl.TRIANGLE_STRIP, boxIndices.length, gl.UNSIGNED_SHORT, 0);
        mvPopMatrix();

        //// PYRAMID ////////////////////

       // if (is_house) {
       //          rotateX(worldMatrix, identityMatrix, angle_house_x);
       //          rotateY(worldMatrix, identityMatrix, angle_house_y);
       //  }

        mvPushMatrix();


            if (is_house) {
                translate(worldMatrix, [0, 1, 0.0]);
                if (two_is_down) {
                    angle_house_x =  angle_house_x + rotate_direction*(1/128)*Math.PI % 4;
                }
            } else {
                translate(worldMatrix, [-2, 0, 0.0]);

                if (two_is_down) {
                    angle_pyramid =  angle_pyramid + rotate_direction*(1/128)*Math.PI % 4;
                }
                rotateX(worldMatrix, identityMatrix, angle_pyramid);

                scaleY(worldMatrix, 2);

                if (a_is_down) {
                        pyramid_shear_x -= 0.01;

                } else {
                    pyramid_shear_x = 0;
                }

                if (b_is_down) {
                        pyramid_shear_y -= 0.01;

                } else {
                    pyramid_shear_y = 0;
                }

                if (c_is_down) {
                        pyramid_shear_z -= 0.01;

                } else {
                    pyramid_shear_z = 0;
                }


                if (pyramid_shear_x != 0) {
                    shearX(worldMatrix, Math.PI - pyramid_shear_x);
                }

                if (pyramid_shear_y != 0) {
                    shearY(worldMatrix, Math.PI - pyramid_shear_y);
                }

                if (pyramid_shear_z != 0) {
                    shearZ(worldMatrix, Math.PI - pyramid_shear_z);
                }

            }

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

function get_shader_text(id) {
    text = document.getElementById(id).innerHTML;
    return text;
}

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

function handleKeyPress(event) {
    console.log(event.key);
    switch(event.key) {
        case 'r' : rotate_direction *= -1; break;
        case 'h' : is_house = !is_house; break;
    }
}

function handleKeyDown(event) {
    console.log(event.key);
    switch(event.key) {
        case 'x' : x_is_down = true; break;
        case 'y' : y_is_down = true; break;
        case 'z' : z_is_down = true; break;
        case 'a' : a_is_down = true; break;
        case 'b' : b_is_down = true; break;
        case 'c' : c_is_down = true; break;
        case '1' : one_is_down = true; break;
        case '2' : two_is_down = true; break;

    }
}

function handleKeyUp(event) {
    console.log(event.key);
    switch(event.key) {
        case 'x' : x_is_down = false; break;
        case 'y' : y_is_down = false; break;
        case 'z' : z_is_down = false; break;
        case 'a' : a_is_down = false; break;
        case 'b' : b_is_down = false; break;
        case 'c' : c_is_down = false; break;
        case '1' : one_is_down = false; break;
        case '2' : two_is_down = false; break;
    }
}
