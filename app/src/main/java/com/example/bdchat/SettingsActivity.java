package com.example.bdchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bdchat.Models.Users;
import com.example.bdchat.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        storage = FirebaseStorage.getInstance();
        auth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
           startActivity(intent);
            }
        });


binding.saveButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        if(!binding.eatStatus.getText().toString().equals("") && !binding.textUsername.getText().toString().equals("")){

            String status = binding.eatStatus.getText().toString();
            String username = binding.textUsername.getText().toString();
            HashMap<String,Object> obj = new HashMap<>();
            obj.put("userName",username);
            obj.put("status",status);
            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                    .updateChildren(obj);

            Toast.makeText(SettingsActivity.this, "Perfil Actualizado", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(SettingsActivity.this, "Ingresar nombre y estado", Toast.LENGTH_SHORT).show();


        }


    }
});



        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent = new Intent();
intent.setAction(Intent.ACTION_GET_CONTENT);
intent.setType("image/*");
startActivityForResult(intent, 25);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri sFile = data.getData();
            binding.profileImage.setImageURI(sFile);
            final StorageReference reference = storage.getReference().child("profile_pic")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic").setValue(uri.toString());
                        }
                    });
                }
            });






        }
    }
}