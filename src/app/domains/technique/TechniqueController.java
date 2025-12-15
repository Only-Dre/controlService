package app.domains.technique;

import app.domains.technique.TechniqueEntity;
import app.domains.technique.TechniqueDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

import static spark.Spark.*;
import static spark.Spark.delete;

public class TechniqueController {
    private static final TechniqueDAO thecniqueDAO = new TechniqueDAO();
    private static final Gson gson = new GsonBuilder().create();

    public static void run() {


        get("/technique", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return gson.toJson(thecniqueDAO.findAll());
            }
        });


        get("/technique/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    Long id = Long.parseLong(request.params(":id"));

                    TechniqueEntity produto = thecniqueDAO.findById(id);

                    if (produto != null) {
                        return gson.toJson(thecniqueDAO.findById(id));
                    } else {
                        response.status(404);
                        return "{\"message\": \"TechniqueEntity com ID " + id + " não encontrado.\"}";
                    }
                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"message\": \"Formato do id inválido.\"}";
                }
            }
        });

        /*POST*/
        post("/technique", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    TechniqueEntity newPatient = gson.fromJson(request.body(), TechniqueEntity.class);

                    thecniqueDAO.save(newPatient);

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

        // PUT /technique/:id - Atualizar produto existente
        put("/technique/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (thecniqueDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"TechniqueEntity não encontrado para atualização.\"}";
                    }

                    TechniqueEntity produtoParaAtualizar = gson.fromJson(request.body(), TechniqueEntity.class);
                    produtoParaAtualizar.setId(id); // garante que o ID da URL seja usado

                    thecniqueDAO.update(produtoParaAtualizar);

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

        // DELETE /technique/:id - Deletar um produto
        delete("/technique/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (thecniqueDAO.findById(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"TechniqueEntity não encontrado para exclusão.\"}";
                    }

                    thecniqueDAO.delete(id); // Usa o Long ID

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
