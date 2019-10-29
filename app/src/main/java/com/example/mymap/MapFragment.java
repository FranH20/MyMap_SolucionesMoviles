package com.example.mymap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

  GoogleMap gMap;
  LocationManager lm;
  Location location;
  double longitude = 0;
  double latitude = 0;
  int[] mapaso = new int[]{0, 1, 2, 3, 4};

  public MapFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View v = inflater.inflate(R.layout.fragment_map, container, false);
    final SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    Button btndistancia = v.findViewById(R.id.btndistancia);

    Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
    String[] letra = {"Ubicacion Actual","Japon","Alemania","Italia","Francia"};
    ArrayAdapter adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, letra);
    spinner.setAdapter(adapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item;
        item = (String) parent.getItemAtPosition(position);
        if(item.equals("Ubicacion Actual")){
          ObtenerUbicacionActual();
          onMapReady(gMap);
        }
        if(item.equals("Japon")){
          latitude = 35.680513;
          longitude = 139.769051;
          onMapReady(gMap);
        }
        if(item.equals("Alemania")){
          latitude = 52.516934;
          longitude = 13.403190;
          onMapReady(gMap);
        }
        if(item.equals("Italia")){
          latitude = 41.902609;
          longitude = 12.494847;
          onMapReady(gMap);
        }
        if(item.equals("Francia")){
          latitude = 48.843489;
          longitude = 2.355331;
          onMapReady(gMap);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    btndistancia.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ObtenerUbicacionEntreDosPuntos();
      }
    });

    Spinner spinnerMapa = (Spinner) v.findViewById(R.id.spinnerCambiarMapa);
    String[] mapas = {"Normal","Ninguno","Satelite","Hibrido","Terreno"};
    ArrayAdapter adapterMapa = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, mapas);
    spinnerMapa.setAdapter(adapterMapa);

    spinnerMapa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item;
        item = (String) parent.getItemAtPosition(position);
        if(item.equals("Ninguno")){
          gMap.setMapType(mapaso[0]);
        }
        if(item.equals("Normal")){
          gMap.setMapType(mapaso[1]);
        }
        if(item.equals("Satelite")){
          gMap.setMapType(mapaso[2]);
        }
        if(item.equals("Hibrido")){
          gMap.setMapType(mapaso[4]);
        }
        if(item.equals("Terreno")){
          gMap.setMapType(mapaso[3]);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    return v;
  }



  @Override
  public void onMapReady(GoogleMap googleMap) {
    gMap = googleMap;

    LatLng aqui = new LatLng(latitude, longitude);

    CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(aqui)
            .zoom(14)//zoom
            .bearing(30)//inclinacion
            .build();

    gMap.addMarker(new MarkerOptions().position(aqui).title("Mi ubicación"));
    gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


  }


  @Override
  public void onLocationChanged(Location location) {
    if (location != null) {
      Log.v("Location Changed", latitude + " and " + longitude);
      lm.removeUpdates(this);
    }
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    String A;
  }

  @Override
  public void onProviderEnabled(String provider) {
    String A;
  }

  @Override
  public void onProviderDisabled(String provider) {
    String A;
  }

  public void ObtenerUbicacionEntreDosPuntos(){

    lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    }

    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);
    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    Location localizacionNueva = new Location("punto B");
    localizacionNueva.setLatitude(latitude);
    localizacionNueva.setLongitude(longitude);

    longitude = location.getLongitude();
    latitude = location.getLatitude();

    Location localizacionActual = new Location("punto A");
    localizacionActual.setLatitude(latitude);
    localizacionActual.setLongitude(longitude);

    float distance = localizacionActual.distanceTo(localizacionNueva);
    Toast.makeText(getActivity(),"Hola la distancia desde tu ubicacion hasta la seleccionada en unidades de metros es : " + distance,Toast.LENGTH_SHORT).show();
  }

  public void ObtenerUbicacionActual(){
    lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    }

    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);
    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    longitude = location.getLongitude();
    latitude = location.getLatitude();
  }

}
