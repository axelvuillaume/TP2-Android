package com.example.tp2;

import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class ColorPickerActivity extends AppCompatActivity {

    GridView colorGridView;
    TextView pickColorText;

    int[] colors = {
            Color.parseColor("#FF5733"),
            Color.parseColor("#33FF57"),
            Color.parseColor("#3357FF"),
            Color.parseColor("#57FF33"),
            Color.parseColor("#FF33FF"),
            Color.parseColor("#5733FF"),
            Color.parseColor("#FF3357"),
            Color.parseColor("#33FFFF"),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background_picker);

        pickColorText = findViewById(R.id.pickColorText);
        colorGridView = findViewById(R.id.colorGridView);

        ColorAdapter colorAdapter = new ColorAdapter(this, colors);
        colorGridView.setAdapter(colorAdapter);

        colorGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedColor = colors[position];
                returnSelectedColor(selectedColor);
            }
        });
    }


    private void returnSelectedColor(int selectedColor) {
        Intent intent = new Intent();
        intent.putExtra("selectedColor", selectedColor);
        setResult(RESULT_OK, intent);
        finish();
    }
}