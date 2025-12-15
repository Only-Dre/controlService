package app.domains.patient;

import app.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientDAO {

    public List<PatientEntity> findAll() {

        List<PatientEntity> listPatients = new ArrayList<>();

        String sql = "SELECT * FROM patient";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet result = preparedStatement.executeQuery();
        ) {
            while(result.next()) {
                PatientEntity patient = new PatientEntity(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("phone")
                );

                listPatients.add(patient);
            }
        } catch(SQLException e) {
            System.err.println("Erro ao buscar os pacientes: " + e.getMessage());
            e.printStackTrace();
        }

        return listPatients;
    };

    public PatientEntity findById(Long id) throws SQLException {

        PatientEntity patient = null;

        String sql = "SELECT * FROM patient WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setLong(1, id);

            try(ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    patient = new PatientEntity(
                            result.getLong("id"),
                            result.getString("name"),
                            result.getString("phone")
                    );
                }
            } catch(SQLException e) {
                System.out.println("Erro ao buscar o paciente!. ID: " + id);
            }
        }

        return patient;
    };


    public void save(PatientEntity patient) throws SQLException {


        String nome = patient.getName().trim();

        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }

        Pattern patternName = Pattern.compile("\\d");
        Matcher matcherName = patternName.matcher(nome);
        if (matcherName.find()) {
            throw new IllegalArgumentException("Nome não pode conter números");
        }

        Pattern patternTelefone = Pattern.compile("^\\d{11}$");
        Matcher matcherTelefone = patternTelefone.matcher(patient.getPhone());

        if (!matcherTelefone.matches()) {
            throw new IllegalArgumentException("Telefone deve conter 11 dígitos numéricos (DDD + número)");
        }

        String sql = "INSERT INTO patient (name, phone) VALUES (?, ?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, patient.getName().toLowerCase());
            preparedStatement.setString(2, patient.getPhone());

            preparedStatement.executeUpdate();

            try(ResultSet result = preparedStatement.getGeneratedKeys()) {
                if (result.next()) {
                    patient.setId(result.getLong(1));
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    };

    public void update(PatientEntity patient) {
        String sql = "UPDATE patient SET nome = ?, phone = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setString(1, patient.getName().toLowerCase());
            preparedStatement.setString(2, patient.getPhone());
            preparedStatement.setLong(3, patient.getId());

            int affectedlines = preparedStatement.executeUpdate();

            System.out.println("PatientEntity ID: " + patient.getId() + " Atualizado");
            System.out.println("Linhas afetadas: " + affectedlines);

        } catch(SQLException e) {
            System.out.println("Erro ao atualizar a patient: " + patient.getName());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    };


    public void delete(Long id) {
        String sql = "DELETE FROM patient WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setLong(1, id);

            int affectedlines = stmt.executeUpdate();

            System.out.println("Paciente Excluída.");
            System.out.println("Linhas afetadas: " + affectedlines);

        } catch(SQLException e) {
            System.out.println("Erro ao excluir patient ID: " + id);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
