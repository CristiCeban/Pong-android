package com.upec.androidtemplate20192020.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import androidx.core.math.MathUtils;
import static com.upec.androidtemplate20192020.game.Constants.RECTANGLE_DEFAULT_SPEED;

/**Class which simulates the rectangle.
 * It has the update and draw functions
 * for rendering on the android.graphics.Canvas.
 * Rectangle is described by his position on the canvas
 * Center with x and y coordinates,color
 * it's acceleration on x-axis for bot.
 * @author Ceban Cristian
 * @author cristiceban4444@gmail.com
 * @version 1.3
 * @since 1.0
 *
 */
class Rectangle {

    /**A rectangle used from android.graphics.Rect.*/
    private final Rect rect;
    private Paint paint;

    /**Standard constructor.*/
    Rectangle(Rect rect, int color) {
        this.rect = rect;
        paint = new Paint();
        paint.setColor(color);
    }

    /**Function to draw a rectangle on the canvas.
     *
     * @param canvas the canvas which will be drawn.
     */
    void draw(Canvas canvas) {
        canvas.drawRect(rect,paint);
    }

    /**Function for update the position of rectangle on the canvas.
     *
     * @param point Center of the rectangle.
     * @param isPlayer a boolean param which says if it's a player.
     * @param ball The ball to test the collision with the rectangles.
     */
    void update(Point point, boolean isPlayer, Ball ball) {
        /*If it's bot then calculate the movement of the bot.*/
       if (!isPlayer)
           rectangleAI(ball);
       /*Otherwise just constrain the player's rectangle in the
        *size of the screen.*/
       else
           constrain(point, true);
    }

    /**Function to constrain the rectangle in the screen canvas's size.
     * It sets the y coordinates of the center to a constant, and the x can
     * flow from 0 + rect.width()/2 to MAX_WIDTH - rect.width()/2.
     *
     * @param point Center of the rectangle.
     * @param isPlayer a boolean param which says if it's a player.
     */
    private void constrain(Point point, boolean isPlayer){
        /*Constrain the paddle to not overcome the screen size.*/
        point.x = MathUtils.clamp( point.x,rect.width()/2,Constants.SCREEN_WIDTH-rect.width()/2);
        /*Constrain the bot on the top of the screen.*/
        if (!isPlayer)
            point.y = 100;
        else
            /*Constrain the player on the bottom of the screen.*/
            point.y = Constants.SCREEN_HEIGHT-100;
        /*Update the rectangle constrained to not overcome the screen's size.*/
        rect.set(point.x - rect.width()/2, point.y - rect.height()/2,
                point.x + rect.width()/2,point.y + rect.height()/2);
    }

    /**Function which simulates the Bot playing.
     *
     * @param ball Rectangle will be moved in dependence of the ball's position.
     */
    private void rectangleAI(Ball ball){
        /*Bot moves the rectangle only if the ball is coming to him.
         *Otherwise,he is just waiting.
         *A strategy to win the bot is to speed up the ball and
         * the bot will be unable to reach the ball in time.
         */
        if(ball.getySpeed()<0) {
            /*If the ball is lefter than the rect then move rect to left.*/
            if (rect.left >= ball.getX())
                rectangleAIRight();
            /*Else, if the ball is righter than then move rect to the right.*/
            else if (rect.right < ball.getX())
                rectangleAILeft();
        }
        /*Constrain the bot to not go over the screen's limits.*/
        constrain(new Point(rect.centerX(),rect.centerY()),false);
    }

    /**Move the rectangle by the AI to right.*/
    private void rectangleAIRight(){
        rect.set(rect.left - RECTANGLE_DEFAULT_SPEED,rect.top,rect.right - RECTANGLE_DEFAULT_SPEED,rect.bottom);
    }

    /**Move the rectangle by the AI to left.*/
    private void rectangleAILeft(){
        rect.set(rect.left + RECTANGLE_DEFAULT_SPEED,rect.top,rect.right + RECTANGLE_DEFAULT_SPEED,rect.bottom);
    }

    /**Standard getter of the rect.*/
    Rect getRect(){
        return this.rect;
    }

}
