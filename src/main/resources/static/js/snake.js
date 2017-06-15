/**
 * Created by victor on 11/06/2017.
 */
/**
 * JavaScript pour le jeu Snake des Opossums !
 */

function runSnake(difficulty, quickness, wallOption){
    var canvas = document.getElementById("snakeBoard");
    var ctx = canvas.getContext("2d");
    //Variables du Snake
    var direction = "right";
    var snakeSize = 20;
    var xSnake = [snakeSize];
    var ySnake = [snakeSize];
    var dx = 0;
    var dy = 0;
    opossum = new Image();
    opossum.src = 'http://rs1255.pbsrc.com/albums/hh631/Frostsong/Aywas/tavi_opossum.png~c200';
    var xOpossum = 0;
    var yOpossum = 0;
    var opossumSize = 40;
    var eaten = true;
    var vitesse = difficulty;
    var nbNewParts = quickness;
    var score = 1;
    var listen = true;
    var wait = false;
    var mur = wallOption;

    function draw(){
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        isDead();
        drawSnake();
        wait = false;
        drawOpossum();
        checkIfEaten();
        directionSnake();
        ctx.closePath();
    }
    var game = setInterval(draw, vitesse);

    $(document).keydown(function(e) {
        if(listen == false || wait == true){
            return "ok";
        }
        if(e.which == 37 && direction != "right"){
            direction = "left";
        }else if(e.which == 38 && direction != "down"){
            direction = "up";
        }else if(e.which == 39 && direction != "left"){
            direction = "right";
        }else if(e.which == 40 && direction != "up"){
            direction = "down";
        }else if(e.which == 27){
            endGame();
        }
        wait = true;
    });

    function drawSnake(){
        ctx.beginPath();
        checkWall();
        var gradient = 10;
        var color = 255;
        for(var x = 0 ; x<xSnake.length ; x++){
            color = 255 - 10;
            ctx.fillRect(xSnake[x], ySnake[x], snakeSize, snakeSize);
            ctx.fillStyle="#FF0000";
            ctx.fill();
            ctx.strokeStyle="#000000";
            ctx.strokeRect(xSnake[x], ySnake[x], snakeSize, snakeSize);
        }
    }

    function drawOpossum(){
        if(eaten){
            xOpossum = Math.random()*(canvas.width-opossumSize);
            yOpossum = Math.random()*(canvas.height-opossumSize);
            eaten = false;
        }
        ctx.drawImage(opossum, xOpossum, yOpossum, opossumSize, opossumSize);
    }

    function checkIfEaten(){
        if(xSnake[0] + snakeSize/2 >= xOpossum - opossumSize/2 && xSnake[0] - snakeSize/2 <= xOpossum + opossumSize/2){
            if(ySnake[0] - snakeSize/2 >= yOpossum - opossumSize/2 && ySnake[0] - snakeSize/2 <= yOpossum + opossumSize/2){
                addPartToSnake();
                eaten = true;
                score += nbNewParts;
            }
        }
    }

    function addPartToSnake(){
        for(v=0 ; v < nbNewParts ; v++){
            if(xSnake.length > 1){
                xSnake.unshift(xSnake[v]);
                ySnake.unshift(ySnake[v]);
            }else{
                xSnake.unshift(xSnake[0]);
                ySnake.unshift(ySnake[0]);
            }
        }
    }

    function directionSnake(){
        if(direction == "left"){
            dx = -snakeSize;
            dy = 0;
        }else if(direction == "right"){
            dx = snakeSize;
            dy = 0;
        }else if(direction == "up"){
            dy = -snakeSize;
            dx = 0;
        }else{
            dy = snakeSize;
            dx = 0;
        }
        for(var x=1 ; x < xSnake.length ; x++){
            xSnake[xSnake.length - x] = xSnake[xSnake.length - x - 1];
            ySnake[xSnake.length - x] = ySnake[xSnake.length - x - 1];
        }
        xSnake[0] += dx;
        ySnake[0] += dy;
    }

    function checkWall(){
        var touche = false;
        if(xSnake[0] + snakeSize/2 <= 0){
            xSnake[0] = canvas.width;
            touche = true;
        }else if(xSnake[0] + snakeSize/2 >= canvas.width){
            xSnake[0] = 0;
            touche = true;
        }else if(ySnake[0] + snakeSize/2 <= 0){
            ySnake[0] = canvas.height;
            touche = true;
        }else if(ySnake[0] + snakeSize/2 >= canvas.height){
            ySnake[0] = 0;
            touche = true;
        }
        if(mur && touche){
            endGame();
        }
    }

    function endGame(){
        scoreSnake(score);
        listen = false;
        clearInterval(game);
    }

    function isDead(){
        for(var x = 1 ; x < xSnake.length-1 ; x++){
            if(xSnake[0] == xSnake[x] && ySnake[0] == ySnake[x]){
                endGame();
            }
        }
    }

}

function menuSnake(){
    var canvas = document.getElementById("snakeBoard");
    var ctx = canvas.getContext("2d");
    var currentBtn = 0;
    var btns = ["Jouer", "Normale", "Extension normale", "Mur traversable"];
    var difficulty = ["Facile", "Normale", "Difficile", "Impossible"];
    var quickness = ["Extension normale", "Extension rapide", "Extension vitesse lumière"];
    var wallOptions = ["Mur traversable", "Mur non traversable"]
    var currentDif = 1;
    var currentQck = 0;
    var wallOption = 0;
    var listen = true;

    //Centre un texte
    function centerText(ctx, text, y) {
        var measurement = ctx.measureText(text);
        var x = (ctx.canvas.width - measurement.width) / 2;
        ctx.fillText(text, x, y);
    }

    function draw(){
        var y = canvas.height / btns.length;
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        ctx.fillStyle = 'white';
        ctx.font = '40px monospace';
        centerText(ctx, 'Snake des Opossums', y);
        for(var v = 0 ; v<btns.length ; v++){
            if(v == currentBtn){
                ctx.fillStyle = 'gold';
                ctx.font = '30px monospace';
            }else{
                ctx.fillStyle = 'white';
                ctx.font = '20px monospace';
            }
            var inscription = btns[v];
            if(v > 0){
                inscription = "< " + inscription + " >";
            }
            centerText(ctx, inscription, y+(v+1)*60);
        }
    }
    var interval = setInterval(draw, 10);

    $(document).keydown(function(e) {
        if(listen == false){
            return "ok";
        }
        if(e.which == 37){
            if(currentBtn == 1){
                currentDif -= 1;
                if(currentDif < 0){
                    currentDif = difficulty.length - 1;
                }
                btns[1] = difficulty[currentDif];
            }else if(currentBtn == 2){
                currentQck -= 1;
                if(currentQck < 0){
                    currentQck = quickness.length - 1;
                }
                btns[2] = quickness[currentQck];
            }else if(currentBtn == 3){
                wallOption -= 1;
                if(wallOption < 0){
                    wallOption = wallOptions.length - 1;
                }
                btns[3] = wallOptions[wallOption];
            }
        }else if(e.which == 38){
            currentBtn -= 1;
            if(currentBtn < 0){
                currentBtn = btns.length - 1;
            }
        }else if(e.which == 39){
            if(currentBtn == 1){
                currentDif += 1;
                if(currentDif > difficulty.length - 1){
                    currentDif = 0;
                }
                btns[1] = difficulty[currentDif];
            }else if(currentBtn == 2){
                currentQck += 1;
                if(currentQck > quickness.length - 1){
                    currentQck = 0;
                }
                btns[2] = quickness[currentQck];
            }else if(currentBtn == 3){
                wallOption += 1;
                if(wallOption > wallOptions.length - 1){
                    wallOption = 0;
                }
                btns[3] = wallOptions[wallOption];
            }
        }else if(e.which == 40){
            currentBtn += 1;
            if(currentBtn > btns.length-1){
                currentBtn = 0;
            }
        }else if(e.which == 13){
            if(currentBtn == 0){
                var dif = setDifficulty(currentDif);
                var qck = setQuickness(currentQck);
                runSnake(dif, qck, wallOption);
                listen = false;
                clearInterval(interval);
            }
        }
    });

    function setDifficulty(dif){
        if(dif == 0){
            return 100;
        }else if(dif == 1){
            return 60;
        }else if(dif == 2){
            return 40;
        }else if(dif == 3){
            return 20;
        }
    }

    function setQuickness(qck){
        if(qck == 0){
            return 1;
        }else if(qck == 1){
            return 3;
        }else if(qck == 2){
            return 10;
        }
    }
}

function scoreSnake(score){
    var canvas = document.getElementById("snakeBoard");
    var ctx = canvas.getContext("2d");
    var text_score = "Score : " + score.toString() + " points !";
    var listen = true;

    //Centre un texte
    function centerText(ctx, text, y) {
        var measurement = ctx.measureText(text);
        var x = (ctx.canvas.width - measurement.width) / 2;
        ctx.fillText(text, x, y);
    }

    function draw(){
        var y = canvas.height / 2 - 0;
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        ctx.fillStyle = 'white';
        ctx.font = '40px monospace';
        centerText(ctx, text_score, y);
        ctx.fillStyle = 'gold';
        ctx.font = '30px monospace';
        centerText(ctx, "Retour au menu", y+40);
    }
    var score = setInterval(draw, 10);

    $(document).keydown(function(e) {
        if(listen == false){
            return "ok";
        }
        if(e.which == 13){
            menuSnake();
            listen = false;
            clearInterval(score);
        }
    });
}

$(document).ready(function(){
    if($("#snakeBoard").length) {
        menuSnake();
    }
});