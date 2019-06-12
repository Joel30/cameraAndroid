package com.example.practica5_6;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.Key;

public class Images extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        getSupportActionBar().setTitle("Images");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView imageView;
        TextView textView, textView1;

        imageView = (ImageView) findViewById(R.id.imageView3);
        textView = (TextView) findViewById(R.id.textView);
        textView1 = (TextView) findViewById(R.id.textView1);

        String key = getIntent().getStringExtra("insert");

        if (key == null){
            key = getIntent().getStringExtra("load");
            imageView.setImageURI(Uri.parse(key));
            textView1.setText(key);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(key);
            imageView.setImageBitmap(bitmap);
            textView1.setText(key);
        }
    }
}
