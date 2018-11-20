package com.example.daniel.projectandroid;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nombreProducto, descripcion, precio;
    private double longitud, latitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreProducto = (EditText) findViewById(R.id.editText);
        descripcion = (EditText) findViewById(R.id.editText2);
        precio = (EditText) findViewById(R.id.editText3);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion localizacion = new Localizacion();
        localizacion.setMainActivity(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) localizacion);
    }

    public class Localizacion implements LocationListener{
        MainActivity mainActivity;

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        @Override
        public void onLocationChanged(Location location) {
            longitud = location.getLongitude();
            latitud = location.getLatitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
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
            Toast.makeText(this, "El valor en longitud es: " + longitud + " y el valor en latitud es: " + latitud, Toast.LENGTH_SHORT).show();
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
                objectDV.put("longituds", longitud);
                objectDV.put("latituds", latitud);

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
