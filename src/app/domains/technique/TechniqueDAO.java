package app.domains.technique;

import app.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechniqueDAO {
    
    public List<TechniqueEntity> findAll() {

        List<TechniqueEntity> listTechniques = new ArrayList<>();

        String sql = "SELECT * FROM technique";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet result = preparedStatement.executeQuery();
        ) {
            while(result.next()) {
                TechniqueEntity technique = new TechniqueEntity(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getDouble("value")
                );

                listTechniques.add(technique);
            }
        } catch(SQLException e) {
            System.err.println("Erro ao buscar as técnicas: " + e.getMessage());
            e.printStackTrace();
        }

        return listTechniques;
    };

    public TechniqueEntity findById(Long id) throws SQLException {

        TechniqueEntity technique = null;

        String sql = "SELECT * FROM technique WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setLong(1, id);

            try(ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    technique = new TechniqueEntity(
                            result.getLong("id"),
                            result.getString("name"),
                            result.getDouble("value")
                    );
                }
            } catch(SQLException e) {
                System.out.println("Erro ao buscar técnica!. ID: " + id);
            }
        }

        return technique;
    };


    public void save(TechniqueEntity technique) {

        String sql = "INSERT INTO technique (name, value) VALUES (?, ?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, technique.getName());
            preparedStatement.setDouble(2, technique.getValue());

            preparedStatement.executeUpdate();

            try(ResultSet result = preparedStatement.getGeneratedKeys()) {
                if (result.next()) {
                    technique.setId(result.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir a técnica" + technique.getName());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };

    public void update(TechniqueEntity technique) {
        String sql = "UPDATE technique SET nome = ?, value = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setString(1, technique.getName());
            preparedStatement.setDouble(2, technique.getValue());
            preparedStatement.setLong(3, technique.getId());

            int affectedlines = preparedStatement.executeUpdate();

            System.out.println("Técnica ID: " + technique.getId() + " Atualizado");
            System.out.println("Linhas afetadas: " + affectedlines);

        } catch(SQLException e) {
            System.out.println("Erro ao atualizar a técnica: " + technique.getName());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };


    public void delete(Long id) {
        String sql = "DELETE FROM technique WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            int affectedlines = stmt.executeUpdate();

            System.out.println("Técnica Excluída.");
            System.out.println("Linhas afetadas: " + affectedlines);

        } catch(SQLException e) {
            System.out.println("Erro ao excluir a técnica ID: " + id);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
