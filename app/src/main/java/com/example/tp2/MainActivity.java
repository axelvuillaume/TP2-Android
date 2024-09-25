package com.example.tp2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private LinearLayout main;
    private int selectedColor;
    private String contactName;
    private TextView contactNameTextView;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {


                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        selectedColor = intent.getIntExtra("selectedColor",-1);
                        main.setBackgroundColor(selectedColor);


                    }
                }
            });

    ActivityResultLauncher<Intent> pickContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            Uri contactUri = intent.getData();
                            retrieveContactName(contactUri);
                        }
                    }
                }
            }
    );

    private void retrieveContactName(Uri contactUri) {
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contactName = cursor.getString(nameIndex);
            contactNameTextView.setText("Nom du contact : " + contactName);
            cursor.close();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("selectedColor", selectedColor);
        outState.putString("contactName", contactName);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedColor = savedInstanceState.getInt("selectedColor");
        contactName = savedInstanceState.getString("contactName");

        if (contactName != "") {
            contactNameTextView.setText("Nom du contact : " + contactName);
        }

        if (selectedColor != -1) {
            main.setBackgroundColor(selectedColor);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = findViewById(R.id.main);
        contactNameTextView = findViewById(R.id.contactNameTextView);

        Button backgroundButton = findViewById(R.id.backgroundButton);
        Button button_open_web = findViewById(R.id.button_open_web);
        Button pickContactButton = findViewById(R.id.pickContactButton);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Toast.makeText(this, "Accès aux contacts autorisé", Toast.LENGTH_SHORT).show();
        }

        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartForResult.launch(new Intent(MainActivity.this, ColorPickerActivity.class));
            }
        });



        button_open_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openWebIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.example.com"));
                startActivity(openWebIntent);
            }
        });

        pickContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                pickContactLauncher.launch(pickContactIntent);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Accès aux contacts autorisé", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission d'accès aux contacts refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }



}