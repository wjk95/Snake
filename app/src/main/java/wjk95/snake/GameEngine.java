/* GameEngine.java
 * Created by Wojciech Krajewski
 * 2018
 */

package wjk95.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.Random;


public class GameEngine extends SurfaceView implements Runnable {

    private Thread thread = null;

    // Canvas is used to draw the game on it
    private Canvas canvas;
    private Paint paint;

    // SurfaceHolder is needed to hold the canvas
    private SurfaceHolder surfaceHolder;


    // Control pausing between updates
    private long nextFrameTime;

    // Used to update the game 10 times per second
    private final long FPS = 10;
    private final long MILLIS_PER_SECOND = 1000;


    private boolean eaten = true;
    private int score;
    private Point apple = new Point();
    // List used to contain all snake blocks
    private LinkedList<Point> snake = new LinkedList<>();

    // enum used to track the movement
    public enum Heading {UP, RIGHT, DOWN, LEFT}
    private Heading heading = Heading.RIGHT;

    private int screenX;
    private int screenY;

    // Screen divided into 20 blocks horizontally
    private final int blocksWide = 20;

    private int blocksHigh;
    private int blockSize;

    private int snakeColor = ContextCompat.getColor(getContext(),R.color.colorSnake);
    private int appleColor = ContextCompat.getColor(getContext(),R.color.colorApple);

    private volatile boolean isPlaying;

    public GameEngine(Context context, Point size) {
        super(context);

        screenX = size.x;
        screenY = size.y;

        blockSize = (screenX / blocksWide);
        blocksHigh = (screenY / blockSize);


        surfaceHolder = getHolder();

        paint = new Paint();

        newGame();

    }


    public void spawnApple() {

        Random random = new Random();
        do {
            apple.x = random.nextInt(blocksWide - 1) + 1;
            apple.y = random.nextInt(blocksHigh - 1) + 1;
        } while (snake.contains(new Point(apple.x, apple.y)));
        eaten = false;
    }


    public void moveSnake() {

        for (int i = snake.size()-1; i > 0; i--) {
            snake.get(i).x = snake.get(i-1).x;
            snake.get(i).y = snake.get(i-1).y;


            }

            switch (heading) {
                case UP:
                    snake.getFirst().y--;
                    break;
                case DOWN:
                    snake.getFirst().y++;
                    break;
                case LEFT:
                    snake.getFirst().x--;
                    break;
                case RIGHT:
                    snake.getFirst().x++;
                    break;
            }


    }


    public void draw() {

        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.argb(255,47,79,79));

            paint.setColor(Color.argb(255,255,255,255));
            paint.setTextSize(70);
            canvas.drawText("Score: " + score, 10, 70, paint);

            paint.setColor(snakeColor);

            for(int i = 0; i < snake.size(); i++){
                canvas.drawRect(snake.get(i).x * blockSize, snake.get(i).y * blockSize, (snake.get(i).x * blockSize) + blockSize, (snake.get(i).y * blockSize) + blockSize, paint);
            }

            paint.setColor(appleColor);
            canvas.drawRect(apple.x * blockSize, apple.y  * blockSize, (apple.x * blockSize) + blockSize, (apple.y * blockSize) + blockSize, paint);



            surfaceHolder.unlockCanvasAndPost(canvas);

        }

    }

    public void newGame() {

        // Drawing one snake block in the middle of the screen
        snake.addFirst(new Point(blocksWide / 2, blocksHigh / 2));



        spawnApple();

        score = 0;
        // Triggering an update
        nextFrameTime = System.currentTimeMillis();

    }

    private void eatApple() {
        snake.addLast(new Point(snake.getLast()));
        spawnApple();
        score += 1;
    }


    private boolean detectDeath(){
        boolean dead = false;


        if (snake.getFirst().x == -1) dead = true;
        if (snake.getFirst().x >= blocksWide) dead = true;
        if (snake.getFirst().y == -1) dead = true;
        if (snake.getFirst().y == blocksHigh+1) dead = true;

        for (int i = snake.size()-1; i > 0; i--) {
            if ((i > 3) && (snake.getFirst().x == snake.get(i).x) && (snake.getFirst().y == snake.get(i).y)) {
                dead = true;
            }
        }

        return dead;
    }

    @Override
    public void run() {
        while(isPlaying) {
            if(updateRequired()){
                update();
                draw();
            }
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e){}

    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }


    public void update() {
        // Compare snake's first block's coordinates with apple's, proceed to eat it if they match
        if (snake.getFirst().x == apple.x && snake.getFirst().y == apple.y) {
           eatApple();
        }

        moveSnake();

        if (detectDeath()) {
            snake.clear();
            newGame();
        }

    }



    public boolean updateRequired() {
        if(nextFrameTime <= System.currentTimeMillis()){

            nextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;

            return true;
        }

        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {

        // Snake movement - heading is currently set to change clockwise on click, similar to classic Snake game.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                    switch(heading){
                        case UP:
                            heading = Heading.RIGHT;
                            break;
                        case RIGHT:
                            heading = Heading.DOWN;
                            break;
                        case DOWN:
                            heading = Heading.LEFT;
                            break;
                        case LEFT:
                            heading = Heading.UP;
                            break;
                    }


        }
        return true;
    }


}
