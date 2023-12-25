package com.example.clientachatandroid;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clientachatandroid.model.Article;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private List<Article> panier = new ArrayList<>();
    private PanierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Add articles to your cart as needed
        panier.add(new Article(1, "Article 1", 29.99, 2));
        panier.add(new Article(2, "Article 2", 29.99, 2));

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new PanierAdapter(panier, new PanierAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Vous pouvez gérer le clic sur un élément ici si nécessaire
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Button to clear the cart
        Button clearCartButton = findViewById(R.id.clearCartButton);
        clearCartButton.setOnClickListener(view -> clearCart());
    }

    private void removeItem(int position) {
        panier.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private void clearCart() {
        panier.clear();
        adapter.notifyDataSetChanged();
    }
}
