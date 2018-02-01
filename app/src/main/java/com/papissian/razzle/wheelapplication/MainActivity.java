package com.papissian.razzle.wheelapplication;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView wheel;
    private Button button;

    //time (for button click or hold determination)
    private int tapTime = 0;

    //initialize array
    int[] position = new int[2];

    //degree
    int degree = 0;

    //wheel and button measurment
    private int wheelDiameter, wheelRadius, buttonDiameter, buttonRadius;
    private double diffX, diffY;

    //screen measurements
    private int screenWidth, screenHeight, screenCenterX, screenCenterY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wheel = (ImageView) findViewById(R.id.wheel);
        button = (Button) findViewById(R.id.button);
        button.setText(degree + "");
        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                tapTime++;

                if (action == MotionEvent.ACTION_UP) {
                    if (tapTime < 10) {
                        //Click action, put button in random rotation point
                        buttonClicked();
                    }
                    tapTime = 0;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    if (tapTime >= 10) {
                        changePos((double) motionevent.getRawX(), (double) motionevent.getRawY());
                    }
                }
                return false;
            } //end onTouch
        });

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setInitialPosition();
        }
    }

    public void setInitialPosition() {

        // Get screen size.
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        //Set values
        screenWidth = size.x;
        screenHeight = size.y;
        screenCenterX = screenWidth/2;
        screenCenterY = screenHeight/2;

        //Wheel & Button Dimentions
        wheelDiameter = wheel.getHeight();
        wheelRadius = wheelDiameter/2;
        buttonDiameter = button.getHeight();
        buttonRadius = buttonDiameter/2;

        //Set Wheel Position Center Screen
        wheel.setY(screenCenterY - wheelRadius);
        wheel.setX(screenCenterX - wheelRadius);

        //Set Button to 0 deg
        degree = 0;
        button.setText(degree + "");
        button.setY(screenCenterY - buttonRadius);
        button.setX(screenCenterX + wheelRadius - buttonRadius);

    }

    public void buttonClicked() {

        //Generate random number from 0-359
        degree = new Random().nextInt(360); //range [0, n-1] where n=360

        // Set button deg value
        button.setText(degree + "");

        position = positionCalculator(degree);

        //Set new button location
        button.setX(position[0]);
        button.setY(position[1]);


    }

    public void changePos(double x, double y) {

        //Difference from center of wheel to finger position
        diffX = x - screenCenterX;
        diffY = screenCenterY - y;

        //calculate degree
        degree = (int) findDeg(diffX, diffY);

        //calculate position
        position = positionCalculator(degree);

        //Set new button location
        button.setX(position[0]);
        button.setY(position[1]);

    }

    public double findDeg(double x, double y) {
        double deg;

        deg = Math.toDegrees(Math.atan2(y,x));

        if (deg < 0) {
            deg = 360 + deg;
        }

        button.setText((int) deg + "");

        return deg;
    }

    public int[] positionCalculator(double deg) {
        double angleInRadian = Math.toRadians(deg);
        double x = wheelRadius * Math.cos(angleInRadian);
        double y = wheelRadius * Math.sin(angleInRadian);

        position[0] = screenCenterX - buttonRadius + (int) x;
        position[1] = screenCenterY - buttonRadius - (int) y;


        return position;
    }

}
