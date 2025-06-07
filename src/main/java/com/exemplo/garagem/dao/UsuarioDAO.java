package com.exemplo.garagem.dao;

import com.exemplo.garagem.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final Connection conexao;

    public UsuarioDAO() {
        this.conexao = DBConnection.getInstance().getConnection();
    }

    // Metodo para autenticação
    public Usuario autenticar(String email, String senha) throws SQLException {
        String query = "SELECT id, nome, email FROM usuarios WHERE email = ? AND senha = SHA2(?, 256)";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email")
                    );
                }
            }
        }
        return null;
    }

    // Metodo para criar usuário
    public void criarUsuario(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, SHA2(?, 256))";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();
        }
    }

    // Metodo para verificar se email já existe
    public boolean emailExiste(String email) throws SQLException {
        String query = "SELECT 1 FROM usuarios WHERE email = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Metodo para listar usuários
    public List<Usuario> listarUsuarios() throws SQLException {
        String query = "SELECT id, nome, email FROM usuarios";
        List<Usuario> lista = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email")
                ));
            }
        }
        return lista;
    }

    // Metodo para atualizar usuário
    public void atualizarUsuario(Usuario usuario) throws SQLException {
        String query = "UPDATE usuarios SET nome = ?, email = ?, senha = SHA2(?, 256) WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
        }
    }

    // Metodo  para deletar usuário
    public void deletarUsuario(int id) throws SQLException {
        String query = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}