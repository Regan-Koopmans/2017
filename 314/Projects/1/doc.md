# Bao Project Documentation
### Regan Koopmans, 15043143

## How To Compile and Run the Project 

To compile and run this project, enter the following commands in the root of the project. In addition, the `ant` build system and the JavaFX UI framework will need to be installed as dependencies.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~bash
ant compile
ant run
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


If everything works correctly, you should be greeted with a window that looks as
follows:
 

 

How to Interact with the Graphical Interface
--------------------------------------------

 

You play the holes that are “closest” to you, meaning you play the bottom 2 rows against the top 2 rows. The yellow tiles represent the house holes, but the functionality of the house was not implemented.

 

To play a certain hold you need only to *click* on that hole, after which a dialog will appear up asking to enter a direction to sow or cascade you seeds in. In some cases, you will not be prompted for a direction, but these are cases in which direction is not needed such as *kimbi* and *k* plays.

Summary of Work Done
--------------------


For this project I successfully completed the following.

 

-   The program uses plain mini-max and expands to 2 ply by default.

-   



Currently my program uses the following linear fitness function:

 

 

$$
f(x) =
$$

 


Implementation Overview
-----------------------

 

Some key points from an implementation standpoint.

-   *The game is made independent of the artificial intelligence*. The programming concerning the Bao game only interacts with abstract players, and knows nothing of their implementation and/or methods of deriving their play decisions.

-   *The game runs independently of the interface*. The main application that the user interacts with only has a pointer to the running game, and in fact runs in a different thread to the running Bao game. This means that the interface can dynamically attach and detach from a running Bao game, which is done when you start a new game and the previous session is terminated.



For a more comprehensive breakdown of the programming of this project, read *implementation.pdf* in the project directory. This document was generated from source using **Javadoc**, and explains in detail the purpose of each class, method and source-file.
