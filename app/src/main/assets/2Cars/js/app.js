/*
 * Javascript file for 2 cars
 */

// Constants
var WINDOW_WIDTH = window.innerWidth;
var WINDOW_HEIGHT = window.innerHeight;
var FPS = 60;
var SPEED = 1;
var GAME_STATE = "stopped";
var INTERVAL_ID;
var TIMER_INTERVAL;
var OBSTACLES_INTERVAL;
var BACKGROUND_COLOR = "#25337a";
var LINE_COLOR = "#6572a7";
var SOUNDS = true;
var SCORE = 0;

// Images
var CRASH = new Image();
CRASH.src = "img/crash.png";

var EXCLAIM = new Image();
EXCLAIM.src = "img/exclaim.png";

// Car Constants
var RED_CAR = new Image();
RED_CAR.src = "img/redcar.png";

var BLUE_CAR = new Image();
BLUE_CAR.src = "img/bluecar.png";

// Constructor function for car
function Car(type) {
    if(type == "red") {
        this.type = "RED_CAR";
        this.imgObj = RED_CAR;
        this.position = "left";
        this.x = Math.floor(WINDOW_WIDTH/2) - 180;
        this.y = Math.floor(WINDOW_HEIGHT) - 180;
    } else if(type == "blue") {
        this.type = "BLUE_CAR";
        this.imgObj = BLUE_CAR;
        this.position = "right";
        this.x = Math.floor(WINDOW_WIDTH/2) + 120;
        this.y = Math.floor(WINDOW_HEIGHT) - 180;
    }

    this.draw = function () {
        ctx.drawImage(this.imgObj, this.x, this.y);
    };

    this.update = function () {
        if(this.position == "left") {
            this.position = "right";
            var that1 = this;
            for(var i=1; i<=20; i++) {
                setTimeout(function () {
                    that1.x += 5;
                }, 5*i);
            }
        } else {
            var that2 = this;
            this.position = "left";
            for(var j=1; j<=20; j++) {
                setTimeout(function () {
                    that2.x -= 5;
                }, 5*j);
            }
        }
    };
}

// Instantiations
var RED_CAR_OBJ = new Car("red");
var BLUE_CAR_OBJ = new Car("blue");

// Obstacle constants
var RED_OBSTACLES = [];
var BLUE_OBSTACLES = [];
var RED_OBSTACLES_INTERVAL_ID;
var BLUE_OBSTACLES_INTERVAL_ID;

function Obstacle(color) {
    this.type = Math.floor((Math.random()*2)) ? "circle" : "square";
    this.position = Math.floor((Math.random()*2)) ? "left" : "right";
    if(this.type == "circle") {
        this.x = Math.floor(WINDOW_WIDTH/2) - 150;
        if(this.position == "right") {
            this.x += 100;
        }
        if(color == "blue") {
            this.x += 200;
        }
    } else if(this.type == "square") {
        this.x = Math.floor(WINDOW_WIDTH/2) - 162.5;
        if(this.position == "right") {
            this.x += 100;
        }
        if(color == "blue") {
            this.x += 200;
        }
    }
    this.y = -100;
    if(color == "red") {
        this.color = "#FE3E67";
    } else if(color == "blue") {
        this.color = "#05A8C4";
    }
    this.alive = true;

    this.draw = function () {
        if(this.type == "circle") {
            ctx.beginPath();
            ctx.arc(this.x, this.y, 25, 0, 2*Math.PI);
            ctx.fillStyle = this.color;
            ctx.fill();

            ctx.beginPath();
            ctx.arc(this.x, this.y, 25*0.8, 0, 2*Math.PI);
            ctx.fillStyle = '#ffffff';
            ctx.fill();

            ctx.beginPath();
            ctx.arc(this.x, this.y, 25*0.5, 0, 2*Math.PI);
            ctx.fillStyle = this.color;
            ctx.fill();
        } else {
            ctx.lineJoin = "round";
            ctx.lineWidth = 20;
            ctx.strokeStyle = this.color;
            ctx.strokeRect(this.x, this.y, 25, 25);
            ctx.fillStyle = this.color;
            ctx.fillRect(this.x, this.y, 25, 25);

            ctx.lineWidth = 10;
            ctx.strokeStyle = "#ffffff";
            ctx.strokeRect(this.x + 1, this.y + 1, 25 - 1.5, 25 - 1.5);
            ctx.fillStyle = "#ffffff";
            ctx.fillRect(this.x + 1, this.y + 1, 25 - 1.5, 25 - 1.5);

            ctx.fillStyle = this.color;
            ctx.fillRect(this.x + 3, this.y + 3, 25 - 6, 25 - 6);
        }
    };

    this.update = function () {
        this.y += 5 + Math.floor(SPEED);

        // Remove elements after crossing the viewport
        if(this.y > WINDOW_HEIGHT) {
            this.alive = false;
        }

        // Handle with circle obstacles
        if(this.type == "circle" && this.alive) {
            if(color == "red") {
                if(RED_CAR_OBJ.x < (this.x - 20) + 40 && RED_CAR_OBJ.x + 60 > (this.x - 20) && RED_CAR_OBJ.y < (this.y - 20) + 40 && RED_CAR_OBJ.y + 50 > (this.y - 20)) {
                    this.alive = false;
                    SCORE += 1;
                    document.getElementById("score").innerHTML = SCORE.toString();
                    if(SOUNDS) {
                        document.getElementById("pop1").pause();
                        document.getElementById("pop1").currentTime = 0;
                        document.getElementById("pop1").play();
                    }
                }
            } else if(color == "blue") {
                if(BLUE_CAR_OBJ.x < (this.x - 20) + 40 && BLUE_CAR_OBJ.x + 60 > (this.x - 20) && BLUE_CAR_OBJ.y < (this.y - 20) + 40 && BLUE_CAR_OBJ.y + 50 > (this.y - 20)) {
                    this.alive = false;
                    SCORE += 1;
                    document.getElementById("score").innerHTML = SCORE.toString();
                    if(SOUNDS) {
                        document.getElementById("pop1").pause();
                        document.getElementById("pop1").currentTime = 0;
                        document.getElementById("pop1").play();
                    }
                }
            }
//            if(this.y > WINDOW_HEIGHT - 50) {
//                ctx.drawImage(EXCLAIM, this.x - 25, this.y - 90);
//                if(SOUNDS){
//                    document.getElementById("pop2").play();
//                }
//                stop();
//            }
        }

        // Handle with square obstacles
        if(this.type == "square" && this.alive) {
            if(color == "red") {
                if(RED_CAR_OBJ.x < (this.x - 20) + 60 && RED_CAR_OBJ.x + 60 > (this.x - 20) && RED_CAR_OBJ.y < (this.y - 20) + 60 && RED_CAR_OBJ.y + 50 > (this.y - 20)) {
                    ctx.drawImage(CRASH, this.x - 65, this.y - 50);
                    if(SOUNDS) {
                        document.getElementById("pop3").play();
                    }
                    stop();
                }
            } else if(color == "blue") {
                if(BLUE_CAR_OBJ.x < (this.x - 20) + 60 && BLUE_CAR_OBJ.x + 60 > (this.x - 20) && BLUE_CAR_OBJ.y < (this.y - 20) + 60 && BLUE_CAR_OBJ.y + 50 > (this.y - 20)) {
                    ctx.drawImage(CRASH, this.x - 65, this.y - 50);
                    if(SOUNDS) {
                        document.getElementById("pop3").play();
                    }
                    stop();
                }
            }
        }
    }
}

function obstaclesGenerator() {
    RED_OBSTACLES_INTERVAL_ID = setInterval(function () {
        var obstacle = new Obstacle("red");
        RED_OBSTACLES.push(obstacle);
    }, 800);
    BLUE_OBSTACLES_INTERVAL_ID = setInterval(function () {
        var obstacle = new Obstacle("blue");
        BLUE_OBSTACLES.push(obstacle);
    }, 800);
}

function obstaclesManager() {
    RED_OBSTACLES.forEach(function (obstacle, index, object) {
        if(!obstacle.alive) {
            object.splice(index, 1);
        }
    });
    BLUE_OBSTACLES.forEach(function (obstacle, index, object) {
        if(!obstacle.alive) {
            object.splice(index, 1);
        }
    });
    if(SPEED < 5) {
        SPEED += 0.005;
    }
}

// Canvas constants
var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");
canvas.width = WINDOW_WIDTH;
canvas.height = WINDOW_HEIGHT;
canvas.style.backgroundColor = BACKGROUND_COLOR;

function clearCanvas() {
    ctx.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
}

function drawLanes() {
    ctx.fillStyle = LINE_COLOR;
    ctx.fillRect(Math.floor(WINDOW_WIDTH/2) - 2.5, 0, 5, WINDOW_HEIGHT);
    ctx.fillRect(Math.floor(WINDOW_WIDTH/2) - 100, 0, 2, WINDOW_HEIGHT);
    ctx.fillRect(Math.floor(WINDOW_WIDTH/2) + 100, 0, 2, WINDOW_HEIGHT);
    ctx.fillRect(Math.floor(WINDOW_WIDTH/2) - 200, 0, 2, WINDOW_HEIGHT);
    ctx.fillRect(Math.floor(WINDOW_WIDTH/2) + 200, 0, 2, WINDOW_HEIGHT);
}

function drawCars() {
    RED_CAR_OBJ.draw();
    BLUE_CAR_OBJ.draw();
}

function drawObstacles() {
    RED_OBSTACLES.forEach(function (obstacle) {
        obstacle.update();
        obstacle.draw();
    });
    BLUE_OBSTACLES.forEach(function (obstacle) {
        obstacle.update();
        obstacle.draw();
    });
}

function draw() {
    clearCanvas();
    drawLanes();
    drawObstacles();
    drawCars();
}

var i= 0, j=0;
function startTimer() {
    i++;
    var curr= document.getElementById("time");
    if(j<1) {
        if(i<10) {
            curr.innerHTML="00:0"+i;
        } else {
            if (i > 59) {
                j++;
                i=0;
                curr.innerHTML = "0"+j+":" + "0"+i;
            } else {
                curr.innerHTML = "0"+j+":" + i;
            }
        }
    }
    else {
        if(i<10) {
            curr.innerHTML="0"+j+":0"+i;
        } else {
            if (i > 59) {
                j++;
                i=0;
                curr.innerHTML = "0"+j+":" + "0"+i;
            }
            else {
                curr.innerHTML = "0"+j+":" + i;
            }
        }
    }
}

function start() {
    GAME_STATE = "started";
    obstaclesGenerator();
    TIMER_INTERVAL = setInterval(startTimer, 1000);
    INTERVAL_ID = setInterval(draw, 1000/FPS);
    OBSTACLES_INTERVAL = setInterval(obstaclesManager, 200);

    localStorage.setItem("autostart", "false");
    document.getElementById("initial").className = "";
    setTimeout(function () {
        document.getElementById("initial").style.display = "none";
    }, 500);
}

function stop() {
    GAME_STATE = "full_stop";
    clearInterval(OBSTACLES_INTERVAL);
    clearInterval(INTERVAL_ID);
    clearInterval(TIMER_INTERVAL);
    Android.sendData(SCORE);

    if(SCORE > localStorage.getItem("highscore")) {
        localStorage.setItem("highscore", SCORE);
    }
    document.getElementById("scores").innerHTML = "SCORE: " + SCORE;
    document.getElementById("final").style.display = "block";
    setTimeout(function () {
        document.getElementById("final").className = "visible";
    }, 1000);
     setTimeout(function(){
        Android.showAd();

       }, 1000);
}

function restart() {
    localStorage.setItem("autostart", "true");
    location.reload();
}

var BB,BBoffsetX,BBoffsetY;
setBB();

canvas.ontouchstart = handleMousedown;

function handleMousedown(e){
    e.preventDefault();
    e.stopPropagation();
    var mx=e.touches[0].clientX-BBoffsetX;
    var my=e.touches[0].clientY-BBoffsetY;
    
    if (mx <= WINDOW_WIDTH/2){
        RED_CAR_OBJ.update();
    } else{
        BLUE_CAR_OBJ.update();
    }
}

function setBB(){
    BB=canvas.getBoundingClientRect();
    BBoffsetX=BB.left;
    BBoffsetY=BB.top;
}

window.onload = function () {
    drawLanes();
    drawCars();

    document.getElementById("black").className = "";
    setTimeout(function () {
        document.getElementById("black").style.display = "none";
    }, 500);

    // Local Storage Handlers
    if(localStorage.getItem("highscore") == null) {
        localStorage.setItem("highscore",0);
    }
    if(localStorage.getItem("autostart") == "false" || localStorage.getItem("autostart") == null) {
        document.getElementById("initial").style.display = "block";
        document.getElementById("initial").className = "visible";
    } else {
        start();
    }
    
    // Keyboard Handlers
    document.addEventListener("keyup", function (e) {
        if(e.keyCode == 90) {
            RED_CAR_OBJ.update();
        } else if(e.keyCode == 77) {
            BLUE_CAR_OBJ.update();
        } else if(e.keyCode == 32) {
            if(GAME_STATE == "stopped") {
                start();
            } else if(GAME_STATE == "started") {
                stop();
            } else {
                restart();
            }
        }
    }, false);

    // Click Handlers
    document.getElementById("replay").onclick = function () {
        restart();
    };
    document.getElementById("start").onclick = function () {
        start();
    };
};
