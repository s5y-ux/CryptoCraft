package net.ddns.vcccd;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Stock {
	private final Main main;
	
	public Stock(Main main) {
		this.main = main;
	}
	
	public Map data() {
		try {
		URL url = new URL("https://api.coincap.io/v2/assets");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse JSON response
        String done = response.toString();
        
        Map jsonJavaRootObject = new Gson().fromJson(done, Map.class);
        //Map one = (Map)jsonJavaRootObject.get("data");
        ///List one = (List)(Map)jsonJavaRootObject.get("data");
        
        //player.sendMessage(jsonJavaRootObject.get("Global Quote").toString());
        // Close the connection
        connection.disconnect();
        return(jsonJavaRootObject);
	} catch (Exception e) {
		assert true;
		return(null);
	}

}
}
