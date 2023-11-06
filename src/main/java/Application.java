

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

    }
}
