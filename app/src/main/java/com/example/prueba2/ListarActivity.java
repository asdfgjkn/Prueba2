package com.example.prueba2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListarActivity extends AppCompatActivity {

    private static final String TAG = "ListarActivity";

    private ListView listView;
    private Button botonAgregar;
    private ArrayList<String> listaEstudiantes;
    private ArrayList<String> listaIDs;  // ahora IDs son String (Firebase)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        listView = findViewById(R.id.lista_estudiantes);
        botonAgregar = findViewById(R.id.boton_agregar);

        botonAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(ListarActivity.this, AgregarActivity.class);
            startActivity(intent);
        });

        listaEstudiantes = new ArrayList<>();
        listaIDs = new ArrayList<>();

        cargarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
    }

    private void cargarLista() {

        listaEstudiantes.clear();
        listaIDs.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("estudiantes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot documentos = task.getResult();

                            if (documentos.isEmpty()) {
                                Toast.makeText(ListarActivity.this, "No hay estudiantes registrados", Toast.LENGTH_SHORT).show();
                            } else {

                                for (DocumentSnapshot doc : documentos) {

                                    String id = doc.getId(); // ID del documento
                                    String nombre = doc.getString("NOMBRE");
                                    String apellido = doc.getString("APELLIDOS");
                                    int edad = doc.getLong("EDAD").intValue();

                                    listaIDs.add(id);
                                    listaEstudiantes.add(nombre + " " + apellido + " — Edad: " + edad);
                                }
                            }

                            actualizarListView();

                        } else {
                            Log.e(TAG, "Error cargando datos Firebase", task.getException());
                            Toast.makeText(ListarActivity.this, "Error al cargar datos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void actualizarListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaEstudiantes
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < listaIDs.size()) {

                String idSeleccionado = listaIDs.get(position);

                Intent intent = new Intent(ListarActivity.this, VerActivity.class);
                intent.putExtra("ID", idSeleccionado);
                startActivity(intent);
            }
        });
    }
}
