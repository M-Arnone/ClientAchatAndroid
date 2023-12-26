package com.example.clientachatandroid.network;

import android.os.AsyncTask;
import com.example.clientachatandroid.databinding.PanierAdapter;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class PayAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final Model model;
    private final PanierAdapter adapter;
    private final double cartPrice;

    public PayAsyncTask(PanierAdapter adapter, double total) {
        cartPrice = total;
        try {
            this.model = Model.getInstance(null);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        this.adapter = adapter;
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
        }
        else System.out.println("Payement error");
    }
}
