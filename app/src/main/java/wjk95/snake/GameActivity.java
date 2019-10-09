/* GameActivity.java
 * Created by Wojciech Krajewski
 * 2018
 */

package wjk95.snake;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;


public class GameActivity extends Activity {

    GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Getting the pixel dimensions of the screen to define its resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //Passing the reference of this activity and screen resolution to gameEngine constructor
        gameEngine = new GameEngine(this, size);
        setContentView(gameEngine);
        }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        gameEngine.pause();
    }
}