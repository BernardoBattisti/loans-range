package leilao;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public enum Moeda
{

    BTC("BTC", "Bitcoin", "BTCUSDT"),
    USDT("US$", "Dólar Americano", "USDTBRL"),
    ETH("ETH", "Ethereum", "ETHUSDT"),
    SOL("SOL", "Solana", "SOLUSDT"),
    BNB("BNB", "Binance Coin", "BNBUSDT"),
    XRP("XRP", "Ripple", "XRPUSDT"),
    ADA("ADA", "Cardano", "ADAUSDT"),
    DOGE("DOGE", "Dogecoin", "DOGEUSDT"),
    LINK("LINK", "Chainlink", "LINKUSDT"),
    MATIC("MATIC", "Polygon", "MATICUSDT");


    private final String simbolo;
    private final String descricao;
    private final String rastreamento;


    Moeda(String simbolo, String descricao, String rastreamento) {
        this.simbolo = simbolo;
        this.descricao = descricao;
        this.rastreamento = rastreamento;

    }

    public String getSimbolo() {
        return this.simbolo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getRastreamento() {
        return this.rastreamento;
    }
}