package ec.edu.tecnologicoloja.cars;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

public class Miposicion {

    public static double latitud, longitud, altitud;

    public static boolean statusGPS;

    public static Location coordenadas;

    @Override
    public void setLocationChange(@NonNull Location location){
        latitud = location.getLatitude();
        longitud = location.getLongitude();
        coordenadas = location;
        altitud = location.getAltitude();
    }
    @Override
    public void onFlushComplete(int requestCode){
        LocationListener.super.onFlushComplete(requestCode);
    }
    @Override
    public void onProviderEnabled(String provider){
        statusGPS = true;
    }
    public void onProviderDisabled(String provider){
        statusGPS = false;
    }

}
