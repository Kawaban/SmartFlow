

import Controllers.ControllerDB;
import Controllers.ControllerIO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static spark.Spark.get;
import static spark.Spark.port;

public class Application {
    public static void main(String[] args) {
        port(8080);
        ControllerIO.activate(args,new ControllerDB());
     /*   System.out.println(LocalDate.parse("2022-02-02").toString());*/
        /*get("/project1/:projectId/something/:id", (req, res) -> {
            String projectId=(req.params("projectID"));
            projectId+=" : "+req.params("id");
            res.status(200);
            return projectId;
        });*/
    }
}
