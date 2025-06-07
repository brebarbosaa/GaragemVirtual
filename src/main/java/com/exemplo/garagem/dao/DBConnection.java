package com.exemplo.garagem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection conexao;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexao = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/garagem_virtual?useSSL=false&serverTimezone=UTC",
                    "root",
                    "1234"
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/garagem_virtual?useSSL=false&serverTimezone=UTC",
                        "root",
                        "1234"
                );
            }
            return conexao;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter conexão", e);
        }
    }
}