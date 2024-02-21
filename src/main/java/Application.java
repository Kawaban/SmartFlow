

import Controllers.ControllerDB;
import Controllers.ControllerIO;


import static spark.Spark.port;
public class Application {
    public static void main(String[] args) {
        port(8080);
        ControllerIO.activate(args, new ControllerDB());

    }
}
