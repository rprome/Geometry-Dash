# Geometry Dash Clone

A custom implementation of the popular game **Geometry Dash**, where players navigate a character through challenging levels, avoiding obstacles and aiming to reach the finish line.

## Table of Contents
- [Project Description](#project-description)
- [Features](#features)
- [Game Design](#game-design)
- [Setup and Installation](#setup-and-installation)
- [How to Play](#how-to-play)
- [Development Process](#development-process)
- [Contributors](#contributors)
- [Citations](#citations)

## Project Description
This project is a custom version of **Geometry Dash**, featuring a start menu, sprite selection, and diverse obstacles like spikes, platforms, and double-jump stars. The player must complete the level without colliding with obstacles and is given unlimited attempts to succeed.

## Features
- **Start Menu**: Select your character sprite before starting the game.
- **Dynamic Obstacles**: Includes blocks, spikes, platforms, and double-jump stars.
- **Physics**: Gravity and smooth jumping mechanics.
- **Collision Detection**: Player interaction with objects results in game over or other effects.
- **Sound Effects and Music**: Background music, collision sounds, and level completion tunes.
- **Progress Indicator**: Displays the player's percentage progress through the level.
- **Pause and Resume**: Allows players to take breaks during gameplay.

## Game Design
The game is implemented using a single main class `GeoDash` and several helper classes:
- **Obstacle**: Generic obstacle base class.
- **TallObstacles**: Taller obstacles requiring precise jumps.
- **Star**: Enables double-jump mechanics.
- **Spikes**: Hazardous obstacles.
- **Platform**: Flat surfaces aiding navigation.
- **Win**: Signals level completion.

### Key Mechanics
- **Physics**: Gravity is implemented to create smooth jumping and falling dynamics.
- **Camera Movement**: The `cameraX` variable ensures the world moves relative to the player.
- **Collision Handling**: Determines interactions with different obstacles and updates the game state.
- **Visuals**: Sprites, background images, and fonts are integrated to enhance user experience.

## Setup and Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/rprome/geometry-dash-clone.git
   ```
2. Open the project in your preferred IDE (e.g., Eclipse or Visual Studio Code).
3. Ensure all required assets (images, sounds) are in the correct directories.
4. Run the `GeoDash` class to start the game.

## How to Play
1. **Start Menu**: Choose your character sprite and press the start button.
2. **Gameplay**:
   - Use the spacebar to jump.
   - Avoid obstacles to reach the end of the level.
   - Pause and resume the game using the button in the top-left corner.
3. **Winning**: Reach the end of the level without colliding with obstacles.

## Development Process
### Challenges
- Implementing smooth gravity and jumping mechanics.
- Handling collision detection for complex objects like spikes and platforms.
- Synchronizing background music and sound effects with game events.
- Formatting text across different IDEs and platforms.

### Solutions
- Used variables like `GRAVITY`, `JUMP_SPEED`, and `isJumping` to refine physics.
- Created arrays and methods for efficient collision detection.
- Employed threading to manage background music playback.
- Converted text messages into images to ensure consistent formatting.

## Contributors
- **Caitlin O’Leyar**
- **Aadiva Rajbhandary**
- **Rizouana Prome**
  
## Citations
- [Game Backgrounds](https://www.gamedevmarket.net/asset/lake-background)
- [Player Sprites](https://gdbrowser.com/iconkit/)
- [Sound Effects](https://pixabay.com/sound-effects/search/game/)
- [Fonts](https://www.dafont.com/)
- [Music](https://downloads.khinsider.com/game-soundtracks/album/geometry-dash/)

---
Enjoy playing our custom Geometry Dash game! Feedback and contributions are welcome.
