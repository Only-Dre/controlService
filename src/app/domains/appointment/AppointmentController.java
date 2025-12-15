package app.domains.appointment;

import app.domains.appointment.dtos.AppointmentDTO;
import app.domains.physiotherapist.PhysiotherapistDAO;
import app.domains.physiotherapist.dtos.PhysiotherapistStatisticsDTO;
import com.google.gson.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static spark.Spark.*;
import static spark.Spark.delete;

public class AppointmentController {
    
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static final PhysiotherapistDAO physiotherapistDAO = new PhysiotherapistDAO();

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                    LocalDateTime.parse(json.getAsString()))
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (dateTime, type, context) ->
                    new JsonPrimitive(dateTime.toString()))
            .create();

    public static void run() {


        get("/appointment", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return gson.toJson(appointmentDAO.findAll());
            }
        });


        get("/appointment/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    Long id = Long.parseLong(request.params(":id"));

                    AppointmentEntity produto = appointmentDAO.findById(id);

                    if (produto != null) {
                        return gson.toJson(appointmentDAO.findById(id));
                    } else {
                        response.status(404);
                        return "{\"message\": \"AppointmentEntity com ID " + id + " não encontrado.\"}";
                    }
                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"message\": \"Formato do id inválido.\"}";
                }
            }
        });


        post("/appointment", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    AppointmentDTO appointmentDTO = gson.fromJson(request.body(), AppointmentDTO.class);


                    AppointmentEntity appointmentEntity = appointmentDAO.save(appointmentDTO);

                    response.status(201);

                    return gson.toJson(appointmentEntity);
                } catch (IllegalArgumentException e) {
                    response.status(400);
                    e.printStackTrace();
                    return
                        "{" +
                        "\"message\": \"Erro ao criar atendimento.\"," +
                        "\"erro\": \"" + e.getMessage() + "\"" +
                        "}"
                    ;
                } catch (Exception e) {
                    response.status(500);
                    System.out.println("Erro ao processar a requisição POST");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return "{\"message\": \"Erro ao criar atendimento.\"}";
                }
            }
        });


        put("/appointment/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    // Pega o ID da URL
                    Long appointmentId = Long.parseLong(request.params(":id"));

                    // Converte o JSON para DTO (igual ao POST)
                    AppointmentDTO appointmentDTO = gson.fromJson(request.body(), AppointmentDTO.class);

                    // Atualiza usando o DAO (passando o ID e o DTO)
                    AppointmentEntity appointmentEntity = appointmentDAO.update(appointmentId, appointmentDTO);

                    response.status(200);

                    return gson.toJson(appointmentEntity);
                } catch (IllegalArgumentException e) {
                    response.status(400);
                    e.printStackTrace();
                    return
                        "{" +
                        "\"message\": \"Erro ao criar atendimento.\"," +
                        "\"erro\": \"" + e.getMessage() + "\"" +
                        "}"
                        ;
                } catch (Exception e) {
                    response.status(500);
                    System.out.println("Erro ao processar a requisição PUT");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return "{\"message\": \"Erro ao atualizar appointment.\"}";
                }
            }
        });


        delete("/appointment/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (appointmentDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"AppointmentEntity não encontrado para exclusão.\"}";
                    }

                    appointmentDAO.delete(id); // Usa o Long ID

                    response.status(204); // No Content
                    return ""; // Corpo vazio

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\": \"Formato de ID inválido.\"}";
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        get("/appointment/statistics/physiotherapist/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    Long physiotherapistId = Long.parseLong(request.params(":id"));

                    PhysiotherapistStatisticsDTO statistics = physiotherapistDAO.getStatisticsByPhysiotherapist(physiotherapistId);

                    response.status(200);
                    return gson.toJson(statistics);

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"message\": \"Formato de ID inválido.\"}";
                } catch (IllegalArgumentException e) {
                    response.status(404);
                    return
                            "{" +
                            "\"message\": \"Erro ao buscar estatísticas.\"," +
                            "\"erro\": \"" + e.getMessage() + "\"" +
                            "}"
                            ;
                } catch (SQLException e) {
                    response.status(500);
                    e.printStackTrace();
                    return "{\"message\": \"Erro ao buscar estatísticas no banco de dados.\"}";
                } catch (Exception e) {
                    response.status(500);
                    e.printStackTrace();
                    return "{\"message\": \"Erro ao processar requisição.\"}";
                }
            }
        });
    }
}
