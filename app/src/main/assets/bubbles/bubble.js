function Bubble(x, y, size, speed, col, crazyness) {
	// position
	this.x = x;
	this.y = y;
	this.size = size;
	this.speed = speed;
	this.col = col;
	// should we delete?
	this.alive = true;
	// crazy fx
	this.crazyness = crazyness;

	// schedule ball for destroyal
	this.destroy = function() {
		this.alive = false;
	}

	this.setSpeed = function(newSpeed) {
		this.speed = newSpeed;
	}

	this.display = function() {
		strokeWeight(1);
		stroke(0);
		fill(this.col);
		ellipse(this.x, this.y, this.size, this.size);
	};

	// move the ball down (towards the ground)	
	this.move = function() {
		this.y += this.speed;
		this.size += random(-this.crazyness, this.crazyness);
	};

	// detects intersection with another Bubble()	
	this.intersects = function(other) {
		d = dist(this.x, this.y, other.x, other.y);
		r1 = this.size / 2;
		r2 = other.size / 2;
		if (d < r1 + r2)
			return true
		else
			return false
	}
}