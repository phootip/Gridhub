# Gridhub

_Gridhub_ is the puzzle game which the player will be able to control the ball and moving around to the finish area. It is designed to be a community-contributed game.  The player can play around with the stage which has already been designed, or can create new stage for other people to play as well.

This is a 10-day project for 2110215 PROG METH course.

## Getting Started

To start the game, you computer need to have install Java 8 or higher installed already. You can build the game yourself using Eclipse. Alternatively, you can just run the `.jar` file that we have built in `export` folder. Note that the game require the folder `levels` attached with the `.jar` file, so if you want to copy the game to other place, do not forget to copy the `levels` along with it.

## How to Play

This game is keyboard-oriented. To navigate throughout the game, use the arrow keys, escape key, and enter key.

| Command | Player 1 | Player 2 |
| ------- | -------- | -------- |
| Move left | `A` | `L` |
| Move right | `D` | `'` |
| Move up | `W` | `P` |
| Move down | `S` | `;` |
| Camera turning | `Q` and `E` | `O` and `[` |

## The Rule

The rule of game is very simple. There is only one way to clear the stage: *just reach the flag* (or finish area). In co-op modes, to be considered winning, both players must be on any flags (or finish area) at the same time.

## Game Objects

There are many objects available in this game.

### Box

Can be pushed from the same floor level and can be walked on if you are above the box.

### Slope

If you want to go to a higher floor, you will need this.
 
### Teleporter

It will take you somewhere in the map. Where does it take you to? You have to try it!!
 
### Teleport Destination

It is marked as a mini circle on the map. It is the destination of the teleporter.

### Switch

If you want to activate the gate, you have to stand over this thing. You can also just put the _block_ over it.

### Gate

The red *X* mark show that the gate *has not been activated* yet, and will not let anything going through it! You have to turn the switch on first to activate the gate.

## Level Editor

In this game, you can also create and edit the stage. Cool, right? Let's try it out and create your own level.

## Forking Our Project

Feel free to fork our project and send us pull request. However, these are the things that should be minded:

- We wrote the code so that the game is functional enough (playable) before the deadline, To do so, our code is *not* clean.
- There is a _collection_ of bugs. Some might be known to us (but too lazy to fix them), some might not. Anyway, feel free to create a new issue or send us pull request. We appreciate your contributions!
- Expect weird things in our code. :D
