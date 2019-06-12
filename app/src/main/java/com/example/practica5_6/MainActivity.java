package com.example.practica5_6;

import android.content.DialogInterface;
import android.content.Intent;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_insertar, btn_cargar;

        btn_insertar = (Button) findViewById(R.id.btn_insert);
        btn_cargar = (Button) findViewById(R.id.btn_load);
        btn_insertar.setOnClickListener(this);
        btn_cargar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_insert){
            imagen();
        } else if (v.getId() == R.id.btn_load){
            cargarImagen();
        }
    }

    private void imagen(){
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(MainActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i] == "Tomar Foto"){
                    tomarFoto();
                } else if (opciones[i] == "Cargar Imagen"){
                    cargarImagen();
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicaion"),REQUEST_IMAGE_CAPTURE);
    }


    String currentPhotoPath;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            MediaScannerConnection.scanFile(this, new String[]{currentPhotoPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                }
            });
            Intent intent = new Intent(MainActivity.this, Images.class);
            intent.putExtra("insert", currentPhotoPath);
            startActivity(intent);

        }  else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Uri path = data.getData();
            Intent intent = new Intent(MainActivity.this, Images.class);
            intent.putExtra("load", path.toString());
            startActivity(intent);
        }
    }

}
