

import Controllers.ControllerDB;
import Controllers.ControllerIO;

import static spark.Spark.get;
import static spark.Spark.port;

public class Application {
    public static void main(String[] args) {
        port(8080);
        ControllerIO.activate(args,new ControllerDB());
        /*get("/project1/:projectId", (req, res) -> {
            String projectId=req.pathInfo().substring(req.pathInfo().lastIndexOf("/")+1);
            res.status(200);
            return projectId;
        });*/
    }
}
