package com.upec.androidtemplate20192020;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.upec.androidtemplate20192020.game.Constants;
import com.upec.androidtemplate20192020.game.PongView;

/**Main activity,the first layout which appears when the app
 * is opened,it has 3 buttons.
 * 1. Play - start the game.
 *    Game is a Clone of the classic Pong 1972 by Atari.
 * 2. Difficulty - another layout where difficulty can be changed
 *    It has 4 difficulties:
 *                        Easy - 5
 *                        Medium - 10
 *                        Hard - 15,
 *                        Hardcore - 30.
 * 3. Exit - exit the game
 *
 * @author Ceban Cristian
 * @author cristiceban4444@gmail.com
 * @version 1.3
 * @since 1.0
 *
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Set the Window in full screen without title.*/
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*Save the Screen Width and Height in universal Display Metrics.
         * This is done to render when the game began.
         */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        /*Set the content view with xml file.*/
        setContentView(R.layout.activity_main);

        /*Find buttons by id from xml file.
        * Bt1 - Play,
        * Bt2 - Difficulty,
        * Bt3 - Exit.
        */
        Button bt1 = findViewById(R.id.button1);
        Button bt2 = findViewById(R.id.button2);
        Button bt3 = findViewById(R.id.button3);

        /*Set the listener for the first button - Play.*/
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*If it's clicked,then launch the game*/
                player1();
            }
        });

        /*Set the listener for the second button - Difficulty.*/
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*If it's clicked,then choose the difficulty.
                 * This is done by changing the bot's rectangle speed.
                 * This is saved in Constants.RECTANGLE_DEFAULT_SPEED.
                 */
                chooseDifficulty();
            }
        });

        /*Set the listener to the third button - Exit.*/
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*If it's clicked,then exit from the app.*/
                finish();
                System.exit(0);
            }
        });
    }

    /*Launch the game and set the current content view to PongView.*/
    public void player1(){
        setContentView(new PongView(this));
    }

    /*Start the new activity where the difficulty can be chosen.*/
    public void chooseDifficulty(){
        startActivity(new Intent(MainActivity.this,ChooseDifficulty.class));
    }
}
