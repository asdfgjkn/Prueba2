package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ListarActivity extends AppCompatActivity {


        private static final String TAG = "ListarActivity";

        private ListView listView;
        private Button botonAgregar;
        private ArrayList<String> listaEstudiantes;
        private ArrayList<Integer> listaIDs;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_listar); // asegúrate que este es el nombre correcto

            listView = findViewById(R.id.lista_estudiantes); // ID debe coincidir con tu XML
            botonAgregar = findViewById(R.id.boton_agregar); // si existe en tu XML

            // Botón para ir a AgregarActivity
            botonAgregar.setOnClickListener(v -> {
                Intent intent = new Intent(ListarActivity.this, AgregarActivity.class);
                startActivity(intent);
            });

            // Inicializar listas
            listaEstudiantes = new ArrayList<>();
            listaIDs = new ArrayList<>();

            // Primera carga
            cargarLista();
        }

        // Se vuelve a cargar al volver a la Activity
        @Override
        protected void onResume() {
            super.onResume();
            cargarLista();
        }

        // Método seguro para cargar datos desde SQLite
        private void cargarLista() {
            listaEstudiantes.clear();
            listaIDs.clear();

            SQLiteDatabase db = null;
            Cursor c = null;

            try {
                // Abrir base de datos (debe haberse creado antes en MainActivity)
                db = openOrCreateDatabase("BD_ESTUDIANTES", MODE_PRIVATE, null);

                // Asegurarse de que la tabla exista (opcional, evita crash)
                db.execSQL("CREATE TABLE IF NOT EXISTS ESTUDIANTES (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE TEXT, APELLIDOS TEXT, EDAD INTEGER)");

                // Ejecutar consulta
                c = db.rawQuery("SELECT ID, NOMBRE, APELLIDOS, EDAD FROM ESTUDIANTES", null);

                if (c != null && c.moveToFirst()) {
                    do {
                        int id = c.getInt(0);
                        String nombre = c.getString(1);
                        String apellidos = c.getString(2);
                        int edad = c.getInt(3);

                        listaIDs.add(id);
                        listaEstudiantes.add(nombre + " " + apellidos + " — Edad: " + edad);
                    } while (c.moveToNext());
                } else {
                    // Si no hay datos, mostrar mensaje opcional
                    Toast.makeText(this, "No hay estudiantes registrados", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                Log.e(TAG, "Error cargando lista: " + ex.getMessage(), ex);
                Toast.makeText(this, "Error al leer la base de datos", Toast.LENGTH_LONG).show();
            } finally {
                // Cerrar cursor y BD si existen
                if (c != null && !c.isClosed()) c.close();
                if (db != null && db.isOpen()) db.close();
            }

            // Actualizar adaptador (aunque la lista esté vacía)
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaEstudiantes
            );
            listView.setAdapter(adapter);

            // Listener para abrir VerActivity al hacer click
            listView.setOnItemClickListener((parent, view, position, id) -> {
                if (position >= 0 && position < listaIDs.size()) {
                    int idSeleccionado = listaIDs.get(position);
                    Intent intent = new Intent(ListarActivity.this, VerActivity.class);
                    intent.putExtra("ID", idSeleccionado);
                    startActivity(intent);
                }
            });
        }
    }

