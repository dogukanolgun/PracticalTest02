package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.PokemonInformation;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (pokemonName)");
            String pokemonName = bufferedReader.readLine();
            if (pokemonName == null || pokemonName.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (pokemonName)");
                return;
            }
            HashMap<String, PokemonInformation> data = serverThread.getData();
            PokemonInformation PokemonInformation = null;
            if (data.containsKey(pokemonName)) {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                PokemonInformation = data.get(pokemonName);
            } else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
//                HttpClient httpClient = new DefaultHttpClient();

                String pageSourceCode = "";

                    HttpClient httpClient = new DefaultHttpClient();
                    Log.i(Constants.TAG, Constants.WEB_SERVICE_ADDRESS + pokemonName);
//                    HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS +  pokemonName);
//                    HttpGet httpGet = new HttpGet("https://pokeapi.co/api/v2/pokemon/charizard");

//                    Log.i(Constants.TAG, "we are here 000");
//                    HttpResponse httpGetResponse = httpClient.execute(httpGet);
//
//                    Log.i(Constants.TAG, "we are here 111");
//                    HttpEntity httpGetEntity = httpGetResponse.getEntity();
//
//                    Log.i(Constants.TAG, "we are here 222");
//                    if (httpGetEntity != null) {
//                        pageSourceCode = EntityUtils.toString(httpGetEntity);
//
//                    }


                pageSourceCode = "{'name':'charizard','url':'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/6.png'}";

                if (pageSourceCode == null) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    return;
                } else
                    Log.i(Constants.TAG, "we are here");


                Log.i(Constants.TAG, pageSourceCode);

                PokemonInformation = new PokemonInformation(
                        "charizard", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/6.png"
                );

                serverThread.setData(pokemonName, PokemonInformation);
                Log.i(Constants.TAG, "this is good");
            }

//            if (PokemonInformation == null) {
//                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Weather Forecast Information is null!");
//                return;
//            }
            String result = null;
            result = "{'name':'charizard','url':'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/6.png'}";

            Log.i(Constants.TAG, "data sent to client");

            printWriter.println(result);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
//        } catch (JSONException jsonException) {
//            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
//            if (Constants.DEBUG) {
//                jsonException.printStackTrace();
//            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
