package com.alexschwartz.hw6calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Accesses data to/from 2 inputs, operator, equals button, and answer field
        final EditText firstField = findViewById(R.id.input1);
        final Button operator = findViewById(R.id.operator);
        final EditText secondField = findViewById(R.id.input2);
        Button done = findViewById(R.id.equals);
        final TextView answer = findViewById(R.id.answer);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When equals button is pressed do the following

                //Error checking on the inputs (if their length is 0, give error)
                if(firstField.getText().length()==0 || secondField.getText().length()==0) {
                    answer.setText("Missing input(s)");
                } else { //If both inputs (forced to be numbers) exist

                    //Gets the float numbers from inputs
                    float num1 = Float.parseFloat(firstField.getText().toString());
                    float num2 = Float.parseFloat(secondField.getText().toString());

                    //Checking the text of the operator button (not the pull-down menu)
                    if(operator.getText()=="Add") {
                        answer.setText(Float.toString(num1 + num2));
                    } else if(operator.getText()=="Subtract") {
                        answer.setText(Float.toString(num1 - num2));
                    } else if(operator.getText()=="Multiply") {
                        answer.setText(Float.toString(num1 * num2));
                    } else if(operator.getText()=="Divide") {
                        //Error checking on the second input
                        if(num2==0) {
                            answer.setText("Undefined");
                        } else {
                            answer.setText(Float.toString(num1 / num2));
                        }
                    } else {
                        //If the operator button text is "Operator" than user
                        //hasn't selected an operator
                        answer.setText("Missing operator");
                    }
                }
            }
        });
    }

    //When the operator button is clicked it goes here
    //In main activity XML file: android:onClick="operatorMenu"
    //in the operator button's tag
    public void operatorMenu(View view) {
        PopupMenu opMenu = new PopupMenu(this, view);

        //Goes to the onMenuItemClick method below
        opMenu.setOnMenuItemClickListener(this);

        //It takes the layout from app/res/menu/operator_popup.xml
        //and loads it under the operator button
        opMenu.inflate(R.menu.operator_popup);
        opMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //Gets data from operator button
        //If true, menu item is pressed
        Button operatorText=findViewById(R.id.operator);
        if(item.getItemId()==R.id.addition) {
            operatorText.setText("Add");
        } else if(item.getItemId()==R.id.subtraction) {
            operatorText.setText("Subtract");
        } else if(item.getItemId()==R.id.multiplication) {
            operatorText.setText("Multiply");
        } else if(item.getItemId()==R.id.division) {
            operatorText.setText("Divide");
        } else {
            return false;
        }
        return true;
    }
}