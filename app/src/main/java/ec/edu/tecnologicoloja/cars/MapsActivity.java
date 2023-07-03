package ec.edu.tecnologicoloja.cars;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private Button loca;
    private Button car;
    public static final String MyPREFERENCES = "MyPrefs";

    public static final String latitud = "latitud" ;
    public static final String longitud = "longitud";


    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }


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
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        LatLng miubica = new LatLng(-1.10, -77.57);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miubica));
        CameraUpdate ZoomCam = CameraUpdateFactory.zoomTo(7);
        mMap.animateCamera(ZoomCam);
        mMap.setOnMapLongClickListener(this);

        loca = (Button) findViewById(R.id.btnLocaliz);
        car = (Button) findViewById(R.id.btncar);

        loca.setOnClickListener(this);
        car.setOnClickListener(this);

    }
    private void miPosicion (){

        LocationManager objLocation = null;
        LocationListener objLocListener;
        objLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        objLocListener = (LocationListener) new Miposicion();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},10000);
        }
        objLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,objLocListener);

        if(objLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            if(Miposicion.latitud != 0){
                double lat = Miposicion.latitud;
                double lon = Miposicion.longitud;

                Toast.makeText(
                        MapsActivity.this, "latitud" +lat+ "\n longitud"+ lon, Toast.LENGTH_SHORT).show();
                LatLng miubica = new LatLng(lat, lon);
                Marker mi_ubicacion= mMap.addMarker((new MarkerOptions().position(miubica).title("Mi Ubicacion")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miubica));
                CameraUpdate ZoomCam = CameraUpdateFactory.zoomTo(18);
                mMap.animateCamera(ZoomCam);
            }else {
                Toast.makeText(
                        MapsActivity.this, "La localizacion no esta funcionando", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v==loca){
            miPosicion();
        }else if(v==car) {
            double la = sharedpreferences.getFloat(latitud,0);
            double lo = sharedpreferences.getFloat(longitud, 0);
            LatLng miubica = new LatLng(la,lo);
            Marker mi_ubicacion = mMap.addMarker(new MarkerOptions().position(miubica)
                    .title("Mi vehiculo").icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_directions_car_24)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(miubica));
            CameraUpdate ZoomCam = CameraUpdateFactory.zoomTo(18);
            mMap.animateCamera(ZoomCam);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
            Toast.makeText(
                    MapsActivity.this, "latitud: " +latLng.latitude + "\n longitud: "+latLng.longitude,  Toast.LENGTH_SHORT).show();
            Marker mi_ubicacion = mMap.addMarker(new MarkerOptions().position(latLng).title("Mi vehiculo").icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_directions_car_24)));
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putFloat(latitud, (float) latLng.latitude);
            editor.putFloat(longitud, (float) latLng.longitude);

            editor.commit();
    }

}
