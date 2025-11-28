package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditarActivity extends AppCompatActivity {

    EditText txtNombre, txtApellidos, txtEdad;
    Button btnGuardar;

    int idEstudiante = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        // Vincular vistas
        txtNombre = findViewById(R.id.input_nombre);
        txtApellidos = findViewById(R.id.input_apellidos);
        txtEdad = findViewById(R.id.input_edad);
        btnGuardar = findViewById(R.id.boton_editar);

        // Obtener ID desde VerActivity
        idEstudiante = getIntent().getIntExtra("ID", 0);

        // Cargar los datos actuales
        cargarDatos();

        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDatos() {

        SQLiteDatabase db = openOrCreateDatabase(
                "BD_ESTUDIANTES",
                MODE_PRIVATE,
                null
        );

        Cursor c = db.rawQuery(
                "SELECT NOMBRE, APELLIDOS, EDAD FROM ESTUDIANTES WHERE ID=" + idEstudiante,
                null
        );

        if (c.moveToFirst()) {
            txtNombre.setText(c.getString(0));
            txtApellidos.setText(c.getString(1));
            txtEdad.setText(String.valueOf(c.getInt(2)));
        }

        c.close();
        db.close();
    }

    private void guardarCambios() {

        String nombre = txtNombre.getText().toString();
        String apellidos = txtApellidos.getText().toString();
        String edad = txtEdad.getText().toString();

        if (nombre.isEmpty() || apellidos.isEmpty() || edad.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = openOrCreateDatabase(
                "BD_ESTUDIANTES",
                MODE_PRIVATE,
                null
        );

        String sql = "UPDATE ESTUDIANTES SET NOMBRE=?, APELLIDOS=?, EDAD=? WHERE ID=?";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, nombre);
        stmt.bindString(2, apellidos);
        stmt.bindString(3, edad);
        stmt.bindLong(4, idEstudiante);
        stmt.execute();

        db.close();

        Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();

        finish(); // volver a VerActivity
    }
}
