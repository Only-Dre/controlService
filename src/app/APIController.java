package app;

import app.domains.appointment.AppointmentController;
import app.domains.patient.PatientController;
import app.domains.physiotherapist.PhysiotherapistController;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.after;
import static spark.Spark.port;

public class APIController {


    private static final String APLICATION_JSON = "application/json";

    public static void run() {
        port(1234);

        after(new Filter() {
            public void handle(Request request, Response response) throws Exception {
                response.type(APLICATION_JSON);
            }
        });

        PatientController.run();
        PhysiotherapistController.run();
        AppointmentController.run();
    }
}
