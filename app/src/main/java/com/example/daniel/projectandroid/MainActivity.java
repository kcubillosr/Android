package com.example.daniel.projectandroid;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText nombreProducto, descripcion, precio;
    private double longitud, latitud;
    private ImageView imagen;
    private Button btnCaptura;
    private byte blob;
    private ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(1024);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreProducto = (EditText) findViewById(R.id.editText);
        descripcion = (EditText) findViewById(R.id.editText2);
        precio = (EditText) findViewById(R.id.editText3);
        imagen = (ImageView) findViewById(R.id.imageView);
        btnCaptura = (Button) findViewById(R.id.btnCaptura);

        btnCaptura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LLamarIntent();
            }
        });


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


    public void LLamarIntent()
    {
        //Nos lleva a un intent para tomar la fotografia
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager())!= null){
            //lo iniciamos con startActivityForResult esperando una respuesta que es la foto
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            //Intent es la acción para enlazar una nueva Actividad, Servicio o BroadcastReceiver.
            //Bundle es una coleccion de pares clave-valor.

            //captura la imagen y lo devuelve como un bundle
            Bundle extras =  null;
            extras = data.getExtras();
            //lo que recibimos como foto lo convertimos en un bitmap, la clave qui es data.
            //bitmap es una imagen, cualquier imagen digital es en realidad un mapa de bit o bitmap.
            Bitmap imageBitmap = null;
            imageBitmap = (Bitmap) extras.get("data");


            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, bytearrayoutputstream);

            //Mostramos la imagen en un imageView
            imagen.setImageBitmap(imageBitmap);
        }
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
        byte [] blob = null;
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
            blob = bytearrayoutputstream.toByteArray();

            Producto producto = new Producto(preci, longitud, latitud, blob, nombre, descri);

            dbHelper baseHelper = new dbHelper(this, "bd_projectoAndroid", null, 1);
            SQLiteDatabase db = baseHelper.getWritableDatabase();

            if(db != null){
                ContentValues objectDV = new ContentValues();
                objectDV.put("nombre", producto.getNombre());
                objectDV.put("descripcion", producto.getDescripcion());
                objectDV.put("precio", producto.getPrecio());
                objectDV.put("longituds", producto.getLongitud());
                objectDV.put("latituds", producto.getLatitud());
                objectDV.put("imagen", producto.getBlob());


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
