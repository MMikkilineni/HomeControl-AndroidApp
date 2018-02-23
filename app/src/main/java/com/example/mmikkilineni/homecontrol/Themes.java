package com.example.mmikkilineni.homecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

import static com.example.mmikkilineni.homecontrol.HomeControl.btSocket;


public class Themes extends AppCompatActivity {

    ToggleButton GoodEvening, GoodNight, Romantic;
    Button Back1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        GoodEvening = (ToggleButton) findViewById(R.id.btn_goodevng);
        GoodNight = (ToggleButton) findViewById(R.id.btn_goodnight);
        Romantic = (ToggleButton) findViewById(R.id.btn_romantic);
        Back1 = (Button)findViewById(R.id.button2);

        GoodEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnGoodEveningOn();
            }
        });

        GoodNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnGoodNightOn();
            }
        });

        Romantic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnRomanticOn();
            }
        });

        Back1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(Themes.this, HomeControl.class);
                startActivity(int1);
            }
        });
    }

    private void turnGoodNightOn() {
        if(GoodNight.isChecked()) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("9".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
        }
        else {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("8".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
        }
    }

    private void turnRomanticOn() {
        if(Romantic.isChecked()) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("7".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
        }
        else {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("6".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
        }
    }

    private void turnGoodEveningOn() {
        if(GoodEvening.isChecked()) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("8".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
        }
        else {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("6".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
