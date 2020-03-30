package com.upec.androidtemplate20192020.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.math.MathUtils;
import java.util.concurrent.ThreadLocalRandom;
import static com.upec.androidtemplate20192020.game.Constants.BALL_DEFAULT_SPEED;

/**Class which simulates the ball.
 * It has the update and draw functions
 * for rendering on the android.graphics.Canvas.
 * Ball is described by his position on the canvas
 * X and y, radius ,its acceleration on x-axis and y-axis,
 * by his color and bounce angle when it hits a paddle.
 * The more the ball bounces one of the paddles the higher the speed gets
 * @author Ceban Cristian
 * @author cristiceban4444@gmail.com
 * @version 1.3
 * @since 1.0
 */
class Ball {

    /**Max. bounce's angle when the ball hits one of the paddle.*/
    private static final int MAX_BOUNCE_ANGLE = 60;

    /**Ball's x and y coordinates.*/
    private float x,y;

    /**Ball's radius.*/
    private float r;

    /**Number of bounce from both balls.*/
    private int countBounce;

    /**Color of the ball.*/
    private int color;

    /**speed of the ball on x-axis.*/
    private float xSpeed;

    /**speed of the ball on y-axis.*/
    private float ySpeed;

    /**Relative intersection of the ball towards the paddle.
     * For example if the paddle has 100px, and ball hits the paddle, then one of the following cases occurs
     *      1.If it's hit in the center then the relative intersection is 0 px.
     *      2.If it's hit in the left part of the paddle then it will have [-1..-50]px.
     *      3.If it's hit in the right part of the paddle then it will have [1..50]px.
     */
    private float relativeIntersectY;

    /**Normalized relative intersection of the ball towards the paddle,
     * This is done for the natural reflection of the ball towards the paddle.
     * for example if the paddle has 100 px and the ball hits the paddle then one of the following cases occurs
     *      1.If it's hit in the center then relative intersection is 0.
     *      2.If it's hit in the left part of the paddle when it will have [-0..-1].
     *      3.If it's hit in the right part of the paddle whet it will have [0..1].
     */
    private float normalizedRelativeIntersectionY;

    /**The angle of the ball after it bounces from one of the paddles.*/
    private float bounceAngle ;

    /**Standard constructor of the ball.
     *
     * @param x the x coordinate on the canvas.
     * @param y the y coordinate on the canvas.
     * @param r the radius of the ball.
     * @param color the color of the ball.
     */
    Ball(float x, float y, float r, int color) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.color = color;
        /*Set the default speed of the ball on x-axis and y-axis.*/
        xSpeed = BALL_DEFAULT_SPEED;
        ySpeed = BALL_DEFAULT_SPEED;
        /*Set the ball on the center of screen.*/
        reset();
    }

    /**Draw the ball on the canvas.
     *
     * @param canvas canvas on which will be drawn the ball.
     */
    void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(x,y,r,paint);
    }

    /**Simulates the moving of the ball on the canvas
     *
     * @param player player's rectangle.
     * @param bot bot's rectangle.
     */
    void update(Rect player, Rect bot){
        /*Check if the ball bounces from the edges.*/
        edges();
        /*Check if the ball bounces from the player's rectangle.*/
        checkPaddleUp(player);
        /*Check if the ball bounces from the bot's rectangle.*/
        checkPaddleDown(bot);
        /*Move the ball on the canvas by updating its coordinates
         * by the speed on x-axis and y-axis.*/
        x += xSpeed;
        y += ySpeed;
    }

    /**Check if the ball bounces from the edges.*/
    private void edges(){
        /*Check the left side or the right side.*/
        if (x < 0 + r || x + r > Constants.SCREEN_WIDTH)
            xSpeed*=-1;

        /*If the ball reaches the top of the screen then reset ball
         * and increment the player's ball.*/
        if (y < 0) {
            reset();
            PongView.incLeftScore();
        }

        /*If the ball reaches the bottom of the screen then reset ball
         * and increment the player's ball.*/
        if (y > Constants.SCREEN_HEIGHT) {
            reset();
            PongView.incRightScore();
        }

        /*If the ball goes outside of the canvas size then reset ball.*/
        if(x < 0 || x > Constants.SCREEN_WIDTH)
            reset();
    }

    /**Reset the ball. Put in the center of screen and generate
     * the angle of the ball.
     */
    private void reset(){
        /*Put the ball in the center of the screen.*/
        x = (float) (Constants.SCREEN_WIDTH*0.5);
        y = (float) (Constants.SCREEN_HEIGHT*0.5);

        /*Generate the random angle of the ball and make the velocity
         *using the cos and sin function.
         */
        float angle = (float) ThreadLocalRandom.current().nextDouble((-Math.PI / 4), (Math.PI / 4));
        ySpeed = (float) (BALL_DEFAULT_SPEED * Math.cos(angle));
        xSpeed = (float) (BALL_DEFAULT_SPEED * Math.sin(angle));

        /*Generate random number for the ball to be pushed
         *either on top or bottom of the screen.*/
        if(ThreadLocalRandom.current().nextDouble(0,1)<0.5)
            ySpeed *=-1;
        /*Reset the number of ball's bounce.*/
        countBounce = 0;
    }

    /**Function to check if ball was bounced by the player's rectangle.
     * It will be checked on the top of rectangle and on the sides.
     * If the ball is bounced,then apart from changing the sign of velocity on
     * y-axis,it also generates the angle of the reflection by the ball
     * surface on the paddle's.
     *
     * @param rect the player's rectangle
     */
    private void checkPaddleUp(Rect rect) {
        /*Check if ball is bounced by paddle top*/
        if (
                ySpeed > 0 &&
                x - r < rect.centerX() + rect.width()*0.5 &&
                x + r > rect.centerX() - rect.width()*0.5 &&
                y - r < rect.centerY() + rect.height()*0.5
        ) {
            /*If it's higher (y-axis) than the paddle.*/
            if (y + r > rect.centerY()) {
                /*Increment the count of bounce.This is done for accelerating the speed
                 *with every bounce.*/
                countBounce++;

                /*Calculate the relative intersect of the ball with paddle.
                 * For example if the paddle has 100px, and the ball hits one of the following:
                 *      1.If it's hit in the center then relative intersection is 0 px.
                 *      2.If it's hit in the left part of the paddle then it will have [-1..-50]px.
                 *      3.If it's hit in the right part of the paddle then it will have [1..50]px.
                 */
                relativeIntersectY = (x - rect.centerX());

                /*Normalize the relative value of relative intersection of the ball with paddle.
                 *For example if paddle has 100 px, and the ball hits one of the following:
                 *      1.If it's hit in the center then relative intersection is 0.
                 *      2.If it's hit in the left part of the paddle then it will have [-0..-1].
                 *      3.If it's hit in the right part of the paddle then it will have [0..1].
                 */
                normalizedRelativeIntersectionY = (float) (relativeIntersectY/(rect.width()*0.5));

                /*Clamp this value between [-1..1]. */
                normalizedRelativeIntersectionY = MathUtils.clamp(normalizedRelativeIntersectionY,-1,1);

                /*Calculate the angle of the ball with which it will be bounced.
                 *It can take the value between [-60..60] degrees.*/
                bounceAngle = (normalizedRelativeIntersectionY) * MAX_BOUNCE_ANGLE;

                /*Calculate the new speed on x-axis and y-axis using in dependence of count of the ball on paddle
                 *and of the angle.For this I used transformation to radians, and sin and cos for the vector of speed.
                 * sign of y speed also it's negated.*/
                xSpeed = (float) ((BALL_DEFAULT_SPEED + countBounce) * Math.sin(Math.toRadians(bounceAngle)));
                ySpeed = (float) ((BALL_DEFAULT_SPEED + countBounce)* -(Math.cos(Math.toRadians(bounceAngle))));
            }
        }
    }

    /**Function to check if the ball was bounced by the bot's rectangle.
     * It will check on the bottom of rectangle and on the sides.
     * If the ball was bounced,then apart from changing the velocity sign on
     * y-axis,it also generates the angle of the reflection by the ball towards the
     * surface on the paddle.
     *
     * @param rect the bot's rectangle
     */
    private void checkPaddleDown(Rect rect){
        /*Check if the ball is bounced by the bottom of the paddle*/
        if (
                ySpeed < 0 &&
                x - r < rect.centerX() + rect.width()*0.5 &&
                x + r > rect.centerX() - rect.width()*0.5 &&
                y + r > rect.centerY() + rect.height()*0.5
        ) {
            /*If it's lower (y-axis) than the paddle.*/
            if (y - r < rect.centerY()) {
                /*Increment the count of bounce.This is done to accelerate speed
                 *with every bounce.*/
                countBounce++;

                /*Calculate the relative intersect of the ball with paddle.
                 * For example if the paddle has 100px, and the ball hits one of the following:
                 *      1.If it's hit in the center then relative intersection is 0 px.
                 *      2.If it's hit in the left part of the paddle then it will have [-1..-50]px.
                 *      3.If it's hit in the right part of the paddle then it will have [1..50]px.
                 */
                relativeIntersectY = (x - rect.centerX());

                /*Normalize the relative value of the relative intersect of the ball with paddle.
                 *For example if paddle has 100 px, and the ball hits one of the following:
                 *      1.If it's hit in the center then relative intersection is 0.
                 *      2.If it's hit in the left part of the paddle then it will have [-0..-1].
                 *      3.If it's hit in the right part of the paddle then it will have [0..1].
                 */
                normalizedRelativeIntersectionY = (float) (relativeIntersectY/(rect.width()*0.5));

                /*Clamp this value between [-1..1]. */
                normalizedRelativeIntersectionY = MathUtils.clamp(normalizedRelativeIntersectionY,-1,1);

                /*Calculate the angle of the ball with which it will be bounced.
                 *It can take the value between [-60..60] degrees.*/
                bounceAngle = (normalizedRelativeIntersectionY) * MAX_BOUNCE_ANGLE;

                /*Calculate the new speed on x-axis and y-axis using the number of the bounce's counts
                 *and the value of the angle. For this I used transformation to radians,and sin and cos for the vector of speed.
                 */
                xSpeed = (float) ((BALL_DEFAULT_SPEED +countBounce) * Math.sin(Math.toRadians(bounceAngle)));
                ySpeed = (float) ((BALL_DEFAULT_SPEED +countBounce) *Math.cos(Math.toRadians(bounceAngle)));
            }
        }
    }

    /**Standard getter of X.*/
    float getX() {
        return x;
    }

    /**Standard getter of Y.*/
    float getY() {
        return y;
    }

    /**Standard getter of speed on y-axis.*/
    float getySpeed() {
        return ySpeed;
    }

}
