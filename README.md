🐔 Chicken Invaders – Java Swing Game

A 2D arcade-style Chicken Invaders shooting game developed in Java (Swing).
This project focuses on both gameplay implementation and software engineering quality, demonstrating the application of Object-Oriented Programming (OOP), SOLID principles, and UML-based system design.

🎮 Gameplay Overview

Control a spaceship to defeat waves of invading chickens

Survive through infinite levels

Fight boss enemies with large HP and special attacks

Collect items to upgrade weapons and abilities

Compete for the highest score

⌨️ Controls
Key	Action
← / A	Move left
→ / D	Move right
↑ / W	Move up
↓ / S	Move down
Shooting	Automatic
P	Pause / Resume
ENTER	Start / Restart
ESC	Menu / Exit
🧠 Core Features

Infinite level progression

Normal enemies & boss enemies

Boss HP bar

Auto-fire weapon system

Item & power-up system

Shield & health management

High score persistence

Animated background (starfield)

Menu, Settings, Pause, Game Over states

🧱 Item & Weapon System
Item	Effect
❤️ Heart	Restore HP
🛡 Shield	Block one enemy hit
🔫 Laser	Laser weapon mode
🚀 Rocket	Rocket weapon mode
💥 Damage Up	Increase bullet damage
🔁 Double Shot	Fire two bullets
🔂 Triple Shot	Fire three bullets
🧨 Pierce	Bullets penetrate enemies
🧩 Software Design

This project is designed following SOLID principles:

SRP – Each class has a single responsibility

OCP – New enemies and features can be added without modifying existing code

LSP – Enemy subclasses are safely interchangeable

ISP – Small, focused interfaces (Drawable, Movable, Damageable)

DIP – Game logic depends on abstractions, not concrete classes

The system architecture is documented using UML diagrams, including:

Overall Class Diagram

Gameplay Core Diagram

State Diagram

Sequence Diagram

Item & Upgrade Diagram

Level Management Diagram

📁 Project Structure
src/
├── Game.java
├── GameWindow.java
├── GamePanel.java
├── Player.java
├── Enemy.java
├── NormalEnemy.java
├── BossEnemy.java
├── Bullet.java
├── EnemyBullet.java
├── Item.java
├── EnemyFactory.java
├── LevelManager.java
├── InfiniteLevelSpawner.java
├── HighScoreManager.java
├── SettingsManager.java
├── SoundManager.java
├── SpriteLoader.java
├── GameState.java
├── WeaponMode.java
├── ItemType.java

res/
├── images/
│   ├── player.png
│   ├── chicken.png
│   ├── boss1.png
│   ├── boss2.png
│   ├── boss3.png
│   └── items...
├── sounds/
│   ├── bgm.wav
│   ├── shoot.wav
│   └── explosion.wav

▶️ How to Run
Requirements

Java JDK 17+ (or JDK 11+)

Any Java IDE (IntelliJ, VS Code, Eclipse)

Run
javac Game.java
java Game


Or simply run Game.java from your IDE.

📊 Educational Purpose

This project was developed as an academic project to demonstrate:

Object-Oriented Design

SOLID Principles

UML → Code traceability

Game loop & collision handling

Scalable software architecture

🚀 Future Improvements

Additional enemy AI behaviors

More boss skills and attack patterns

Save/load game state

Multiplayer support

Migration to JavaFX or other game engines

👨‍💻 Author

Developer: [Your Name]

Institution: International University – VNU-HCM

Course: Object-Oriented Programming / Software Engineering
