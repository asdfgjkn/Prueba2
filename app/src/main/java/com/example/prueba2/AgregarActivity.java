package com.example.prueba2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarActivity extends AppCompatActivity {


        private SQLiteDatabase db;
        private EditText txtNombre, txtApellidos, txtEdad;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_agregar);

            db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS ESTUDIANTES (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NOMBRE TEXT," +
                    "APELLIDOS TEXT," +
                    "EDAD INTEGER)");

            txtNombre = findViewById(R.id.input_nombre);
            txtApellidos = findViewById(R.id.input_apellidos);
            txtEdad = findViewById(R.id.input_edad);

            Button botonGuardar = findViewById(R.id.botonGuardar);
            botonGuardar.setOnClickListener(v -> insertarEstudiante());
        }

        private void insertarEstudiante() {

            String nombre = txtNombre.getText().toString().trim();
            String apellidos = txtApellidos.getText().toString().trim();
            String edadTexto = txtEdad.getText().toString().trim();

            if (nombre.isEmpty() || apellidos.isEmpty() || edadTexto.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int edad = Integer.parseInt(edadTexto);

            try {
                String sql = "INSERT INTO ESTUDIANTES (NOMBRE, APELLIDOS, EDAD) VALUES (?, ?, ?)";
                SQLiteStatement st = db.compileStatement(sql);

                st.bindString(1, nombre);
                st.bindString(2, apellidos);
                st.bindLong(3, edad);

                st.executeInsert();

                Toast.makeText(this, "Estudiante agregado", Toast.LENGTH_SHORT).show();

                // Volver a ListarActivity
                startActivity(new Intent(AgregarActivity.this, ListarActivity.class));
                finish();

            } catch (Exception e) {
                Toast.makeText(this, "Error al insertar", Toast.LENGTH_SHORT).show();
            }
        }

}