package com.example.shopulse.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shopulse.Model.AdminOrders;
import com.example.shopulse.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity
{

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }



    private RecyclerView orderList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);


        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");


        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, final int i, @NonNull final AdminOrders adminOrders)
                    {
                        adminOrdersViewHolder.userName.setText("İsim: " + adminOrders.getName());
                        adminOrdersViewHolder.userPhoneNumber.setText("Telefon: " + adminOrders.getPhone());
                        adminOrdersViewHolder.userTotalPrice.setText("Toplam Fiyat = ₺ " + adminOrders.getTotalAmount());
                        adminOrdersViewHolder.userDatetime.setText("Sipariş Tarihi: " + adminOrders.getDate() + "" + adminOrders.getTime());
                        adminOrdersViewHolder.userShippingAddress.setText("Kargolanacak Adres: " + adminOrders.getAddress()+ ", " + adminOrders.getCity());

                        adminOrdersViewHolder.ShowOrderBtn.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                String uID = getRef(i).getKey();

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

                        adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Evet",
                                                "Hayır"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Verilmiş siparişi onaylıyor musunuz ? ");



                                builder.setItems(options, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (which == 0)
                                        {
                                            String uID = getRef(i).getKey();

                                            RemoverOrder(uID);
                                        }
                                        else
                                        {
                                            finish();
                                        }
                                    }
                                });

                                builder.show();
                            }

                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }




    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userPhoneNumber, userTotalPrice, userDatetime, userShippingAddress;
        public Button ShowOrderBtn;

        public AdminOrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDatetime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            ShowOrderBtn = itemView.findViewById(R.id.show_all_products_btn);

        }
    }


    private void RemoverOrder(String uID)
    {
        orderRef.child(uID).removeValue();
    }
}
