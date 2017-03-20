/*

    CLASS       : Hole
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Encapsulates a button that represents a Bao hole in the 
                  program.

 */
package bao;

import javafx.scene.control.Button;

public class Hole extends Button {
    public int x;
    public int y;
    public Hole(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }
}
