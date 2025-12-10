package com.example.prueba2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarActivity extends AppCompatActivity {

    EditText inputNombre, inputApellidos, inputEdad;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar);

        // Vincular vistas
        inputNombre = findViewById(R.id.input_nombre);
        inputApellidos = findViewById(R.id.input_apellidos);
        inputEdad = findViewById(R.id.input_edad);
        btnGuardar = findViewById(R.id.botonGuardar);

        btnGuardar.setOnClickListener(v -> {

            EditText input_nombre = findViewById(R.id.input_nombre);
            EditText input_apellidos = findViewById(R.id.input_apellidos);
            EditText input_edad = findViewById(R.id.input_edad);

            String nombre = input_nombre.getText().toString().trim();
            String apellido = input_apellidos.getText().toString().trim();
            String edad = input_edad.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            String id = db.collection("estudiantes").document().getId();

            Map<String, Object> data = new HashMap<>();
            data.put("ID", id);
            data.put("NOMBRE", nombre);
            data.put("APELLIDOS", apellido);
            data.put("EDAD", Integer.parseInt(edad));

            db.collection("estudiantes")
                    .document(id)
                    .set(data)
                    .addOnSuccessListener(a -> {
                        Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
        });


    }
}
