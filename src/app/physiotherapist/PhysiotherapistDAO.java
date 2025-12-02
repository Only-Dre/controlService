package app.physiotherapist;

import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhysiotherapistDAO {
    /* READ ALL*/
    public List<PhysiotherapistEntity> buscarTodos() throws SQLException {

        List<PhysiotherapistEntity> physiotherapists = new ArrayList<>();

        String sql = "SELECT * FROM physiotherapist";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();) {

            while(rs.next()) {


                PhysiotherapistEntity physiotherapist = new PhysiotherapistEntity(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("procedures"),
                        rs.getDouble("commissions")
                );

                physiotherapists.add(physiotherapist);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar physiotherapists: " + e.getMessage());
            e.printStackTrace();
        }

        return physiotherapists;
    };

    /*READ BY ID*/
    public physiotherapistEntity buscarPorId(Long id) throws SQLException {

        physiotherapistEntity physiotherapist = null;

        String sql = "SELECT * FROM physiotherapists WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    physiotherapist = new physiotherapistEntity(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getInt("estoque")
                    );
                }
            } catch(SQLException e) {
                System.out.println("Erro ao buscar physiotherapist. ID: " + id);
            }
        }

        return physiotherapist;
    };

    /*CREATE*/
    public void inserir(physiotherapistEntity physiotherapist) {
        String sql = "INSERT INTO physiotherapists (nome, preco, estoque) VALUES (?,?,?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, physiotherapist.getNome());
            stmt.setDouble(2,physiotherapist.getPreco());
            stmt.setInt(3, physiotherapist.getEstoque());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    physiotherapist.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inseriro o physiotherapist" + physiotherapist.getNome());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };

    /*UPDATE*/
    public void atualizar(physiotherapistEntity physiotherapist) {
        String sql = "UPDATE physiotherapists SET nome = ?, preco = ?, estoque = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, physiotherapist.getNome());
            stmt.setDouble(2, physiotherapist.getPreco());
            stmt.setInt(3, physiotherapist.getEstoque());

            stmt.setLong(4, physiotherapist.getId());

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("physiotherapistEntity ID: " + physiotherapist.getId() + " Atualizado");
            System.out.println("Linhas afetadas: " + linhasAfetadas);

        } catch(SQLException e) {
            System.out.println("Erro ao atualizar o physiotherapist: " + physiotherapist.getNome());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };


    /*DELETE*/
    public void deletar(Long id) {
        String sql = "DELETE FROM physiotherapists WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("physiotherapistEntity Excluído.");
            System.out.println("Linhas afetadas: " + linhasAfetadas);

        } catch(SQLException e) {
            System.out.println("Erro ao excluir physiotherapist ID: " + id);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
