package com.example.prueba2;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Crear base de datos
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES",
                Context.MODE_PRIVATE, null);

        // 2. Crear tabla si no existe
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS ESTUDIANTES (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "NOMBRE TEXT," +
                        "APELLIDOS TEXT," +
                        "EDAD INTEGER)"
        );

        // 3. Crear hilo que espere 5 segundos
        Thread hilo = new Thread(() -> {
            try {
                Thread.sleep(5000); // 5 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 4. Redirigir a ListarActivity
            Intent intent = new Intent(MainActivity.this, ListarActivity.class);
            startActivity(intent);
            finish(); // Cierra la pantalla de inicio
        });

        hilo.start();
    }
}
