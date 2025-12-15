package app.domains.physiotherapist;

import app.domains.physiotherapist.dtos.PhysiotherapistStatisticsDTO;
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

public class PhysiotherapistController {

    private static final PhysiotherapistDAO physiotherapistDAO = new PhysiotherapistDAO();
    private static final Gson gson = new Gson();

    public static void run() {


        get("/physiotherapist", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return gson.toJson(physiotherapistDAO.findAll());
            }
        });


        get("/physiotherapist/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    Long id = Long.parseLong(request.params(":id"));

                    PhysiotherapistEntity produto = physiotherapistDAO.findById(id);

                    if (produto != null) {
                        return gson.toJson(physiotherapistDAO.findById(id));
                    } else {
                        response.status(404);
                        return "{\"message\": \"PhysiotherapistEntity com ID " + id + " não encontrado.\"}";
                    }
                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"message\": \"Formato do id inválido.\"}";
                }
            }
        });


        post("/physiotherapist", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    PhysiotherapistEntity newPhysiotherapist = gson.fromJson(request.body(), PhysiotherapistEntity.class);

                    System.out.println(newPhysiotherapist);

                    physiotherapistDAO.save(newPhysiotherapist);

                    response.status(201);

                    return gson.toJson(newPhysiotherapist);

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

        put("/physiotherapist/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id"));

                    if (physiotherapistDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"message\": \"Fisioterapeuta não encontrado para atualização.\"}";
                    }

                    PhysiotherapistEntity physiotherapistParaAtualizar = gson.fromJson(request.body(), PhysiotherapistEntity.class);
                    physiotherapistParaAtualizar.setId(id);

                    physiotherapistDAO.update(physiotherapistParaAtualizar);

                    response.status(200);
                    return gson.toJson(physiotherapistParaAtualizar);

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"message\": \"Formato de ID inválido.\"}";
                } catch (NullPointerException e) {
                    response.status(400);
                    e.printStackTrace();
                    return
                            "{" +
                                    "\"message\": \"Erro ao atualizar Fisioterapeuta.\"," +
                                    "\"erro\": \"" + e.getMessage() + "\"" +
                                    "}"
                            ;
                } catch (SQLIntegrityConstraintViolationException e) {
                    response.status(409);
                    e.printStackTrace();
                    return
                            "{" +
                                    "\"message\": \"Erro ao atualizar fisioterapeuta.\"," +
                                    "\"erro\": \"" + e.getMessage() + "\"" +
                                    "}"
                            ;
                } catch (IllegalArgumentException e) {
                    response.status(400);
                    e.printStackTrace();
                    return
                            "{" +
                                    "\"message\": \"Erro ao atualizar fisioterapeuta.\"," +
                                    "\"erro\": \"" + e.getMessage() + "\"" +
                                    "}"
                            ;
                } catch (SQLException e) {
                    response.status(500);
                    e.printStackTrace();
                    return "{\"message\": \"Erro geral no banco de dados.\"}";
                } catch (Exception e) {
                    response.status(500);
                    e.printStackTrace();
                    return "{\"message\": \"Falha no servidor!\"}";
                }
            }
        });


        delete("/physiotherapist/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (physiotherapistDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"PhysiotherapistEntity não encontrado para exclusão.\"}";
                    }

                    physiotherapistDAO.delete(id); // Usa o Long ID

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
