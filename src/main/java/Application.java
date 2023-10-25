

import Controllers.ControllerDB;
import Controllers.ControllerIO;

import static spark.Spark.get;
import static spark.Spark.port;

public class Application {
    public static void main(String[] args) {
        port(8080);
        ControllerIO.activate(args,new ControllerDB());
        /*get("/project1/:projectId/something/:id", (req, res) -> {
            String projectId=(req.params("projectID"));
            projectId+=" : "+req.params("id");
            res.status(200);
            return projectId;
        });*/
    }
}
