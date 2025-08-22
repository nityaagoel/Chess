# ♔ Chess Game in Java ♕

A **complete chess game implementation** in Java featuring a sophisticated GUI, advanced AI opponent, and comprehensive Object-Oriented Programming design. Perfect for learning OOP concepts, game development, and AI algorithms!

## ✨ Features

🎯 **Complete Chess Implementation**
- Full chess rules with all piece movements
- Check, checkmate, and stalemate detection
- Turn-based gameplay validation
- Move history tracking

🤖 **Intelligent AI Opponent**
- Minimax algorithm with Alpha-Beta pruning
- Configurable difficulty levels (search depth)
- Strategic position evaluation
- Material and positional analysis

🎨 **Interactive GUI**
- Intuitive click-to-move interface
- Visual move highlighting and validation
- Customizable piece representations
- Responsive design with smooth interactions

🏗️ **Advanced OOP Design**
- Clean architecture with separation of concerns
- Polymorphic piece behavior
- Robust exception handling
- Extensible design patterns

## 🗂️ Project Structure

```
chess-game/
├── 📁 src/
│   ├── 📁 main/
│   │   └── ChessGame.java          # 🚀 Main application entry point
│   ├── 📁 game/
│   │   ├── Board.java              # ♟️ Chess board logic and game state
│   │   ├── Player.java             # 👤 Player enumeration (WHITE/BLACK)
│   │   └── Move.java               # 🎯 Move representation and validation
│   ├── 📁 pieces/
│   │   ├── Piece.java              # 🔄 Abstract base class for all pieces
│   │   ├── Pawn.java               # ♟️ Pawn movement and special rules
│   │   ├── Rook.java               # ♜ Rook straight-line movement
│   │   ├── Knight.java             # ♞ Knight L-shaped movement
│   │   ├── Bishop.java             # ♝ Bishop diagonal movement
│   │   ├── Queen.java              # ♛ Queen combined movement
│   │   └── King.java               # ♚ King movement and special rules
│   ├── 📁 exceptions/
│   │   ├── InvalidMoveException.java # ⚠️ Invalid move handling
│   │   └── GameException.java       # 🚫 General game exceptions
│   ├── 📁 ai/
│   │   └── ChessAI.java            # 🧠 Minimax AI implementation
│   ├── 📁 gui/
│   │   ├── ChessGUI.java           # 🖼️ Main game window
│   │   └── ChessSquare.java        # ⬜ Individual chess square component
│   └── 📁 resources/ (optional)
│       └── 📁 images/              # 🎨 Custom piece images
├── 📄 README.md                    # 📖 This file
└── 📄 LICENSE                      # ⚖️ MIT License
```
### Prerequisites
- Java 8 or higher installed on your system
- Basic understanding of chess rules
- Terminal/Command Prompt access

### Installation & Running

#### 📥 Option 1: Download and Compile

1. **Clone or download the project**
   ```bash
   git clone https://github.com/yourusername/chess-game-java.git
   cd chess-game-java
   ```

2. **Create the directory structure**
   ```bash
   mkdir -p src/{main,game,pieces,exceptions,ai,gui}
   ```

3. **Add all the Java files** to their respective packages

4. **Compile the project**
   ```bash
   javac -d bin src/**/*.java
   ```

5. **Run the game**
   ```bash
   java -cp bin main.ChessGame
   ```

#### 🖥️ Option 2: Using IDE (IntelliJ IDEA)

1. Create a new Java project
2. Set up the package structure as shown above
3. Copy all source files into their respective packages
4. Run `main.ChessGame` as the main class
5. Enjoy playing! ♟️

## 🎮 How to Play

### Game Modes
- **👥 Human vs Human**: Two players take turns on the same computer
- **🤖 Human vs AI**: Challenge the computer opponent

### Controls & Interface

| Action | Method | Description |
|--------|--------|-------------|
| **Select Piece** | Left Click | Click on any piece you want to move |
| **View Valid Moves** | After Selection | Valid moves highlighted in green |
| **Make Move** | Left Click | Click on highlighted destination square |
| **Deselect** | Left Click Same Piece | Cancel current selection |
| **New Game** | Button Click | Reset board to starting position |
| **Toggle AI** | Button Click | Switch between Human vs AI / Human vs Human |

### Visual Indicators

| Color | Meaning |
|-------|---------|
| 🟡 **Yellow** | Currently selected piece |
| 🟢 **Green** | Valid move destinations |
| ⬜ **Light Brown** | Light squares (default) |
| ⬛ **Dark Brown** | Dark squares (default) |



