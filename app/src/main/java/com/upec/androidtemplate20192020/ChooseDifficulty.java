package com.upec.androidtemplate20192020;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.upec.androidtemplate20192020.game.Constants;
import java.util.ArrayList;

/**Class which handles the layout ChooseDifficulty.
 * it has the back button and spinner with
 * selection of the difficulty.
 * @author Ceban Cristian
 * @author cristiceban4444@gmail.com
 * @version 1.3
 * @since 1.1
 *
 */
public class ChooseDifficulty extends AppCompatActivity {

    /**Override the method onCreate from AppCompatActivity,
     * it sets the button to go back and spinner with
     * selection of the difficulty.
     *
     * @param savedInstanceState a bundle on which
     *                           the instance is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Set the Window in full screen without title.*/
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_difficulty);

        /*Find a spinner by id.*/
        Spinner spinner = findViewById(R.id.spinner_difficulty);

        /*Create an array list with 4 elements,it will be the hint of
         *the spinner. Spinner will have the 4 option.
         *      1.Easy
         *      2.Medium
         *      3.Hard
         *      4.Nightmare
         */
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Easy");
        arrayList.add("Medium");
        arrayList.add("Hard");
        arrayList.add("Nightmare!");

        /*Create an ArrayAdapter and put the difficulty option of the game.*/
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);

        /*Set to be dropped down by clicking on them and load the the layout.*/
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        /*Create the spinner and put adapter in him.
         *Here you select the speed of the bot's rectangle*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String difficulty = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + difficulty,Toast.LENGTH_LONG).show();
                if (position == 0)
                    Constants.RECTANGLE_DEFAULT_SPEED = 5;
                else if (position == 1)
                    Constants.RECTANGLE_DEFAULT_SPEED = 10;
                else if (position == 2)
                    Constants.RECTANGLE_DEFAULT_SPEED = 15;
                else if (position == 3 )
                    Constants.RECTANGLE_DEFAULT_SPEED = 30;

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        /*Create the back button to the main app
         * and link it with the function to go back.*/
        Button bt1 = findViewById(R.id.buttonBack);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    /*Function to go back.*/
    private void goBack(){
        startActivity(new Intent(ChooseDifficulty.this,MainActivity.class));
    }

}
