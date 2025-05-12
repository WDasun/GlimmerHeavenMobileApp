package com.example.glimmerheaven.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.glimmerheaven.BuildConfig;
import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.Shop;
import com.example.glimmerheaven.utils.dialogs.SimpleAlertDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Polyline currentPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_google_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(GoogleMapActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }else{
            Toast.makeText(this, "Location access is denied. You have to allow permission to continue!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;

        ArrayList<Shop> shopList = new ArrayList<>();

        Shop shop1 = new Shop();
        shop1.setShopName("AAAAA");
        shop1.setAddressLineOne("asdas/A");
        shop1.setAddressLineTwo("asdas asft3");
        shop1.setEmail("jhsdkajsd@gmail.com");
        shop1.setContactNumber("0332233248");
        shop1.setLatitude(7.092761);
        shop1.setLongitude(80.006922);

        Shop shop2 = new Shop();
        shop2.setShopName("BBBBB");
        shop2.setAddressLineOne("asdaccc/B");
        shop2.setAddressLineTwo("www ffff");
        shop2.setEmail("4r3r34f@gmail.com");
        shop2.setContactNumber("0332233248");
        shop2.setLatitude(7.092745);
        shop2.setLongitude(80.002319);

        shopList.add(shop1);
        shopList.add(shop2);



        LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions option = new MarkerOptions().position(myLocation).title("My Location");
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        myMap.addMarker(option);

        for(Shop shop : shopList){
            LatLng latLng = new LatLng(shop.getLatitude(), shop.getLongitude());
            myMap.addMarker(new MarkerOptions().position(latLng).title(shop.getShopName()));
        }

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));

        myMap.setOnMarkerClickListener(marker -> {

            String title = marker.getTitle();

            for(Shop shop : shopList){
                if(title.equals(shop.getShopName())){
                    LatLng destinationShop = new LatLng(shop.getLatitude(), shop.getLongitude());
                    drawRoute(myLocation, destinationShop);
                    new SimpleAlertDialog(this,
                            shop.getShopName(),
                            "Address : "+shop.getAddressLineOne()+" "+shop.getAddressLineTwo()+"\n\n"
                                    +"Contact : "+shop.getContactNumber()+"\n\n"
                                    +"Email : "+shop.getEmail()+"\n\n",
                            "Call now",
                            "close",
                            () -> {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + shop.getContactNumber()));
                                startActivity(intent);
                            },
                            null)
                            .createDialog().show();
                    break;
                }
            }
            return false;
        });
    }

    private void drawRoute(LatLng origin, LatLng destination) {

        if(currentPolyline != null){
            currentPolyline.remove();
        }

        String mapApi = BuildConfig.MAPS_API_KEY;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.latitude + "," + origin.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&key=" + mapApi;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray routes = response.getJSONArray("routes");
                        if (routes.length() > 0) {
                            JSONObject route = routes.getJSONObject(0);
                            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                            String encodedPolyline = overviewPolyline.getString("points");
                            List<LatLng> points = decodePolyline(encodedPolyline);
                            currentPolyline = myMap.addPolyline(new PolylineOptions()
                                    .addAll(points)
                                    .width(10)
                                    .color(Color.BLUE));
                        } else {
                            Toast.makeText(this, "No route found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("ROUTE", "Volley error: " + error.getMessage());
                    Toast.makeText(this, "Failed to get route", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    // Polyline decoder
    public List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng point = new LatLng(lat / 1E5, lng / 1E5);
            poly.add(point);
        }
        return poly;
    }
}