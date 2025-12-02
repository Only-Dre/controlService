package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // AJUSTE o nome do banco de acordo com o seu MySQL Workbench
    private static final String URL = "jdbc:mysql://localhost:3306/control_service?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "aluno";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {

        try {
            // Carrega o driver do MySQL
            Class.forName(DRIVER);

            System.out.println("Conectando ao banco control_service...");
            return DriverManager.getConnection(URL, USER, PASS);

        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado. Verifique o mysql-connector-j na pasta lib.");
            throw new RuntimeException("Erro: Driver JDBC ausente.", e);

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados MySQL.");
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter a conexão.", e);
        }
    }
}
