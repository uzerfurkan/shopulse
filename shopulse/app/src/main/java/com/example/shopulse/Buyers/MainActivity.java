 package com.example.shopulse.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopulse.Model.Users;
import com.example.shopulse.Prevalent.Prevalent;
import com.example.shopulse.R;
import com.example.shopulse.Sellers.SellerHomeActivity;
import com.example.shopulse.Sellers.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

 public class MainActivity extends AppCompatActivity
 {

     private Button joinNowButton, loginButton;
     private ProgressDialog loadingBar;
     private TextView sellerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        sellerBegin = (TextView) findViewById(R.id.satici_olmak_istiyorum);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Zaten Giriş Yapılmış");
                loadingBar.setMessage("Lütfen Bekleyiniz....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }


     @Override
     protected void onStart()
     {
         super.onStart();

         FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

         if (firebaseUser != null)
         {
             Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(intent);
             finish();
         }
     }






     private void AllowAccess(final String phone, final String password)
     {

         final DatabaseReference RootRef;
         RootRef = FirebaseDatabase.getInstance().getReference();

         RootRef.addListenerForSingleValueEvent(new ValueEventListener()
         {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot)
             {
                 if (dataSnapshot.child("Users").child(phone).exists())
                 {
                     Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                     if (usersData.getPhone().equals(phone))
                     {
                         if (usersData.getPassword().equals(password))
                         {
                             Toast.makeText(MainActivity.this, "Zaten giriş yapıldı..", Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();

                             Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                             Prevalent.currentOnlineUser = usersData;
                             startActivity(intent);
                         }

                         else
                         {
                             loadingBar.dismiss();
                             Toast.makeText(MainActivity.this, "Şifre Hatalı", Toast.LENGTH_SHORT).show();
                         }
                     }
                 }
                 else
                 {
                     Toast.makeText(MainActivity.this, "Bu" + phone + "hesap yok.", Toast.LENGTH_SHORT).show();
                     loadingBar.dismiss();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError)
             {

             }
         });
     }
 }
