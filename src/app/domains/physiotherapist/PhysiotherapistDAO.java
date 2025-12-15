package app.domains.physiotherapist;

import app.domains.physiotherapist.dtos.PhysiotherapistStatisticsDTO;
import app.domains.physiotherapist.enums.TechniquesENUM;
import app.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                        TechniquesENUM.valueOf(result.getString("technique").toUpperCase()),
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
                            TechniquesENUM.valueOf(result.getString("technique")),
                            result.getDouble("commission")
                    );
                }
            } catch(SQLException e) {
                System.out.println("Erro ao buscar o fisioterapeuta!. ID: " + id);
            }
        }

        return physiotherapist;
    };


    public void save(PhysiotherapistEntity physiotherapist) throws SQLException {

        String nome = physiotherapist.getName().trim();
        String password = physiotherapist.getPassword().trim();

        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }

        if (password.length() < 3) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 3 caracteres");
        }

        Pattern patternName = Pattern.compile("\\d");
        Matcher matcherName = patternName.matcher(nome);
        if (matcherName.find()) {
            throw new IllegalArgumentException("Nome não pode conter números");
        }

        String sql = "INSERT INTO physiotherapist (name, email, password, technique, commission) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, physiotherapist.getName());
            preparedStatement.setString(2, physiotherapist.getEmail());
            preparedStatement.setString(3, physiotherapist.getPassword());
            preparedStatement.setString(4, physiotherapist.getTechnique().name());
            preparedStatement.setDouble(5, physiotherapist.getCommission());

            preparedStatement.executeUpdate();

            try(ResultSet result = preparedStatement.getGeneratedKeys()) {
                if (result.next()) {
                    physiotherapist.setId(result.getLong(1));
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    };

    public void update(PhysiotherapistEntity physiotherapist) throws SQLException {

        String nome = physiotherapist.getName().trim();
        String password = physiotherapist.getPassword().trim();

        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }

        if (password.length() < 3) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 3 caracteres");
        }

        Pattern patternName = Pattern.compile("\\d");
        Matcher matcherName = patternName.matcher(nome);
        if (matcherName.find()) {
            throw new IllegalArgumentException("Nome não pode conter números");
        }

        String sql = "UPDATE physiotherapist SET name = ?, email = ?, password = ?, technique = ?, commission = ? WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);) {

            preparedStatement.setString(1, physiotherapist.getName());
            preparedStatement.setString(2, physiotherapist.getEmail());
            preparedStatement.setString(3, physiotherapist.getPassword());
            preparedStatement.setString(4, physiotherapist.getTechnique().name());
            preparedStatement.setDouble(5, physiotherapist.getCommission());
            preparedStatement.setLong(6, physiotherapist.getId());

            int affectedlines = preparedStatement.executeUpdate();

            System.out.println("PhysiotherapistEntity ID: " + physiotherapist.getId() + " Atualizado");
            System.out.println("Linhas afetadas: " + affectedlines);

        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }


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

    public PhysiotherapistStatisticsDTO getStatisticsByPhysiotherapist(Long physiotherapistId) throws SQLException {

        // Verifica se o fisioterapeuta existe
        PhysiotherapistEntity physiotherapist = findById(physiotherapistId);

        if (physiotherapist == null) {
            throw new IllegalArgumentException("Fisioterapeuta não encontrado com ID: " + physiotherapistId);
        }

        PhysiotherapistStatisticsDTO statistics = null;

        // Query que agrega os dados dos atendimentos
        String sql = "SELECT COUNT(*) as total_appointments, " +
                "COALESCE(SUM(value), 0) as total_value, " +
                "COALESCE(SUM(net_value), 0) as total_net_value " +
                "FROM appointment WHERE physiotherapist_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setLong(1, physiotherapistId);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    statistics = new PhysiotherapistStatisticsDTO(
                            physiotherapistId,
                            physiotherapist.getName(),
                            result.getInt("total_appointments"),
                            result.getDouble("total_value"),
                            result.getDouble("total_net_value")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar estatísticas do fisioterapeuta ID: " + physiotherapistId);
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }

        return statistics;
    }

}
