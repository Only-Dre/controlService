package app.domains.physiotherapist;

import com.google.gson.Gson;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

import static spark.Spark.*;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class PhysiotherapistController {

    private static final PhysiotherapistDAO patientDAO = new PhysiotherapistDAO();
    private static final Gson gson = new Gson();

    private static final String APLICATION_JSON = "application/json";
    
    public static void run() {
        port(1234);

        after(new Filter() {
            public void handle(Request request, Response response) throws Exception {
                response.type(APLICATION_JSON);
            }
        });


        /*PATIENT*/
        /*GET ALL*/
        get("/physiotherapist", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return gson.toJson(patientDAO.findAll());
            }
        });

        /*GET BY ID*/
        get("/physiotherapist/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    Long id = Long.parseLong(request.params(":id"));

                    PhysiotherapistEntity produto = patientDAO.findById(id);

                    if (produto != null) {
                        return gson.toJson(patientDAO.findById(id));
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

        /*POST*/
        post("/physiotherapist", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    PhysiotherapistEntity newPatient = gson.fromJson(request.body(), PhysiotherapistEntity.class);

                    patientDAO.save(newPatient);

                    response.status(201);

                    return gson.toJson(newPatient);
                } catch (Exception e) {
                    response.status(500);
                    System.out.println("Erro ao processar a requisição POST");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return "{\"message\": \"Erro ao criar produto.\"}";
                }
            }
        });

        // PUT /physiotherapist/:id - Atualizar produto existente
        put("/physiotherapist/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (patientDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"PhysiotherapistEntity não encontrado para atualização.\"}";
                    }

                    PhysiotherapistEntity produtoParaAtualizar = gson.fromJson(request.body(), PhysiotherapistEntity.class);
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

        // DELETE /physiotherapist/:id - Deletar um produto
        delete("/physiotherapist/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (patientDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"PhysiotherapistEntity não encontrado para exclusão.\"}";
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
