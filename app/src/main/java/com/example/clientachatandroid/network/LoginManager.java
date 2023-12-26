package com.example.clientachatandroid.network;

import android.content.Context;
import android.os.AsyncTask;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class LoginManager {

    private Context context;
    private Model model;

    public LoginManager(Context context) throws SQLException, IOException, ClassNotFoundException {
        this.context = context;
        this.model = Model.getInstance(context);
    }

    public void performLoginAsync(String username, String password, OnLoginCompleteListener listener) {
        new LoginTask(listener).execute(username, password);
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {
        private final OnLoginCompleteListener listener;

        public LoginTask(OnLoginCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(String... params) {
            boolean success = false;

            if (params[0] == null || params[0].equals("") || params[1] == null || params[1].equals("")){
                listener.onLoginFailed();
                return null;
            }

                try {
                    success = model.on_pushLogin(params[0], params[1], false);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (listener != null) {
                if (success)
                    listener.onLoginComplete();
                else
                    listener.onLoginFailed();
            }

            return null;
        }
    }

    public interface OnLoginCompleteListener {
        void onLoginComplete();

        void onLoginFailed();
    }
}
