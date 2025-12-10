package com.example.prueba2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarActivity extends AppCompatActivity {

    EditText txtNombre, txtApellidos, txtEdad;
    Button btnGuardar;

    String idEstudiante = ""; // ID del estudiante desde Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        // Vincular vistas del XML
        txtNombre = findViewById(R.id.input_nombre);
        txtApellidos = findViewById(R.id.input_apellidos);
        txtEdad = findViewById(R.id.input_edad);
        btnGuardar = findViewById(R.id.boton_editar);

        // Obtener ID enviado desde ListarActivity
        idEstudiante = getIntent().getStringExtra("ID");

        // Cargar los datos del estudiante
        cargarDatos();

        // Guardar cambios
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDatos() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("estudiantes")
                .document(idEstudiante)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            if (doc.exists()) {
                                txtNombre.setText(doc.getString("NOMBRE"));
                                txtApellidos.setText(doc.getString("APELLIDOS"));
                                txtEdad.setText(String.valueOf(doc.getLong("EDAD")));
                            }

                        } else {
                            Toast.makeText(EditarActivity.this,
                                    "Error al cargar datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void guardarCambios() {

        String nombre = txtNombre.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String edadStr = txtEdad.getText().toString().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || edadStr.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad = Integer.parseInt(edadStr);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("estudiantes")
                .document(idEstudiante)
                .update(
                        "NOMBRE", nombre,
                        "APELLIDOS", apellidos,
                        "EDAD", edad
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this,
                            "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Volver atrás automáticamente
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error al guardar cambios", Toast.LENGTH_SHORT).show()
                );
    }
}
