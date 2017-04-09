
package bao;

import javafx.scene.control.Button;

/** 
* A helper class that encapsulates interface details on a playable hole on the board interface.
*/

public class Hole extends Button {

    /** The Hole's lateral position on the interface. */

    public int x;

    /** The Hole's vertical position on the interface. */

    public int y;
    public Hole(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }
}
