package com.example.clientachatandroid.activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clientachatandroid.R;
import com.example.clientachatandroid.databinding.PanierAdapter;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;
import com.example.clientachatandroid.network.CancelAsyncTask;
import com.example.clientachatandroid.network.PayAsyncTask;
import com.example.clientachatandroid.network.UpdateCartAsyncTask;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends MenuActivity {
    private List<Article> panier = new ArrayList<>();
    private PanierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        try {
            panier = Model.getInstance(null).getPanier();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new PanierAdapter(panier);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new UpdateCartAsyncTask(adapter).execute();

        // Button to clear the cart
        Button clearCartButton = findViewById(R.id.clearCartButton);
        clearCartButton.setOnClickListener(view -> clearCart());

        // Button to pay
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(view -> payCart());

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void clearCart() {
        new CancelAsyncTask(adapter, -1).execute();
    }

    private void payCart() {
        new PayAsyncTask(adapter, computeTotal(), getApplicationContext()).execute();
    }

    private double computeTotal() {
        double total = 0;
        for (Article a :
                panier) {
            total += a.getPrix() * a.getQuantite();
        }
        return total;
    }
}
