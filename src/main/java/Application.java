

import Controllers.ControllerDB;
import Controllers.ControllerIO;

import static spark.Spark.get;

public class Application {
    public static void main(String[] args) {
   ///     ControllerIO.activate(args,new ControllerDB());
        get("/project/:projectId", (req, res) -> {
            String projectId=req.pathInfo().substring(req.pathInfo().lastIndexOf(":")+1);
            res.status(200);
            return projectId;
        });
    }
}
