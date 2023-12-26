package com.example.clientachatandroid.network;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.clientachatandroid.activities.CartActivity;
import com.example.clientachatandroid.activities.main.MainActivity;
import com.example.clientachatandroid.databinding.PanierAdapter;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class PayAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final Model model;
    private final PanierAdapter adapter;
    private final double cartPrice;
    private Context context;

    public PayAsyncTask(PanierAdapter adapter, double total, Context context) {
        cartPrice = total;
        try {
            this.model = Model.getInstance(null);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            return model.on_pushPayer(Double.toString(cartPrice));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(Boolean paySuccessful) {
        if (paySuccessful){
            model.getPanier().clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(context, "Payement Succesful", Toast.LENGTH_SHORT).show();
        }
        else System.out.println("Payement error");
    }
}
