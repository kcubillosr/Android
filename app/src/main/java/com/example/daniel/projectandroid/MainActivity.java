package com.example.daniel.projectandroid;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nombreProducto, descripcion, precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreProducto = (EditText) findViewById(R.id.editText);
        descripcion = (EditText) findViewById(R.id.editText2);
        precio = (EditText) findViewById(R.id.editText3);
    }

    public void insertar(View view){
        if(nombreProducto.getText().toString().trim().equalsIgnoreCase("")){
            nombreProducto.setError("Debe ingresar un nombre");
        } else if (descripcion.getText().toString().trim().equalsIgnoreCase("")){
            descripcion.setError("Debe ingresar una descripción");
        } else if(precio.getText().toString().trim().equalsIgnoreCase("")){
            precio.setError("Debe ingresar precio del producto");
        } else{

            String nombre, descri;
            int preci;
            nombre = nombreProducto.getText().toString();
            descri = descripcion.getText().toString();
            preci = Integer.parseInt(precio.getText().toString());

            dbHelper baseHelper = new dbHelper(this, "bd_projectoAndroid", null, 1);
            SQLiteDatabase db = baseHelper.getWritableDatabase();

            if(db != null){
                ContentValues objectDV = new ContentValues();
                objectDV.put("nombre", nombre);
                objectDV.put("descripcion", descri);
                objectDV.put("precio", preci);

                long nfilas = db.insert("tbl_productos", null, objectDV);

                if(nfilas>0){
                    Toast.makeText(this, "Producto registrado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al grabar producto", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
