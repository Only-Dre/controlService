/*
package app.domains.appointment;

import app.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    */
/* READ ALL*//*

    public List<AppointmentEntity> buscarTodos() throws SQLException {

        List<AppointmentEntity> appointmentEntities = new ArrayList<>();

        String sql = "SELECT * FROM appointment";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();) {

            while(rs.next()) {


                AppointmentEntity appointmentEntity = new AppointmentEntity(
                        rs.getLong("id"),
                        rs.getLong("patient_id"),
                        rs.getLong("physiotherapist_id"),
                        rs.getString("procedure"),
                        rs.getDate("appointmentEntity"),
                        rs.getTime("start_hour"),
                        rs.getTime("end_hour"),
                        rs.getTime("duration"),
                        rs.getDouble("procedure_value")
                );

                appointmentEntities.add(appointmentEntity);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar appointmentEntities: " + e.getMessage());
            e.printStackTrace();
        }

        return appointmentEntities;
    };

    */
/*READ BY ID*//*

    public AppointmentEntity buscarPorId(Long id) throws SQLException {

        AppointmentEntity appointmentEntity = null;

        String sql = "SELECT * FROM appointments WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    appointmentEntity = new AppointmentEntity(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getInt("estoque")
                    );
                }
            } catch(SQLException e) {
                System.out.println("Erro ao buscar appointmentEntity. ID: " + id);
            }
        }

        return appointmentEntity;
    };

    */
/*CREATE*//*

    public void inserir(AppointmentEntity appointmentEntity) {
        String sql = "INSERT INTO appointments (nome, preco, estoque) VALUES (?,?,?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, appointmentEntity.getNome());
            stmt.setDouble(2, appointmentEntity.getPreco());
            stmt.setInt(3, appointmentEntity.getEstoque());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    appointmentEntity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inseriro o appointmentEntity" + appointmentEntity.getNome());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };

    */
/*UPDATE*//*

    public void atualizar(AppointmentEntity appointmentEntity) {
        String sql = "UPDATE appointments SET nome = ?, preco = ?, estoque = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, appointmentEntity.getNome());
            stmt.setDouble(2, appointmentEntity.getPreco());
            stmt.setInt(3, appointmentEntity.getEstoque());

            stmt.setLong(4, appointmentEntity.getId());

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("AppointmentEntity ID: " + appointmentEntity.getId() + " Atualizado");
            System.out.println("Linhas afetadas: " + linhasAfetadas);

        } catch(SQLException e) {
            System.out.println("Erro ao atualizar o appointmentEntity: " + appointmentEntity.getNome());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };


    */
/*DELETE*//*

    public void deletar(Long id) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("AppointmentEntity Excluído.");
            System.out.println("Linhas afetadas: " + linhasAfetadas);

        } catch(SQLException e) {
            System.out.println("Erro ao excluir appointment ID: " + id);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
*/
