package leilao;

import cripto.CriptoService;
import pessoa.Pessoa;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Emprestimo {


    private int id;

    private double jurosAtual;
    private Pessoa credor;
    private Pessoa tomador;
    private Moeda moedaSolicitada;
    private Double quantidadeMoedaSolicitada;
    private Moeda moedaGarantia;
    private Double quantidadeMoedaGarantia;
    private int quantidadeDeParcelas;
    private LocalDate dataFechamentoAcordo;
    private EstadoDoEmprestimo estadoDoEmprestimo;


    public Emprestimo(int id, double jurosAtual, Pessoa credor, Pessoa tomador,
                      Moeda moedaSolicitada, Double quantidadeMoedaSolicitada,
                      Moeda moedaGarantia, Double quantidadeMoedaGarantia,
                      int quantidadeDeParcelas, LocalDate dataFechamentoAcordo,
                      EstadoDoEmprestimo estadoDoEmprestimo) {
        this.id = id;
        this.jurosAtual = jurosAtual;
        this.credor = credor;
        this.tomador = tomador;
        this.moedaSolicitada = moedaSolicitada;
        this.quantidadeMoedaSolicitada = quantidadeMoedaSolicitada;
        this.moedaGarantia = moedaGarantia;
        this.quantidadeMoedaGarantia = quantidadeMoedaGarantia;
        this.quantidadeDeParcelas = quantidadeDeParcelas;
        this.dataFechamentoAcordo = dataFechamentoAcordo;
        this.estadoDoEmprestimo = estadoDoEmprestimo;
    }


    public Double getValorGarantiaEmDolar()
    {
        try
        {
            CriptoService.PriceTicker priceTicker = CriptoService.getPreco(moedaGarantia);
            if (moedaGarantia.getSimbolo().equals("US$"))
            {
                priceTicker.setPrice(1.00);
            }
            return priceTicker.getPrice() * quantidadeMoedaGarantia;
        }catch(Exception e)
        {
            throw new RuntimeException("Erro ao calcular valor da Garantia");
        }
    }

    public Double getValroSolicitadoEmDolar()
    {
        try
        {
            CriptoService.PriceTicker priceTicker = CriptoService.getPreco(moedaSolicitada);
            if (moedaSolicitada.getSimbolo().equals("US$"))
            {
                priceTicker.setPrice(1.00);
            }
            return priceTicker.getPrice() * quantidadeMoedaSolicitada;
        }catch(Exception e)
        {
            throw new RuntimeException("Erro ao calcular valor solicitado");
        }
    }



    public int getId() { return id; }
    public double getJurosAtual() { return jurosAtual; }
    public EstadoDoEmprestimo getEstadoDoEmprestimo() { return estadoDoEmprestimo; }
    public Pessoa getTomador() { return tomador; }
    public Moeda getMoedaSolicitada() { return moedaSolicitada; }
    public Double getQuantidadeMoedaSolicitada() { return quantidadeMoedaSolicitada; }
    public Moeda getMoedaGarantia() { return moedaGarantia; }
    public Double getQuantidadeMoedaGarantia() { return quantidadeMoedaGarantia; }
    public Pessoa getCredor() { return this.credor; }
    public int getQuantidadeDeParcelas() { return quantidadeDeParcelas; }
    public LocalDate getDataFechamentoAcordo() { return dataFechamentoAcordo; }


    public String getCredorAtual() {
        if (credor == null) {
            return "Ainda não existe um lance";
        }
        return credor.getName();
    }

    @Override
    public String toString() {
        // Adicionamos o ID e mais alguns campos para clareza
        return String.format(
                "Emprestimo (ID: %d) {\n" +
                        "\tTomador: %s\n" +
                        "\tCredor Atual: %s\n" +
                        "\tJuros Atual: %.2f%%\n" +
                        "\tValor solicitado: %s %s\n" +
                        "\tEstado do Emprestimo: %s\n" +
                        "}",
                id,
                tomador.getName(),
                getCredorAtual(),
                jurosAtual,
                moedaSolicitada.getSimbolo(),
                quantidadeMoedaSolicitada,
                estadoDoEmprestimo
        );
    }
}