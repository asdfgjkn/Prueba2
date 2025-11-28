package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class VerActivity extends AppCompatActivity {

    TextView txtNombre, txtApellidos, txtEdad;
    Button btnEditar, btnEliminar;
    int idEstudiante = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        // Vincular vistas
        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtEdad = findViewById(R.id.txtEdad);

        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Obtener el ID enviado desde la lista
        idEstudiante = getIntent().getIntExtra("ID", 0);

        // Cargar los datos del estudiante
        cargarDatos();

        // Botón editar
        btnEditar.setOnClickListener(v -> {
            Intent i = new Intent(VerActivity.this, EditarActivity.class);
            i.putExtra("ID", idEstudiante);
            startActivity(i);
        });

        // Botón eliminar
        btnEliminar.setOnClickListener(v -> eliminarEstudiante(idEstudiante));
    }


    private void cargarDatos() {
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES",
                MODE_PRIVATE, null);

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


    private void eliminarEstudiante(int id) {
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES",
                MODE_PRIVATE, null);

        String sql = "DELETE FROM ESTUDIANTES WHERE ID=?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, id);
        stmt.execute();
        db.close();

        Toast.makeText(this, "Estudiante eliminado", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(VerActivity.this, ListarActivity.class);
        startActivity(intent);
        finish();
    }
}
