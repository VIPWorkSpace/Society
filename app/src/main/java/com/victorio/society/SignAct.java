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

public class SignAct extends AppCompatActivity {
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    Button btn_craete, btn_login;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        btn_craete = findViewById(R.id.btn_create);
        btn_login = findViewById(R.id.btn_sign_in);
        username = findViewById(R.id.xusername);
        password = findViewById(R.id.xpassword);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mengubah bentuk button
                btn_login.setText("Redirecting...");
                btn_login.setEnabled(false);
                //mengambil user id di database
                final String xusername = username.getText().toString();
                final String xpassword = password.getText().toString();

                if (xusername.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username Kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_login.setText("SIGN IN");
                    btn_login.setEnabled(true);
                }
                else if (xpassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_login.setText("SIGN IN");
                    btn_login.setEnabled(true);
                }
                else{
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(xusername);
                    //melakukan validasi apakah id ada di database
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                // ambil data from firebase
                                String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                //validasi password ke database
                                if (xpassword.equals(passwordFromFirebase)) {
                                    //Menyimpan data kepada local storage
                                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(username_key, username.getText().toString());
                                    editor.apply();
                                    //pindah activity
                                    Intent login = new Intent(SignAct.this, HomeAct.class);
                                    startActivity(login);
                                    btn_login.setText("Redirecting...");
                                    btn_login.setEnabled(false);
                                    finish();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Password salah ! ", Toast.LENGTH_SHORT).show();
                                    btn_login.setText("SIGN IN");
                                    btn_login.setEnabled(true);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Username salah ! ", Toast.LENGTH_SHORT).show();
                                btn_login.setText("SIGN IN");
                                btn_login.setEnabled(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });


        btn_craete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(SignAct.this, RegisterAct.class);
                startActivity(register);
            }
        });
    }
}
