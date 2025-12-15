package app.domains.appointment;

import app.domains.appointment.dtos.AppointmentDTO;
import app.domains.patient.PatientDAO;
import app.domains.patient.PatientEntity;
import app.domains.physiotherapist.PhysiotherapistDAO;
import app.domains.physiotherapist.PhysiotherapistEntity;
import app.domains.technique.TechniqueDAO;
import app.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    PatientDAO patientDAO = new PatientDAO();
    PhysiotherapistDAO physiotherapistDAO = new PhysiotherapistDAO();
    TechniqueDAO techniqueDAO = new TechniqueDAO();

    public List<AppointmentEntity> findAll() throws SQLException {

        List<AppointmentEntity> appointmentEntities = new ArrayList<>();

        String sqlAppointment = "SELECT * FROM appointment";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlAppointment);
            ResultSet result = preparedStatement.executeQuery();) {

            while(result.next()) {

                PatientEntity patient = patientDAO.findById(result.getLong("patient_id"));
                PhysiotherapistEntity physiotherapist = physiotherapistDAO.findById(result.getLong("physiotherapist_id"));

                AppointmentEntity appointmentEntity = new AppointmentEntity(
                        result.getLong("id"),
                        patient,
                        physiotherapist,
                        physiotherapist.getTechnique(),
                        result.getObject("date", LocalDateTime.class),
                        result.getInt("duration")
                );

                appointmentEntities.add(appointmentEntity);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar os atendimentos: " + e.getMessage());
            e.printStackTrace();
        }

        return appointmentEntities;
    };



    public AppointmentEntity findById(Long id) throws SQLException {

        AppointmentEntity appointmentEntity = null;

        String sql = "SELECT * FROM appointment WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setLong(1, id);

            try(ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {

                    PatientEntity patient = patientDAO.findById(result.getLong("patient_id"));
                    PhysiotherapistEntity physiotherapist = physiotherapistDAO.findById(result.getLong("physiotherapist_id"));

                    appointmentEntity = new AppointmentEntity(
                            result.getLong("id"),
                            patient,
                            physiotherapist,
                            physiotherapist.getTechnique(),
                            result.getObject("date", LocalDateTime.class),
                            result.getInt("duration")
                    );
                }
            } catch(SQLException e) {
                System.out.println("Erro ao buscar atendimento. ID: " + id);
            }
        }

        return appointmentEntity;
    };



    public AppointmentEntity save(AppointmentDTO appointmentDTO) throws SQLException {

        // Validação da data
        if (appointmentDTO.date() == null) {
            throw new IllegalArgumentException("Data do agendamento é obrigatória");
        }

        if (appointmentDTO.date().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data do agendamento não pode estar no passado");
        }

        PatientEntity patient = patientDAO.findById(appointmentDTO.patientId());
        PhysiotherapistEntity physiotherapist = physiotherapistDAO.findById(appointmentDTO.physiotherapistId());

        AppointmentEntity appointmentEntity = new AppointmentEntity(patient, physiotherapist, physiotherapist.getTechnique(), appointmentDTO.date(), appointmentDTO.duration());

        String sql = "INSERT INTO appointment (patient_id, physiotherapist_id, technique, date, duration, value, net_value) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setLong(1, appointmentEntity.getPatient().getId());
            preparedStatement.setLong(2, appointmentEntity.getPhysiotherapist().getId());
            preparedStatement.setString(3, appointmentEntity.getPhysiotherapist().getTechnique().name());
            preparedStatement.setObject(4, appointmentEntity.getDate());
            preparedStatement.setInt(5, appointmentEntity.getDuration());
            preparedStatement.setDouble(7, appointmentEntity.getNetValue());
            preparedStatement.setDouble(6, appointmentEntity.getValue());


            preparedStatement.executeUpdate();

            try(ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    appointmentEntity.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }

        return appointmentEntity;
    };



    public AppointmentEntity update(Long appointementId ,AppointmentDTO appointmentDTO) throws SQLException {

        if (appointmentDTO.date() == null) {
            throw new IllegalArgumentException("Data do agendamento é obrigatória");
        }

        if (appointmentDTO.date().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data do agendamento não pode estar no passado");
        }

        PatientEntity patient = patientDAO.findById(appointmentDTO.patientId());
        PhysiotherapistEntity physiotherapist = physiotherapistDAO.findById(appointmentDTO.physiotherapistId());

        AppointmentEntity appointmentEntity = new AppointmentEntity(patient, physiotherapist, physiotherapist.getTechnique(), appointmentDTO.date(), appointmentDTO.duration());
        String sql = "UPDATE appointment SET patient_id = ?, physiotherapist_id = ?, technique = ?, date = ?, duration = ?, value = ?, net_value = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setLong(1, appointmentEntity.getPatient().getId());
            preparedStatement.setLong(2, appointmentEntity.getPhysiotherapist().getId());
            preparedStatement.setString(3, appointmentEntity.getPhysiotherapist().getTechnique().name());
            preparedStatement.setObject(4, appointmentEntity.getDate());
            preparedStatement.setInt(5, appointmentEntity.getDuration());
            preparedStatement.setDouble(6, appointmentEntity.getValue());
            preparedStatement.setDouble(7, appointmentEntity.getNetValue());
            preparedStatement.setLong(8, appointementId);

            int linhasAfetadas = preparedStatement.executeUpdate();

            System.out.println("AppointmentEntity ID: " + appointmentEntity.getId() + " Atualizado");
            System.out.println("Linhas afetadas: " + linhasAfetadas);

        } catch(SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }

        return appointmentEntity;
    };




    public void delete(Long id) {
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
