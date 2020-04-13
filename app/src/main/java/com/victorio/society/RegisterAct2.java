package com.victorio.society;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterAct2 extends AppCompatActivity {
    Button btn_save, btn_add_photo;
    EditText nama_lengkap, telepon, alamat, rt, rw, kelurahan, kecamatan, petunjuk;
    ImageView pic_photo_register;
    Uri photo_location;
    Integer photo_max = 1;
    DatabaseReference reference;
    StorageReference storageReference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_act2);
        getUsernameLocal();

        btn_save = findViewById(R.id.btn_regis2);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        telepon = findViewById(R.id.telepon);
        alamat = findViewById(R.id.alamat);
        rt = findViewById(R.id.rt);
        rw = findViewById(R.id.rw);
        kelurahan = findViewById(R.id.kelurahan);
        kecamatan = findViewById(R.id.kecamatan);
        petunjuk = findViewById(R.id.petunjuk);
        pic_photo_register = findViewById(R.id.pic_photo_register_user);


        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_save.setEnabled(false);
                btn_save.setText("Loading...");
                final String xnama_lengkap = nama_lengkap.getText().toString();
                final String xtelepon = telepon.getText().toString();
                final String xalamat = alamat.getText().toString();
                final String xrt = rt.getText().toString();
                final String xrw = rw.getText().toString();
                final String xkelurahan = kelurahan.getText().toString();
                final String xkecamatan = kecamatan.getText().toString();
                final String xpetunjuk = petunjuk.getText().toString();


                if (xnama_lengkap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama Lengkap tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }
                else if (xtelepon.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Telepon tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }
                else if (xalamat.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Alamat tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }
                else if (xrt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "RT tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }
                else if (xrw.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "RW tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }
                else if (xkelurahan.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Kelurahan tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }
                else if (xkecamatan.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Kecamatan tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }
                else if (xpetunjuk.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Berikan petunjuk rumah ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }
                else if (photo_location == null) {
                    Toast.makeText(getApplicationContext(), "Foto wajib diisi ! ", Toast.LENGTH_SHORT).show();
                    btn_save.setText("CONTINUE");
                    btn_save.setEnabled(true);
                }//

                else{
                    //menyimpan kepada database
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                    storageReference = FirebaseStorage.getInstance().getReference().child("PhotoUser").child(username_key_new);
                    //validasi file apakah ada
                    if (photo_location != null) {
                        final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                        storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //mengambil url foto dari firebase
                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri_photo = uri.toString();
                                        reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                        reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                        reference.getRef().child("telepon").setValue(telepon.getText().toString());
                                        reference.getRef().child("alamat").setValue(alamat.getText().toString());
                                        reference.getRef().child("rt").setValue(rt.getText().toString());
                                        reference.getRef().child("rw").setValue(rw.getText().toString());
                                        reference.getRef().child("petunjuk").setValue(petunjuk.getText().toString());
                                        reference.getRef().child("kecamatan").setValue(kecamatan.getText().toString());
                                        reference.getRef().child("kelurahan").setValue(kelurahan.getText().toString());
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        //pindah activity
                                        Intent abc = new Intent(RegisterAct2.this, HomeAct.class);
                                        startActivity(abc);
                                        finish();

                                    }
                                });


                            }
                        });
                    }

                }
            }
        });


    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    //membuat method mengambil foto
    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).noFade().centerCrop().fit().into(pic_photo_register);

        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
