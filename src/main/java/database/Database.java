package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {


    private static final String DB_URL = "jdbc:sqlite:loans_range.db";


    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);


        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initialize() {

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {


            String sqlPessoas = "CREATE TABLE IF NOT EXISTS pessoas (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name TEXT NOT NULL," +
                    " age INTEGER NOT NULL," +
                    " email TEXT NOT NULL UNIQUE," +
                    " passwordHash TEXT NOT NULL," +
                    " phone TEXT," +
                    " cpf TEXT NOT NULL UNIQUE," +
                    " tipoPessoa TEXT NOT NULL" +
                    ");";


            String sqlEmprestimos = "CREATE TABLE IF NOT EXISTS emprestimos (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " jurosAtual REAL NOT NULL," +
                    " id_credor INTEGER," +
                    " id_tomador INTEGER NOT NULL," +
                    " moedaSolicitada TEXT NOT NULL," +
                    " quantidadeMoedaSolicitada REAL NOT NULL," +
                    " moedaGarantia TEXT NOT NULL," +
                    " quantidadeMoedaGarantia REAL NOT NULL," +
                    " quantidadeDeParcelas INTEGER NOT NULL," +
                    " dataFechamentoAcordo TEXT," +
                    " estadoDoEmprestimo TEXT NOT NULL," +
                    " FOREIGN KEY(id_credor) REFERENCES pessoas(id)," +
                    " FOREIGN KEY(id_tomador) REFERENCES pessoas(id)" +
                    ");";


            String sqlParcelas = "CREATE TABLE IF NOT EXISTS parcelas (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " id_emprestimo INTEGER NOT NULL," +
                    " dataVencimento TEXT NOT NULL," +
                    " FOREIGN KEY(id_emprestimo) REFERENCES emprestimos(id) ON DELETE CASCADE" +
                    ");";


            stmt.execute(sqlPessoas);
            stmt.execute(sqlEmprestimos);
            stmt.execute(sqlParcelas);

            System.out.println("Banco de dados inicializado com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}