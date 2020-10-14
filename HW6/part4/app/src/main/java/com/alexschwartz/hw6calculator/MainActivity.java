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

        final EditText firstField = findViewById(R.id.input1);
        final Button operator = findViewById(R.id.operator);
        final EditText secondField = findViewById(R.id.input2);
        Button done = findViewById(R.id.equals);
        final TextView answer = findViewById(R.id.answer);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstField.getText().length()==0 || secondField.getText().length()==0) {
                    answer.setText("Missing input(s)");
                } else {
                    float num1 = Float.parseFloat(firstField.getText().toString());
                    float num2 = Float.parseFloat(secondField.getText().toString());
                    if(operator.getText()=="Add") {
                        answer.setText(Float.toString(num1 + num2));
                    } else if(operator.getText()=="Subtract") {
                        answer.setText(Float.toString(num1 - num2));
                    } else if(operator.getText()=="Multiply") {
                        answer.setText(Float.toString(num1 * num2));
                    } else if(operator.getText()=="Divide") {
                        if(num2==0) {
                            answer.setText("Undefined");
                        } else {
                            answer.setText(Float.toString(num1 / num2));
                        }
                    } else {
                        answer.setText("Missing operator");
                    }
                }
            }
        });
    }

    public void operatorMenu(View view) {
        PopupMenu opMenu = new PopupMenu(this, view);
        opMenu.setOnMenuItemClickListener(this);
        opMenu.inflate(R.menu.operator_popup);
        opMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
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