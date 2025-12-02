package app.appointment.domain;

import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    /* READ ALL*/
    public List<Appointment> buscarTodos() throws SQLException {

        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT * FROM appointment";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();) {

            while(rs.next()) {


                Appointment appointment = new Appointment(
                        rs.getLong("id"),
                        rs.getLong("patient_id"),
                        rs.getLong("physiotherapist_id"),
                        rs.getString("procedure"),
                        rs.getDate("appointment"),
                        rs.getTime("start_hour"),
                        rs.getTime("end_hour"),
                        rs.getTime("duration"),
                        rs.getDouble("procedure_value")
                );

                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar appointments: " + e.getMessage());
            e.printStackTrace();
        }

        return appointments;
    };

    /*READ BY ID*/
    public Appointment buscarPorId(Long id) throws SQLException {

        Appointment appointment = null;

        String sql = "SELECT * FROM appointments WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    appointment = new Appointment(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getInt("estoque")
                    );
                }
            } catch(SQLException e) {
                System.out.println("Erro ao buscar appointment. ID: " + id);
            }
        }

        return appointment;
    };

    /*CREATE*/
    public void inserir(Appointment appointment) {
        String sql = "INSERT INTO appointments (nome, preco, estoque) VALUES (?,?,?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, appointment.getNome());
            stmt.setDouble(2,appointment.getPreco());
            stmt.setInt(3, appointment.getEstoque());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    appointment.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inseriro o appointment" + appointment.getNome());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };

    /*UPDATE*/
    public void atualizar(Appointment appointment) {
        String sql = "UPDATE appointments SET nome = ?, preco = ?, estoque = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, appointment.getNome());
            stmt.setDouble(2, appointment.getPreco());
            stmt.setInt(3, appointment.getEstoque());

            stmt.setLong(4, appointment.getId());

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("Appointment ID: " + appointment.getId() + " Atualizado");
            System.out.println("Linhas afetadas: " + linhasAfetadas);

        } catch(SQLException e) {
            System.out.println("Erro ao atualizar o appointment: " + appointment.getNome());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };


    /*DELETE*/
    public void deletar(Long id) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("Appointment Excluído.");
            System.out.println("Linhas afetadas: " + linhasAfetadas);

        } catch(SQLException e) {
            System.out.println("Erro ao excluir appointment ID: " + id);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
