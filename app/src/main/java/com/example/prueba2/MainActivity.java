package com.example.prueba2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pantalla de carga 5 segundos
        Thread hilo = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(MainActivity.this, ListarActivity.class);
            startActivity(intent);
            finish();
        });

        hilo.start();
    }
}
