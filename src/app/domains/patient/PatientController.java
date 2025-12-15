package app.domains.patient;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class PatientController {

    private static final PatientDAO patientDAO = new PatientDAO();
    private static final Gson gson = new Gson();

    public static void run() {


        get("/patient", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return gson.toJson(patientDAO.findAll());
            }
        });


        get("/patient/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    Long id = Long.parseLong(request.params(":id"));

                    PatientEntity produto = patientDAO.findById(id);

                    if (produto != null) {
                        return gson.toJson(patientDAO.findById(id));
                    } else {
                        response.status(404);
                        return "{\"message\": \"PatientEntity com ID " + id + " não encontrado.\"}";
                    }
                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"message\": \"Formato do id inválido.\"}";
                }
            }
        });


        post("/patient", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    PatientEntity newPatient = gson.fromJson(request.body(), PatientEntity.class);

                    patientDAO.save(newPatient);

                    response.status(201);

                    return gson.toJson(newPatient);

                } catch (NullPointerException e) {
                    response.status(400);
                    e.printStackTrace();
                    return
                        "{" +
                        "\"message\": \"Erro ao criar Fisioterapeuta.\"," +
                        "\"erro\": \"" + e.getMessage() + "\"" +
                        "}"
                    ;
                } catch (SQLIntegrityConstraintViolationException e) {
                    response.status(409);
                    e.printStackTrace();
                    return
                        "{" +
                        "\"message\": \"Erro ao criar paciente.\"," +
                        "\"erro\": \"" + e.getMessage() + "\"" +
                        "}"
                    ;
                } catch (IllegalArgumentException e) {
                    response.status(400);
                    e.printStackTrace();
                    return
                        "{" +
                        "\"message\": \"Erro ao criar paciente.\"," +
                        "\"erro\": \"" + e.getMessage() + "\"" +
                        "}"
                    ;
                } catch (SQLException e) {
                    response.status(500);
                    e.printStackTrace();
                    return "{\"message\": \"Erro geral no banco de dados..\"}";
                } catch (Exception e) {
                    response.status(500);
                    e.printStackTrace();
                    return "{\"message\": \"Falha no servidor!.\"}";
                }

            }

        });


        put("/patient/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (patientDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"PatientEntity não encontrado para atualização.\"}";
                    }

                    PatientEntity produtoParaAtualizar = gson.fromJson(request.body(), PatientEntity.class);
                    produtoParaAtualizar.setId(id); // garante que o ID da URL seja usado

                    patientDAO.update(produtoParaAtualizar);

                    response.status(200); // OK
                    return gson.toJson(produtoParaAtualizar);

                } catch (NumberFormatException e) {
                    response.status(400); // Bad Request
                    return "{\"mensagem\": \"Formato de ID inválido.\"}";
                } catch (Exception e) {
                    response.status(500);
                    System.err.println("Erro ao processar requisição PUT: " + e.getMessage());
                    e.printStackTrace();
                    return "{\"mensagem\": \"Erro ao atualizar produto.\"}";
                }
            }
        });


        delete("/patient/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (patientDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"PatientEntity não encontrado para exclusão.\"}";
                    }

                    patientDAO.delete(id); // Usa o Long ID

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
    }
}
