var game;

function setup() {
	createCanvas(windowWidth, windowHeight);
	ship = new Ship(width / 2, height / 2);
	scoreBoard = new ScoreBoard(width - 10, 20);
	game = new Game(ship, scoreBoard);
	game.reset();
	game.setDebug(true);
}

function draw() {
	if (game.isActive()) {
		background(255);
		// create new meteors
		game.createNewMeteor();
		// update position of and display stuff (meteors, projectiles, ship)
		game.updateAndDisplayMeteors();
		game.updateAndDisplayProjectiles();
		game.updateAndDisplayShip();
		// display the scoreboard
		game.displayScoreboard();
		// remove projectiles that passed the top of screen
		game.removeLostProjectiles();
		// detect successfull shots (projectile hits meteor)
		// after a successfull shoot, projectile and meteor will be marked as "dead"
		game.detectSuccessfullShots();
		// remove "dead" meteors and projectiles
		game.removeKilledMeteors();
		game.removeUsedProjectiles();
		// if a meteor hits the ground, it's game over.
		game.stopIfMeteorHitGround();
		// show debug info when enables
		game.showDebugInfo();
	} else {
		game.showWelcomeScreen();
	}
}

function mouseClicked() {
	// when the game is active, clicking the mouse shots
	if (game.gameActive)
		game.shoot();
	// when the game is inactive, clicking the mouse restarts the game
	else {
		game.reset();
		game.start();
	}
}