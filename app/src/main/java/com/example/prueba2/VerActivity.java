package com.example.prueba2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class VerActivity extends AppCompatActivity {

    TextView txtNombre, txtApellidos, txtEdad;
    Button btnEditar, btnEliminar;

    String idEstudiante = "";   // ahora es String (Firebase)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtEdad = findViewById(R.id.txtEdad);

        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Obtener ID desde ListarActivity
        idEstudiante = getIntent().getStringExtra("ID");

        // Cargar datos
        cargarDatos();

        // Editar
        btnEditar.setOnClickListener(v -> {
            Intent i = new Intent(VerActivity.this, EditarActivity.class);
            i.putExtra("ID", idEstudiante);
            startActivity(i);
        });

        // Eliminar
        btnEliminar.setOnClickListener(v -> eliminarEstudiante(idEstudiante));
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

                                String nombre = doc.getString("NOMBRE");
                                String apellidos = doc.getString("APELLIDOS");
                                int edad = doc.getLong("EDAD").intValue();

                                txtNombre.setText(nombre);
                                txtApellidos.setText(apellidos);
                                txtEdad.setText(String.valueOf(edad));

                            } else {
                                Toast.makeText(VerActivity.this, "El estudiante no existe", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(VerActivity.this, "Error cargando datos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void eliminarEstudiante(String id) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("estudiantes")
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(VerActivity.this, "Estudiante eliminado", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(VerActivity.this, ListarActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VerActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                });
    }
}
