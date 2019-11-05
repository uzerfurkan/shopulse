package com.example.shopulse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);


        totalAmount = getIntent().getStringExtra("Toplam Fiyat");
        Toast.makeText(this, "Toplam Fiyat = ₺"+ totalAmount, Toast.LENGTH_SHORT).show();


        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_siparis_btn);
        nameEditText = (EditText) findViewById(R.id.shippment_isim);
        nameEditText = (EditText) findViewById(R.id.shippment_telefon);
        nameEditText = (EditText) findViewById(R.id.shippment_adres);
        nameEditText = (EditText) findViewById(R.id.shippment_sehir);
    }
}
