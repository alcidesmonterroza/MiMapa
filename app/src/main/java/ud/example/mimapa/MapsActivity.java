package ud.example.mimapa;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private final LatLng SAN= new LatLng(12.587151, -81.698713);
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng miubicacion;
    private Spinner lista01;
    private CheckBox checkmarker;
    private EditText latitud,longitud;
    private Button buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                miubicacion = new LatLng(location.getLatitude(),location.getLongitude());
            }
        };
        latitud = findViewById(R.id.textLat);
        longitud = findViewById(R.id.textLong);
        checkmarker = findViewById(R.id.checkBox);
        lista01 = findViewById(R.id.spinner1);
        lista01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String tipomapa = lista01.getSelectedItem().toString();
                if(tipomapa.equals("Normal")){mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);}
                if(tipomapa.equals("Satelital")){mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);}
                if(tipomapa.equals("Híbrido")){mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);}
                if(tipomapa.equals("Terreno")){mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);}
                if(tipomapa.equals("None")){mMap.setMapType(GoogleMap.MAP_TYPE_NONE);}


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        revisarpermisos();
    }

    private void revisarpermisos() {
      //  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission
                    (this,Manifest.permission.ACCESS_COARSE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},101);
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,locationListener);
                 }
        }

   // }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        if(checkmarker.isChecked()){
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener((GoogleMap.OnMapClickListener)this);
       mMap.getUiSettings().setZoomControlsEnabled(true);
      mMap.getUiSettings().setCompassEnabled(true);

    }

    public void MapaNormal(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void MapaSatelite(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void IrParaiso(View v){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SAN,15));
    }

    public void irUD(View v){
        LatLng UD = new LatLng(4.6281058,-74.0659999);
        if(checkmarker.isChecked()) {
            Marker miMaker = mMap.addMarker(new MarkerOptions()
                    .position(UD)
                    .title("UD")
                    .snippet("Universidad Distrital FJC"));

            miMaker.showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UD,17));
    }

    public void Miubicacion(View v){
        try {

            Toast.makeText(MapsActivity.this,"aqui está", Toast.LENGTH_SHORT).show();
            if(checkmarker.isChecked()) {
                mMap.addMarker(new MarkerOptions().position(miubicacion).title("Ubicacion Actual"));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miubicacion,17));




        } catch (Exception ex){

        }
    }


    public void limpiar(View v){
        mMap.clear();
    }

    public void addMarker(View v){
        LatLng TempLatLng = mMap.getCameraPosition().target;
        mMap.addMarker(new MarkerOptions()
                .position(TempLatLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        );


    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(checkmarker.isChecked()){
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

    public void ubicaciondada(View v){
        double x = Double.valueOf(latitud.getText().toString());
        double y = Double.valueOf(longitud.getText().toString());
        LatLng dada= new LatLng(x,y);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dada,15));
        mMap.addMarker(new MarkerOptions().position(dada).title("Ubicación dada"));

    }

}