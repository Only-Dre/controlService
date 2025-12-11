package app.domains.physiotherapist;

import app.domains.physiotherapist.PhysiotherapistEntity
import app.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhysiotherapistDAO {
    public List<PhysiotherapistEntity> findAll() {

        List<PhysiotherapistEntity> ListPhysiotherapists = new ArrayList<>();

        String sql = "SELECT * FROM physiotherapist";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet result = preparedStatement.executeQuery();
        ) {
            while(result.next()) {
                PhysiotherapistEntity physiotherapist = new PhysiotherapistEntity(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getDouble("commission")
                );

                ListPhysiotherapists.add(physiotherapist);
            }
        } catch(SQLException e) {
            System.err.println("Erro ao buscar os fisioterapeutas: " + e.getMessage());
            e.printStackTrace();
        }

        return ListPhysiotherapists;
    };

    public PhysiotherapistEntity findById(Long id) throws SQLException {

        PhysiotherapistEntity physiotherapist = null;

        String sql = "SELECT * FROM physiotherapist WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setLong(1, id);

            try(ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    physiotherapist = new PhysiotherapistEntity(
                            result.getLong("id"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getString("password"),
                            result.getDouble("commission")
                    );
                }
            } catch(SQLException e) {
                System.out.println("Erro ao buscar o fisioterapeuta!. ID: " + id);
            }
        }

        return physiotherapist;
    };


    public void save(PhysiotherapistEntity physiotherapist) {

        String sql = "INSERT INTO physiotherapist (name, email, password, commission) VALUES (?, ?, ?, ?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, physiotherapist.getName());
            preparedStatement.setString(2, physiotherapist.getEmail());
            preparedStatement.setString(3, physiotherapist.getPassword());
            preparedStatement.setDouble(4, physiotherapist.getCommission());


            preparedStatement.executeUpdate();

            try(ResultSet result = preparedStatement.getGeneratedKeys()) {
                if (result.next()) {
                    physiotherapist.setId(result.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir o fisioterapeuta" + physiotherapist.getName());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };

    public void update(PhysiotherapistEntity physiotherapist) {
        String sql = "UPDATE physiotherapist SET name = ?, phone = ?, email = ?, password = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setString(1, physiotherapist.getName());
            preparedStatement.setString(2, physiotherapist.getEmail());
            preparedStatement.setString(3, physiotherapist.getPassword());
            preparedStatement.setDouble(4, physiotherapist.getCommission());

            int affectedlines = preparedStatement.executeUpdate();

            System.out.println("PhysiotherapistEntity ID: " + physiotherapist.getId() + " Atualizado");
            System.out.println("Linhas afetadas: " + affectedlines);

        } catch(SQLException e) {
            System.out.println("Erro ao atualizar a physiotherapist: " + physiotherapist.getName());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };


    public void delete(Long id) {
        String sql = "DELETE FROM physiotherapist WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            int affectedlines = stmt.executeUpdate();

            System.out.println("Fisioterapeuta Excluído.");
            System.out.println("Linhas afetadas: " + affectedlines);

        } catch(SQLException e) {
            System.out.println("Erro ao excluir fisioterapeuta ID: " + id);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
