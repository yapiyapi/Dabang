package com.example.firstproject.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.firstproject.Activity_Map;
import com.example.firstproject.Activity_PostToChat;
import com.example.firstproject.MainActivity;
import com.example.firstproject.R;
import com.example.firstproject.databinding.FragmentMapBinding;
import com.example.firstproject.object.Message;
import com.example.firstproject.object.Post;
import com.example.firstproject.object.Signup_member;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Iterator;

public class Fragment_Map extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    SharedPreferences 게시글_s;
    Gson gson; private String 게시글,이미지,제목,식별번호;
    private double lat, log;
    private FragmentMapBinding binding;
    private GoogleMap mMap;
    private MarkerOptions mOptions = new MarkerOptions();
    final LatLng s = new LatLng(37.5554, 126.9706);

    private MapView mapView;
    private SupportMapFragment supportMapFragment;
    private AutocompleteSupportFragment autocompleteFragment;
    private TextView finishMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_f);
        supportMapFragment.getMapAsync(this);
        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(s, 10));
        marker();

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }else{  checkLocationPermissionWithRationale();}


    }

    private void marker() {
        /** SharedPreferences **/
        게시글_s = getActivity().getSharedPreferences("post", MODE_PRIVATE);
        /** gson **/
        gson = new GsonBuilder().create();
        /** Iterator **/
        Iterator iterator = 게시글_s.getAll().keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            게시글 = 게시글_s.getString(key,"게시글 없음");//json
            if(!게시글.equals("게시글 없음")) {
                Post post = gson.fromJson(게시글, Post.class); // post 객체
                이미지 = MainActivity.getRealPathFromURI(getContext(), Uri.parse(post.get이미지()));
                제목 = post.get제목();
                식별번호 = post.get식별부호();
                lat = post.getLat();
                log = post.getLog();
            }
            LatLng location = new LatLng(lat,log);
            mMap.addMarker(new MarkerOptions()
                    .position(location).title(제목).snippet(식별번호)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getActivity(), Activity_PostToChat.class);
        intent.putExtra("key", marker.getSnippet());
        startActivity(intent);
    }
    
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("위치정보")
                        .setMessage("이 앱을 사용하기 위해서는 위치정보에 접근이 필요합니다. 위치정보 접근을 허용하여 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


}