package com.upec.androidtemplate20192020.game;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**A class which is used to store all game logic and interact with
 * threads (PongThread).
 * The Android SurfaceView provides a dedicated drawing surface with
 * good render and speed of callback.
 * @author Ceban Cristian
 * @author cristiceban4444@gmail.com
 * @version 1.3
 * @since 1.0
 *
 */
public class PongView extends SurfaceView implements SurfaceHolder.Callback {

    /**The context which allows access callback for surfaceView.*/
    Context context;

    /**A thread's class for run the pong Game.*/
    private PongThread pongThread;

    /**Surface holder of the View.*/
    private SurfaceHolder surfaceHolder;

    /**Player's rectangle.*/
    private Rectangle player;

    /**Bot's rectangle.*/
    private Rectangle bot;

    /**Center of the player's rectangle.It is used to update
     * the player's rectangle using the onTouchEvent.
     */
    private Point playerPoint;

    /**Center of the bot's rectangle.It is used to move the bot's
     * rectangle along the screen.
     */
    private Point botPoint;

    /**Ball which is bounced from the paddle and the screen's edges.*/
    private Ball ball;

    /**Both rectangles width.*/
    private int recW;

    /**Both rectangles height.*/
    private int recH;

    /**Player's score.*/
    private static int leftScore;

    /**Bot's score.*/
    private static int rightScore;

    /**Paint to store the colors.*/
    private Paint paint;

    /**Standard constructor which creates the game.
     *
     * @param context Context which will be created.
     */
    public PongView(Context context) {
        super(context);
        this.context = context;
        /*Callback the holder.*/
        getHolder().addCallback(this);

        /*Create the pong Thread.*/
        pongThread = new PongThread(getHolder(), this);

        /*Store the width and height of the rectangle in Constants.*/
        sizeR();

        /*Create the player's rectangle at the bottom of the screen.*/
        player = new Rectangle(new Rect((int) (Constants.SCREEN_WIDTH / 2 - recW / 2), (int) (Constants.SCREEN_HEIGHT / 2 + recH / 2),
                (int) (Constants.SCREEN_WIDTH / 2 + recW / 2), (int) (Constants.SCREEN_HEIGHT / 2 - recH / 2)), Color.rgb(255, 0, 0));

        /*Create the bot's rectangle at the top of the screen.*/
        bot = new Rectangle(new Rect((int) (Constants.SCREEN_WIDTH / 2 - recW / 2), (int) (Constants.SCREEN_HEIGHT / 2 + recH / 2),
                (int) (Constants.SCREEN_WIDTH / 2 + recW / 2), (int) (Constants.SCREEN_HEIGHT / 2 - recH / 2)), Color.rgb(0, 0, 100));

        /*Assign the player's point with the center of the player's rectangle.*/
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2 , Constants.SCREEN_HEIGHT - Constants.SCREEN_HEIGHT / 5);

        /*Assign the bot's point with the center of the bot's rectangle.*/
        botPoint = new Point(Constants.SCREEN_WIDTH / 2, 100);

        /*The ball which will be bounced from the edges and the paddle.*/
        ball = new Ball(225, 225, 35, Color.rgb(254, 50, 123));

        /*The player's score.*/
        leftScore = 0;

        /*The bot's score.*/
        rightScore = 0;

        /*Paint to store colors.*/
        paint = new Paint();

        /*Set the color of the paint to red.*/
        paint.setColor(Color.RED);

        /*Focus the Context.*/
        setFocusable(true);
    }

    /**Function which is called when the surface was created.
     *
     * @param surfaceHolder The surface on which canvas
     *                      will be drawn from pongThread.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        /*Create the thread with current holder and run his.*/
        pongThread = new PongThread(getHolder(), this);
        pongThread.setRunning(true);
        pongThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    /**The function which stops the thread if the app was closed.
     *
     * @param surfaceHolder Surface to be destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        /*Stop the thread.*/
        boolean retry = true;
        while (true) {
            try {
                pongThread.setRunning(false);
                pongThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    /**
     *
     * @param event absconding coordinates
     *              from the touched place.
     * @return True if the screen was touched.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                playerPoint.set((int) event.getX(), (int) event.getY());
        }
        return true;
    }

    /**Draw function which is called every frame by the thread.
     * It draws all the objects and sets the score on the screen.
     *
     * @param canvas The canvas on which will be drawn.
     */
    @Override
    public void draw(Canvas canvas) {
        /*Reset game if any of the score reached 5 points.*/
        if (leftScore == 5 || rightScore == 5)
            resetGame();

        /*Draw on the white canvas.*/
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);

        /*Draw the player's rectangle.*/
        player.draw(canvas);

        /*Draw the bot's rectangle.*/
        bot.draw(canvas);

        /*Draw the ball.*/
        ball.draw(canvas);

        /*Set the score with the black color on the center of the screen.*/
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(leftScore), (float) (Constants.SCREEN_WIDTH * 0.33), (float) (Constants.SCREEN_HEIGHT * 0.5), paint);
        canvas.drawText(String.valueOf(rightScore), (float) (Constants.SCREEN_WIDTH * 0.66), (float) (Constants.SCREEN_HEIGHT * 0.5), paint);
    }

    /**Update function which is called every frame by the thread.
     * It draws all the objects and sets the score on the screen.
     */
    public void update() {
        /*Update the player's rectangle position.*/
        player.update(playerPoint, true,ball);

        /*Update the bot's position.*/
        bot.update(botPoint,false,ball);

        /*Update the ball's position.*/
        ball.update(player.getRect(),bot.getRect());
    }

    /**Save the rectangle width and height based
     * on the screen width and height.
     */
    public void sizeR() {
        recW = (int) (Constants.SCREEN_WIDTH * 0.33);
        recH = (int) (Constants.SCREEN_HEIGHT * 0.025);
    }

    /**Increment the player's score.*/
    public static void incLeftScore() {
        leftScore++;
    }

    /**Increment the bot's score.*/
    public static void incRightScore() {
        rightScore++;
    }

    /**Reset the score to 0 for both paddles.*/
    public static void resetScore() {
        leftScore = 0;
        rightScore = 0;
    }

    /**Reset the game.*/
    public void resetGame() {
        resetScore();
    }
}