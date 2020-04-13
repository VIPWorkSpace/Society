package com.victorio.society;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterAct extends AppCompatActivity {
    DatabaseReference reference_username, reference;
    Button btn_regis1;
    EditText username, password, email;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_regis1 = findViewById(R.id.btn_regis1);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);


        btn_regis1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ubah state menjadi loading
                btn_regis1.setText("Loading ...");
                btn_regis1.setEnabled(false);
                final String xusername = username.getText().toString();
                final String xpassword = password.getText().toString();
                final String xemail_address = email.getText().toString();
                //mengambil username dari firebase
                reference_username = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(username.getText().toString());
                reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "Username sudah ada! ", Toast.LENGTH_SHORT).show();
                            btn_regis1.setText("CONTINUE");
                            btn_regis1.setEnabled(true);
                        }
                        else if (xusername.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Username Kosong ! ", Toast.LENGTH_SHORT).show();
                            btn_regis1.setText("CONTINUE");
                            btn_regis1.setEnabled(true);
                        } else if (xpassword.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Password Kosong ! ", Toast.LENGTH_SHORT).show();
                            btn_regis1.setText("CONTINUE");
                            btn_regis1.setEnabled(true);
                        } else if (xemail_address.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Email Kosong ! ", Toast.LENGTH_SHORT).show();
                            btn_regis1.setText("CONTINUE");
                            btn_regis1.setEnabled(true);
                        }
                        else{
                            //Menyimpan data kepada local storage
                            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(username_key, username.getText().toString());
                            editor.apply();

                            //Simpan kepada database
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString());
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                                    dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                                    dataSnapshot.getRef().child("email_address").setValue(email.getText().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Intent regis2 = new Intent(RegisterAct.this, RegisterAct2.class);
                            startActivity(regis2);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
