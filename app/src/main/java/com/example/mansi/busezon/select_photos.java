package com.example.mansi.busezon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mansi.busezon.adapters.ImagesAdapter;
import com.haresh.multipleimagepickerlibrary.MultiImageSelector;

import java.util.ArrayList;
import java.util.List;

public class select_photos extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerViewImages;
    private GridLayoutManager gridLayoutManager;

    private ArrayList<String> mSelectedImagesList = new ArrayList<>();
    private final int MAX_IMAGE_SELECTION_LIMIT=100;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 401;
    private final int REQUEST_IMAGE=301;

    private MultiImageSelector mMultiImageSelector;
    private ImagesAdapter mImagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photos);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //getActionBar().setTitle("BuSezon");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        recyclerViewImages = (RecyclerView) findViewById(R.id.recycler_view_images);
        gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setLayoutManager(gridLayoutManager);


        mMultiImageSelector = MultiImageSelector.create();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkAndRequestPermissions()) {
                    mMultiImageSelector.showCamera(true);
                    mMultiImageSelector.count(MAX_IMAGE_SELECTION_LIMIT);
                    mMultiImageSelector.multi();
                    mMultiImageSelector.origin(mSelectedImagesList);
                    mMultiImageSelector.start(select_photos.this, REQUEST_IMAGE);

//                mMultiImageSelector.showCamera(true);
//                mMultiImageSelector.single();
//                mMultiImageSelector.origin(mSelectedImagesList);
//                mMultiImageSelector.start(MainActivity.this, REQUEST_IMAGE);
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            try {
                mSelectedImagesList = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                mImagesAdapter = new ImagesAdapter(this,mSelectedImagesList);
                recyclerViewImages.setAdapter(mImagesAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        int externalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            floatingActionButton.performClick();
        }
    }


}

