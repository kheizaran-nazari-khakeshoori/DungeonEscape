# 🏰 Dungeon Escape

A turn-based dungeon crawler RPG built with Java Swing, demonstrating advanced Object-Oriented Programming principles including inheritance, polymorphism, encapsulation, and composition.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation & Running](#installation--running)
- [How to Play](#how-to-play)
- [Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)

## 🎮 Overview

Dungeon Escape is a Java-based RPG where players navigate through procedurally generated dungeon levels, fighting enemies, avoiding traps, collecting items, and managing resources. The game features multiple playable characters, each with unique abilities, a variety of enemies with different strengths and weaknesses, and a dynamic combat system with status effects.

**Educational Purpose:** This project demonstrates proper application of OOP concepts including:
- **Encapsulation**: Private fields with controlled access through getters/setters
- **Abstraction**: Abstract classes (`Player`, `Enemy`) and interfaces (`Iwarrior`, `IEffectable`, `Effect`)
- **Inheritance**: Class hierarchies for players, enemies, items, and traps
- **Polymorphism**: Method overriding and interface implementation
- **Composition**: Objects containing other objects (e.g., `Player` has `Inventory`, `EffectManager`)
- **Information Hiding**: Internal implementation details hidden behind public APIs

## ✨ Features

### Core Gameplay
- **Multiple Playable Characters**: Choose from Elfo, Bean, or Lucy, each with unique stats and special abilities
- **Turn-Based Combat**: Strategic combat system with attack, special abilities, item usage, and flee options
- **Dynamic Enemy System**: 8+ enemy types with unique behaviors, damage types, weaknesses, and resistances
- **Status Effects**: Poison, invisibility, healing over time, and defensive buffs
- **Trap System**: Spike traps, poison dart traps, and mimic chests
- **Inventory Management**: Collect weapons, potions, and special items
- **Shop System**: Purchase items and equipment between levels
- **Progressive Difficulty**: Enemy stats scale with each level

### Technical Features
- **Factory Pattern**: For creating items, traps, and levels
- **Manager Pattern**: Separate managers for combat, traps, doors, items, and levels
- **Effect System**: Flexible generic effect system that can be applied to any warrior
- **Rule Engine**: Configurable game rules and probabilities
- **MVC Architecture**: Clear separation between Model, View, and Controller

## 🔧 Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: Version 3.6.0 or higher
- **Operating System**: Windows, macOS, or Linux

## 🚀 Installation & Running

### Option 1: Using Maven (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd DungeonEscape
   ```

2. **Compile the project**
   ```bash
   mvn clean compile
   ```

3. **Run the game**
   ```bash
   mvn exec:java
   ```

### Option 2: Build JAR and Run

1. **Build executable JAR with dependencies**
   ```bash
   mvn clean package assembly:single
   ```

2. **Run the JAR file**
   ```bash
   java -jar target/job-quest-game-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

### Option 3: IDE (IntelliJ IDEA / Eclipse)

1. Import as Maven project
2. Wait for dependencies to download
3. Run `com.dungeonescape.Main` class

## 🎯 How to Play

### Game Start
1. Launch the game and select your character from the character selection screen
2. Each character has different stats and special abilities:
   - **Elfo**: 75 HP, high flee/disarm chance, precise shot ability
   - **Bean**: Balanced stats with unique abilities
   - **Lucy**: Specialized combat abilities

### Game Controls
- **Attack**: Click the Attack button to perform a basic attack
- **Special Ability**: Use your character's unique special ability (3-turn cooldown)
- **Use Item**: Open inventory and select potions or items to use
- **Flee**: Attempt to escape from combat (30% base chance)
- **Navigate**: Click on doors to progress through the dungeon

### Combat System
- Each enemy has specific damage types (Physical, Fire, Poison, Piercing, Magic)
- Enemies may have weaknesses (take extra damage) or resistances (take reduced damage)
- Status effects persist between turns and modify combat outcomes
- Manage your health and resources carefully - healing items are limited

### Progression
- Clear each level by defeating enemies and avoiding traps
- Collect gold from defeated enemies
- Visit shops to purchase items and equipment
- Enemy difficulty increases with each level

### Winning Condition
- Survive through all dungeon levels and defeat the final boss

## 🏗️ Architecture

### MVC Pattern
The project follows the Model-View-Controller architectural pattern:

```
┌─────────────────────────────────────────────────────┐
│                       VIEW                           │
│  (UI Components: Panels, Dialogs, Windows)          │
│  GameWindow, DungeonPanel, InventoryPanel, etc.     │
└────────────────┬────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────┐
│                    CONTROLLER                        │
│  (Game Logic Managers)                              │
│  CombatManager, LevelManager, ItemUsageManager      │
└────────────────┬────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────┐
│                      MODEL                           │
│  (Game Entities & Data)                             │
│  Player, Enemy, Item, Trap, Effect, Inventory       │
└─────────────────────────────────────────────────────┘
```

### Class Hierarchy

#### Players
```
Player (abstract)
├── Elfo
├── Bean
└── Lucy
```

#### Enemies
```
Enemy (abstract)
├── Goblin
├── SkeletonWarrior
├── Ghost
├── PoisonSpider
├── ShadowAssassin
├── StoneMan
├── SlimeBlob
└── MimicChest
```

#### Items
```
Item (abstract)
├── Weapon
└── Potion (abstract)
    ├── Antidote
    ├── InvisibilityPotion
    └── StaminaElixir
```

## 🎨 Design Patterns

### 1. **Factory Pattern**
- `ItemFactory`: Creates different types of items
- `TrapFactory`: Generates traps for levels
- `LevelFactory`: Builds complete dungeon levels

### 2. **Strategy Pattern**
- Different enemy types implement varied attack strategies
- Effect system allows different behaviors to be applied dynamically

### 3. **Template Method Pattern**
- Abstract `Player` and `Enemy` classes define template methods
- Subclasses override specific behaviors (e.g., `useSpecialAbility()`)

### 4. **Observer Pattern (Implicit)**
- UI components observe game state changes
- `UIStateManager` coordinates UI updates

### 5. **Manager Pattern**
- `CombatManager`: Handles combat logic
- `LevelManager`: Manages level progression
- `TrapManager`: Handles trap interactions
- `ItemUsageManager`: Manages item usage
- `EffectManager`: Manages status effects

### 6. **Composition Over Inheritance**
- Players and enemies compose `EffectManager` and `RuleEngine`
- `Player` contains `Inventory` and `Weapon`
- Flexible and maintainable design

## 📁 Project Structure

```
DungeonEscape/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/dungeonescape/    # Main application entry
│   │   │   │   ├── Game.java         # Core game controller
│   │   │   │   └── Main.java         # Application entry point
│   │   │   ├── controller/           # Game logic controllers
│   │   │   │   ├── CombatManager.java
│   │   │   │   ├── LevelManager.java
│   │   │   │   ├── TrapManager.java
│   │   │   │   ├── ItemUsageManager.java
│   │   │   │   ├── DoorManager.java
│   │   │   │   ├── EffectManager.java
│   │   │   │   └── RuleEngine.java
│   │   │   ├── model/                # Game entities
│   │   │   │   ├── Player.java       # Abstract player class
│   │   │   │   ├── Enemy.java        # Abstract enemy class
│   │   │   │   ├── Item.java         # Item hierarchy
│   │   │   │   ├── Effect.java       # Effect interface
│   │   │   │   ├── Inventory.java
│   │   │   │   └── ... (character and enemy implementations)
│   │   │   ├── view/                 # UI components
│   │   │   │   ├── GameWindow.java
│   │   │   │   ├── DungeonPanel.java
│   │   │   │   ├── InventoryPanel.java
│   │   │   │   ├── StatusPanel.java
│   │   │   │   └── ... (other UI panels)
│   │   │   ├── utils/                # Utilities
│   │   │   │   └── DiceRoller.java
│   │   │   └── exceptions/           # Custom exceptions
│   │   │       └── InvalidMoveException.java
│   │   └── resources/
│   │       └── images/               # Game assets
│   │           ├── enemies/
│   │           ├── players/
│   │           ├── potions/
│   │           ├── weapons/
│   │           └── ui/
│   └── test/
│       └── java/                     # Unit tests (to be implemented)
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## 🛠️ Technologies Used

- **Language**: Java 17
- **Build Tool**: Apache Maven 3.10.1
- **GUI Framework**: Java Swing
- **Architecture**: MVC (Model-View-Controller)
- **Version Control**: Git

## 🎓 Learning Objectives Demonstrated

This project successfully demonstrates:

✅ **Encapsulation**: All fields are private with controlled access  
✅ **Abstraction**: Abstract classes and interfaces define contracts  
✅ **Inheritance**: Multi-level inheritance hierarchies  
✅ **Polymorphism**: Method overriding and interface implementation  
✅ **Composition**: Objects contain other objects  
✅ **Generic Programming**: `Effect<T>`, `Level<T>`, `IEffectable<T>`  
✅ **Exception Handling**: Custom exceptions for invalid game states  
✅ **Factory Pattern**: Centralized object creation  
✅ **Separation of Concerns**: Clear MVC architecture  
✅ **SOLID Principles**: Single Responsibility, Dependency Inversion  

## 📝 Future Enhancements

- [ ] Add comprehensive unit tests using JUnit 5
- [ ] Implement save/load game functionality
- [ ] Add sound effects and background music
- [ ] Create more enemy types and boss battles
- [ ] Add multiplayer co-op mode
- [ ] Implement achievement system
- [ ] Add difficulty settings

## 👤 Author

Created as an educational project to demonstrate Object-Oriented Programming principles in Java.

## 📄 License

This project is created for educational purposes.

---

**Note**: This is an academic project designed to showcase OOP concepts. For questions or suggestions, please open an issue.


