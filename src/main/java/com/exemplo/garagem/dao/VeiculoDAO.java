package com.exemplo.garagem.dao;

import com.exemplo.garagem.models.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {
    private final Connection conexao;

    public VeiculoDAO() {
        this.conexao = DBConnection.getInstance().getConnection();
    }

    public void criar(Veiculo veiculo, int usuarioId) throws SQLException {
        String query = "INSERT INTO veiculos (marca, modelo, ano, placa, usuario_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, veiculo.getMarca());
            stmt.setString(2, veiculo.getModelo());
            stmt.setInt(3, veiculo.getAno());
            stmt.setString(4, veiculo.getPlaca());
            stmt.setInt(5, usuarioId);
            stmt.executeUpdate();

            // Obter ID gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    veiculo.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Veiculo> listarPorUsuario(int usuarioId) throws SQLException {
        List<Veiculo> veiculos = new ArrayList<>();
        String query = "SELECT * FROM veiculos WHERE usuario_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Veiculo v = new Veiculo(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getInt("ano"),
                            rs.getString("placa")
                    );
                    veiculos.add(v);
                }
            }
        }
        return veiculos;
    }

    public boolean deletarVeiculo(int id, int usuarioId) throws SQLException {
        String query = "DELETE FROM veiculos WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setInt(2, usuarioId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean atualizarVeiculo(Veiculo veiculo, int usuarioId) throws SQLException {
        String query = "UPDATE veiculos SET marca = ?, modelo = ?, ano = ?, placa = ? WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setString(1, veiculo.getMarca());
            stmt.setString(2, veiculo.getModelo());
            stmt.setInt(3, veiculo.getAno());
            stmt.setString(4, veiculo.getPlaca());
            stmt.setInt(5, veiculo.getId());
            stmt.setInt(6, usuarioId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Veiculo buscarPorId(int id, int usuarioId) throws SQLException {
        String query = "SELECT * FROM veiculos WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setInt(2, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Veiculo(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getInt("ano"),
                            rs.getString("placa")
                    );
                }
            }
        }
        return null;
    }
}