package cripto;
import com.google.gson.Gson;
import leilao.Moeda;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import leilao.Moeda;


public class CriptoService {


    public class PriceTicker
    {
        String simbolo;
        Double price;
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }


    public static PriceTicker getPreco(Moeda moeda) throws Exception
    {


        String simboloApi = moeda.getRastreamento();
        String apiUrl = "https://api.binance.com/api/v3/ticker/price?symbol=" + simboloApi;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha na API: " + response.body());
        }

        Gson gson = new Gson();
        PriceTicker ticker = gson.fromJson(response.body(), PriceTicker.class);

        return ticker;
    }

}