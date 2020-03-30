package com.upec.androidtemplate20192020.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**Extended Thread class for app to be run.
 * It sets to draw game's logic and update
 * the objects on the canvas.It updates and draws
 * every frame. For default is set to 60 FPS.
 *@author Ceban Cristian
 * @author cristiceban4444@gmail.com
 * @version 1.3
 * @since 1.0
 */
public class PongThread extends Thread {

    /**Max FPS locked on 60.*/
    private static final int MAX_FPS = 60;

    /**Average FPS of app running on device.*/
    private double averageFPS;

    /**Surface holder where Canvas is drawn.*/
    private final SurfaceHolder surfaceHolder;

    /**All game logic is here*/
    private PongView pongView;

    /**Boolean to test if app is running or is closed.*/
    private boolean running;

    /**Canvas to draw*/
    private static Canvas canvas;

    /**Constructor.
     *
     * @param surfaceHolder Surface holder where Canvas is drawn.
     * @param pongView Game logic.
     */
    PongThread(SurfaceHolder surfaceHolder, PongView pongView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.pongView = pongView;
    }

    /**Set thread state to running
     *
     * @param running state to put.
     */
    void setRunning(boolean running){
        this.running = running;
    }

    /**Override the method "run" from THREAD.*/
    @Override
    public void run () {
        /*Start time of thread in ns*/
        long startTime;

        /*Time in ms.*/
        long timeMills = 1000/MAX_FPS;

        /* Wait time to not overlay MAX_FPS*/
        long waitTime;

        /*Number of frame to not overlay MAX_FPS*/
        int frameCount = 0;

        /*Total time from*/
        long totalTime = 0;

        /*Target time to not overlap MAX_FPS*/
        long targetTime = 1000/MAX_FPS;

        /*Until Thread is running do all game logic*/
        while(running){

            /*Start time = System time (in ns),this is done for
             * sinchronized the FPS to be 60 or less.
             */
            startTime = System.nanoTime();

            /*Set canvas null for*/
            canvas = null;

            /*Trying to lock canvas and make the draw
             * and update functions to be displayed on surfaceHolder.
             * Can throw Exception on the method
             * this.surfaceHolder.lockCanvas()
             */
            try{
                /*Lock the SurfaceHolder just for one
                 *Canvas to be drawn at the same time*/
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.pongView.update();
                    this.pongView.draw(canvas);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                /*If canvas is != null then trying to unlock canvas
                 *and post it.
                 */
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            /*Set the time in ms current time - start time and convert to ms*/
            timeMills = (System.nanoTime() - startTime)/1000000;

            /*Set wait time for the next fps */
            waitTime = targetTime - timeMills;
            try{
                /*Wait for the necessary time.*/
                if(waitTime>0)
                    sleep(waitTime);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            /*Increment total time.*/
            totalTime += System.nanoTime() - startTime;

            /*Increment frame count until it 60*/
            frameCount++;

            /*If frame count is equal to MAX_FPS then
             *reset all the values specified to current second and
             *print the average FPS.
             */
            if(frameCount == MAX_FPS) {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }

}

