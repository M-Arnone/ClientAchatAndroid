package com.example.clientachatandroid.model;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {
    public static volatile Model instance;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Socket sClient;
    private String _requete;
    private ConfigReader cg;
    int numArticle = 1;
    int numClient;

    Context context;
    private ArrayList<Article> panier = new ArrayList<>();

    public ArrayList<Article> getPanier() {
        return panier;
    }

    public void addArt(Article A) {
        Article addTemp = new Article();
        boolean trouve = false;
        for (int i = 0; (getPanier() != null && getPanier().size() > i) && trouve == false; i++) {
            if (getPanier().get(i).getId() == A.getId()) {
                addTemp = getPanier().get(i);
                addTemp.setQuantite(addTemp.getQuantite() + A.getQuantite());
                trouve = true;
            }
        }

        if (trouve == false) {
            getPanier().add(A);
        }

        for (int i = 0; getPanier() != null && getPanier().size() > i; i++) {
            System.out.println("Panier client " + i + ":" + getPanier().get(i));
        }
    }

    public void setPanier(ArrayList<Article> panier) {
        this.panier = panier;
    }

    public int getNumArticle() {
        return numArticle;
    }

    public void setNumArticle(int numArticle) {
        this.numArticle = numArticle;
    }




    public String getRequete() {
        return _requete;
    }

    public void setRequete(String _requete) {
        this._requete = _requete;
    }

    public Article getArticleActuel() {
        // Appelez votre méthode pour récupérer l'article actuel
        try {
            return setArticle(numArticle);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getCaddie() throws IOException {
        setRequete("CADDIE");
        String reponse = Echange(getRequete());
        System.out.println(reponse);
        String[] mots = reponse.split("#");
        panier.clear();
        for (int i = 1; i < mots.length; i += 4) {
            panier.add(new Article(Integer.parseInt(mots[i]), mots[i + 1], Double.parseDouble(mots[i + 2]), Integer.parseInt(mots[i + 3])));
        }
        System.out.println(panier);
    }

    public boolean on_pushLogin(String nom, String pwd, boolean newClient) throws IOException {
        if (newClient)
            setRequete("LOGIN#" + nom + "#" + pwd + "#1");
        else setRequete("LOGIN#" + nom + "#" + pwd + "#0");
        String reponse = Echange(getRequete());
        if (reponse.contains("ko"))
            return false;

        String[] mots = reponse.split("#");
        numClient = Integer.parseInt(mots[2]);
        setArticle(numArticle);
        return true;
    }

    public void on_pushLogout() throws IOException {
        setRequete("LOGOUT#oui");
        Echange(getRequete());
    }

    public Article on_pushSuivant() throws IOException {
        numArticle++;
        return setArticle(numArticle);
    }

    public Article on_pushPrecedent() throws IOException {
        numArticle--;
        return setArticle(numArticle);
    }

    public void on_pushAcheter(int quantite) throws IOException {
        System.out.println("NUM ARTICLE : " + numArticle);
        System.out.println("Quantite : " + quantite);
        setRequete("ACHAT#" + numArticle + "#" + quantite);
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        if (mots[1].equals("ok")) {
            Article a = new Article(numArticle, mots[2], Double.parseDouble(mots[3]), quantite);
            addArt(a);
        } else System.err.println("Erreur d'achat !!");

    }

    public boolean on_pushSupprimerArticle(int id) throws IOException {
        setRequete("CANCEL#" + id);
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        if (mots[1].equals("ok")) {
            for (Article artPass : getPanier()) {
                if (artPass.getId() == id) {
                    getPanier().remove(artPass);
                    return true;
                }
            }
        } else {
            System.out.println("Erreur de suppression!!!");
            return false;
        }

        return false;
    }

    public boolean on_pushViderPanier() throws IOException {
        setRequete("CANCELALL");
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        if (mots[1].equals("ok")) {
            getPanier().clear();
            System.out.println("CANCELALL_OK");
            return true;
        }
        return false;
    }

    public boolean on_pushPayer(String total) throws IOException {
        setRequete("CONFIRMER#" + numClient + "#" + total);
        String reponse = Echange(getRequete());
        String[] mots = reponse.split("#");
        if (mots[1].equals("ok")) {
            getPanier().clear();
            System.out.println("Confirm_OK");
            return true;
        }
        return false;
    }

    private String Echange(String requete) throws IOException {
        // Envoie de la requête
        try {
            Send(requete);
        } catch (IOException e) {
            System.err.println("Erreur de Send : " + e.getMessage());
            sClient.close();
        }

        // Recevoir la réponse
        String reponse;
        try {
            reponse = Receive();
        } catch (IOException e) {
            System.err.println("Erreur de Receive : " + e.getMessage());
            sClient.close();
            throw e;
        }
        return reponse;
    }


    private void Send(String r) throws IOException {
        try {
            r += "#)";
            dos.write(r.getBytes());
            dos.flush();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi des données: " + e.getMessage());
            throw e;
        }
    }

    private String Receive() throws IOException {
        StringBuilder data = new StringBuilder();
        boolean finished = false;
        byte lastByte = 0;

        while (!finished) {
            int byteRead = dis.read();
            if (byteRead == -1) {
                throw new IOException("Erreur de lecture - Connection fermée ou autre erreur.");
            }

            char readChar = (char) byteRead;

            if (lastByte == '#' && readChar == ')') {
                finished = true;
            } else {
                data.append(readChar);
            }

            lastByte = (byte) readChar;
        }

        return data.toString();
    }

    public void connectToServer() throws IOException {
        try {
            ConfigReader cg = new ConfigReader(context);
            System.out.println("PORT : " + cg.getPort());
            sClient = new Socket(cg.getServerIP(), cg.getPort());
            dos = new DataOutputStream(sClient.getOutputStream());
            dis = new DataInputStream(sClient.getInputStream());
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
            throw e;
        }

    }

    public Article setArticle(int num) throws IOException {
        String reponse = null;
        Article a = null;
        setRequete("CONSULT#" + num);
        try {
            reponse = Echange(getRequete());
        } catch (IOException ex) {
            System.err.println("Erreur d'Echange - Consult : " + ex.getMessage());
        }
        String[] infos = reponse.split("#");

        if (infos.length >= 7) {

            String nomArticle = infos[3];
            double prix = Double.parseDouble(infos[4]);
            int quantite = Integer.parseInt(infos[5]);
            String nomFichierImage = infos[6];

            a = new Article(nomArticle, prix, quantite, nomFichierImage);

        } else {
            System.err.println("Réponse mal formée");
        }
        return a;

    }

    public static Model getInstance(Context c) throws SQLException, ClassNotFoundException, IOException {
        if (instance == null) {
            synchronized (Model.class) {
                if (instance == null) {
                    instance = new Model(c);
                }
            }
        }
        return instance;
    }

    @SuppressLint("StaticFieldLeak")
    public Model(Context c1) throws IOException {
        this.context = c1.getApplicationContext();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    connectToServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }


    public static void main(String[] args) throws ClassNotFoundException, SQLException {


    }
}
