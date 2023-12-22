package net.ddns.vcccd;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/*
This class is used in order to retrieve information from coin caps
public API. This API is used to receive information on crypto
currencies. Please keep in mind that if this API becomes
EOL there will have to be a major re-factor in this project.

This API is public. I have done my best to probe the API as
Little as possible in this project. This wrapper will never be
public, so if you're seeing this comment remember that piracy 
is bad and don't abuse this API because it is awesome and you
shoulden't ruin it for the rest of us.

Thank you :)
*/

public class MarketData {

    //Used as a Map to store the raw data for class methods
	//This is the most important variable in the entire plugin
    private Map<?, ?> Data = constructData();

    //Used to construct the JSON data for the rest of the class
    private Map<?, ?> constructData(){

        //Exception Handling for API
        try {

            //API URL for JSON data on Crypto
            URL url = new URL("https://api.coincap.io/v2/assets");

            //Creates the connection and sends a GET request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //Response is read and stored in a String Builder object reading the lines in the Buffer
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            //Closes the reader and Parses the JSON response
            reader.close();
            String usableString = response.toString();

            //Uses Gson (Thanks Craftbukkit, you rule) to Map elements to the Root Object
            Map<?, ?> jsonJavaRootObject = new Gson().fromJson(usableString, Map.class);

            //Disconnects from the HTTP URL Connection
            connection.disconnect();

            //Returns the raw map object
            return (jsonJavaRootObject);

        } catch (Exception e) {

            //Otherwise, we can just send an error to the server console
            return (null);
        }
    }

    /*
     * Everything from this comment down
     * includes accessor and mutator
     * methods. Please avoid accessing Raw Data
     * if possible (Note to myself) 
    */
    
    //Used to get the raw JSON data as a Map Object
    public Map<?, ?> getRawData() {
        return(Data);
    }
    
    //Accessor method for id
    public String getID() {
    	return(Data.get("id").toString());
    }
    
    //Accessor method for rank
    public String getRank() {
    	return(Data.get("rank").toString());
    }
    
    //Accessor method for symbol
    public String getName() {
    	return(Data.get("name").toString());
    }
    
    //Accessor method for supply
    public String getSupply() {
    	return(Data.get("supply").toString());
    }
}