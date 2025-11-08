package leilao;

import database.Database;
import pessoa.Pessoa;
import pessoa.UserService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class LeilaoService {


    public static void criarEmprestimo(double jurosInicial, Pessoa tomador, Moeda moedaSolicitada,
                                       double quantidadeMoedaSolicitada, Moeda moedaGarantia,
                                       double quantidadeMoedaGarantia, int parcelas) throws SQLException {

        String sql = "INSERT INTO emprestimos (jurosAtual, id_tomador, moedaSolicitada, quantidadeMoedaSolicitada, " +
                "moedaGarantia, quantidadeMoedaGarantia, quantidadeDeParcelas, estadoDoEmprestimo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, jurosInicial);
            pstmt.setInt(2, tomador.getId());
            pstmt.setString(3, moedaSolicitada.name());
            pstmt.setDouble(4, quantidadeMoedaSolicitada);
            pstmt.setString(5, moedaGarantia.name());
            pstmt.setDouble(6, quantidadeMoedaGarantia);
            pstmt.setInt(7, parcelas);
            pstmt.setString(8, EstadoDoEmprestimo.ABERTO.name());

            pstmt.executeUpdate();
        }
    }


    public static void darLance(int emprestimoId, int credorId, double novoJuros) throws SQLException {


        String sql = "UPDATE emprestimos SET jurosAtual = ?, id_credor = ? WHERE id = ?";



        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, novoJuros);
            pstmt.setInt(2, credorId);
            pstmt.setInt(3, emprestimoId);
            pstmt.executeUpdate();
        }
    }


    public static void encerrarLeilao(Emprestimo emprestimo) throws SQLException {
        LocalDate dataFechamento = LocalDate.now();


        String sqlUpdate = "UPDATE emprestimos SET estadoDoEmprestimo = ?, dataFechamentoAcordo = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

            pstmt.setString(1, EstadoDoEmprestimo.FECHADO.name());
            pstmt.setString(2, dataFechamento.toString());
            pstmt.setInt(3, emprestimo.getId());
            pstmt.executeUpdate();
        }


        String sqlInsertParcela = "INSERT INTO parcelas (id_emprestimo, dataVencimento) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertParcela)) {

            for (int i = 0; i < emprestimo.getQuantidadeDeParcelas(); i++) {
                LocalDate dataVencimento = dataFechamento.plusMonths(i + 1);
                pstmt.setInt(1, emprestimo.getId());
                pstmt.setString(2, dataVencimento.toString());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }


    }


    public static int getParcelasRestantesCount(int emprestimoId) throws SQLException {
        String sqlCount = "SELECT COUNT(*) FROM parcelas WHERE id_emprestimo = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmtCount = conn.prepareStatement(sqlCount)) {

            pstmtCount.setInt(1, emprestimoId);
            try (ResultSet rs = pstmtCount.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }


    public static void pagarFatura(int emprestimoId) throws SQLException {
        Connection conn = null;

        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);


            String sqlDeleteParcela = "DELETE FROM parcelas WHERE id = (" +
                    "SELECT id FROM parcelas " +
                    "WHERE id_emprestimo = ? " +
                    "ORDER BY dataVencimento ASC LIMIT 1" +
                    ")";

            try (PreparedStatement pstmtDeleteParcela = conn.prepareStatement(sqlDeleteParcela)) {
                pstmtDeleteParcela.setInt(1, emprestimoId);
                pstmtDeleteParcela.executeUpdate();
            }


            int parcelasRestantes = 0;
            String sqlCount = "SELECT COUNT(*) FROM parcelas WHERE id_emprestimo = ?";

            try (PreparedStatement pstmtCount = conn.prepareStatement(sqlCount)) {
                pstmtCount.setInt(1, emprestimoId);
                try (ResultSet rs = pstmtCount.executeQuery()) {
                    if (rs.next()) {
                        parcelasRestantes = rs.getInt(1);
                    }
                }
            }


            if (parcelasRestantes == 0) {
                String sqlDeleteEmprestimo = "DELETE FROM emprestimos WHERE id = ?";
                try (PreparedStatement pstmtDeleteEmprestimo = conn.prepareStatement(sqlDeleteEmprestimo)) {
                    pstmtDeleteEmprestimo.setInt(1, emprestimoId);
                    pstmtDeleteEmprestimo.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {

            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {

            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }


    public static boolean prazoDePagamentoAtingido(int emprestimoId) throws SQLException {
        LocalDate proximoPagamento = getProximaParcela(emprestimoId);
        if (proximoPagamento == null) {
            return false;
        }
        return LocalDate.now().isAfter(proximoPagamento) || LocalDate.now().isEqual(proximoPagamento);
    }


    public static LocalDate getProximaParcela(int emprestimoId) throws SQLException
    {
        String sql = "SELECT dataVencimento FROM parcelas " +
                "WHERE id_emprestimo = ? " +
                "ORDER BY dataVencimento ASC LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, emprestimoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return LocalDate.parse(rs.getString("dataVencimento"));
                }
            }
        }
        return null;
    }



    public static List<Emprestimo> getEmprestimosAbertos() throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE estadoDoEmprestimo = 'ABERTO'";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            return extrairListaDeEmprestimos(rs);
        }
    }


    public static List<Emprestimo> getEmprestimosPorTomador(int tomadorId) throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE id_tomador = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tomadorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return extrairListaDeEmprestimos(rs);
            }
        }
    }


    public static List<Emprestimo> getEmprestimosPorCredor(int credorId) throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE id_credor = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, credorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return extrairListaDeEmprestimos(rs);
            }
        }
    }


    private static List<Emprestimo> extrairListaDeEmprestimos(ResultSet rs) throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            double jurosAtual = rs.getDouble("jurosAtual");
            int id_credor = rs.getInt("id_credor");
            int id_tomador = rs.getInt("id_tomador");


            Pessoa credor = (id_credor > 0) ? UserService.findPessoaById(id_credor) : null;
            Pessoa tomador = UserService.findPessoaById(id_tomador);


            Moeda moedaSolicitada = Moeda.valueOf(rs.getString("moedaSolicitada"));
            double qtdMoedaSolicitada = rs.getDouble("quantidadeMoedaSolicitada");
            Moeda moedaGarantia = Moeda.valueOf(rs.getString("moedaGarantia"));
            double qtdMoedaGarantia = rs.getDouble("quantidadeMoedaGarantia");
            int parcelas = rs.getInt("quantidadeDeParcelas");

            // Converte a String de data do banco de volta para LocalDate
            String dataFechamentoStr = rs.getString("dataFechamentoAcordo");
            LocalDate dataFechamento = (dataFechamentoStr != null) ? LocalDate.parse(dataFechamentoStr) : null;

            EstadoDoEmprestimo estado = EstadoDoEmprestimo.valueOf(rs.getString("estadoDoEmprestimo"));


            Emprestimo emp = new Emprestimo(id, jurosAtual, credor, tomador, moedaSolicitada,
                    qtdMoedaSolicitada, moedaGarantia, qtdMoedaGarantia, parcelas, dataFechamento, estado);

            if (estado == EstadoDoEmprestimo.FECHADO) {
                int parcelasRestantes = getParcelasRestantesCount(id);
                emp.setParcelasRestantes(parcelasRestantes);
            }

            emprestimos.add(emp);
        }
        return emprestimos;
    }
}