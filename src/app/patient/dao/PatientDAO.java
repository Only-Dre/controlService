package app.patient.dao;

// Imports importantes dependendo do pacote
import app.patient.domain.Patient;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    /* Leitura ALL */
    public List<Patient> buscarTodos() {

        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patient";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                );
                patients.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pacientes: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }

    /* Leitura por ID do paciente */
    public Patient buscarPorId(Long id) {

        Patient patient = null;

        String sql = "SELECT * FROM patient WHERE id = ?"; // Seleciona todos de "patient" por id

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    patient = new Patient(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("phone")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar paciente por ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return patient;
    }

    /* Criação */
    public void inserir(Patient patient) {

        String sql = "INSERT INTO patient (name, phone) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getPhone());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    patient.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir paciente: " + patient.getName());
            e.printStackTrace();
        }
    }

    /* Atualização de dados */
    public void atualizar(Patient patient) {

        String sql = "UPDATE patient SET name = ?, phone = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getPhone());
            stmt.setLong(3, patient.getId());

            int linhas = stmt.executeUpdate();

            System.out.println("Paciente atualizado. Linhas afetadas: " + linhas);

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente ID " + patient.getId());
            e.printStackTrace();
        }
    }

    /* Remoção */
    public void deletar(Long id) {

        String sql = "DELETE FROM patient WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int linhas = stmt.executeUpdate();

            System.out.println("Paciente deletado. Linhas afetadas: " + linhas);

        } catch (SQLException e) {
            System.err.println("Erro ao deletar paciente ID " + id);
            e.printStackTrace();
        }
    }
}
