function ScoreBoard(x,y) {
	// position
	this.x = x;
	this.y = y;
	// initial score
	this.score = 0;
	var maxscore = 0;
	this.display = function() {
		noStroke();
		fill(0);
		textAlign(RIGHT);
		textFont("Courier new");
		textSize(22);
		text("score: " + this.score,this.x,this.y);
	};
	
	this.incrementScore = function() {

		this.score++;
		if(maxscore<this.score)
       { Android.sendData(this.score);
          maxscore = this.score;
       }


	};
	
	this.reset = function() {

		this.score = 0;
	}
}