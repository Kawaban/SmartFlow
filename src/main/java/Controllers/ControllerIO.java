package Controllers;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import dev.harrel.jsonschema.Validator;
import dev.harrel.jsonschema.ValidatorFactory;
import dev.harrel.jsonschema.providers.OrgJsonNode;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.util.Scanner;

import static spark.Spark.*;

public class ControllerIO {

    private final static Validator validator = new ValidatorFactory().withJsonNodeFactory(new OrgJsonNode.Factory()).createValidator();
    public static void activate(String[] args,ControllerDB controllerDB)
    {
        activateGets(args,controllerDB);
        activatePosts(args,controllerDB);
        activatePuts(args,controllerDB);
    }

    public static void activateGets(String[] args,ControllerDB controllerDB)
    {
        get("/project/:projectId", (request, response) -> {
            long projectId = Long.parseLong(request.params("projectId"));
            String obj;
            try {
                obj = controllerDB.findProjectById(projectId);
            } catch (RuntimeException err) {
                response.status(400);
                return "Error" + " " + err.toString();
            }
            if (obj != null) {
                response.status(200);
                return obj;
            } else {
                response.status(404);
                return "Error: Object was not found";
            }
        });

        get("/project/:projectId/task/:taskId",(request, response) -> {
            long taskId= Long.parseLong(request.params("taskId"));
            long projectId= Long.parseLong(request.params("projectId"));
            String obj=controllerDB.findTaskById(projectId ,taskId);
            if(obj!=null) {
                response.status(200);
                return obj;
            }
            else
            {
                response.status(404);
                return "Error: Object was not found";
            }
        });

      /*  get("/project/user",(req,res) -> {
            res.status(200);
            return req.pathInfo();
        });*/
    }

    public static void activatePosts(String[] args,ControllerDB controllerDB)
    {
        URI postDeveloperSchema;
        URI postProjectSchema;
        URI postTaskSchema;
        URI postAssignmentSchema;
        try {
            FileInputStream fis1 = new FileInputStream("src/main/resources/JSONschemas/postdeveloper.json");
            String data1 = IOUtils.toString(fis1, "UTF-8");
            postDeveloperSchema = validator.registerSchema(data1);

            FileInputStream fis2 = new FileInputStream("src/main/resources/JSONschemas/postproject.json");
            String data2 = IOUtils.toString(fis2, "UTF-8");
             postProjectSchema = validator.registerSchema(data2);

            FileInputStream fis3 = new FileInputStream("src/main/resources/JSONschemas/posttask.json");
            String data3 = IOUtils.toString(fis3, "UTF-8");
            postTaskSchema = validator.registerSchema(data3);

            FileInputStream fis4 = new FileInputStream("src/main/resources/JSONschemas/postassignment.json");
            String data4 = IOUtils.toString(fis4, "UTF-8");
            postAssignmentSchema = validator.registerSchema(data4);
        }catch (IOException err)
        {
            throw new RuntimeException(err.toString());
        }


        post("/project",(request, response) -> {

            String stringJson= request.body();

            Validator.Result result = validator.validate(postProjectSchema, stringJson);
            if (!result.isValid()) {
                response.status(400);
                return "Error: wrong input parameters: " + result.toString();
            }

            try {
                JSONObject jsonObject = new JSONObject(stringJson);
                controllerDB.createNewProject(jsonObject);
                response.status(200);
                return "Created projectId: " + jsonObject.get("id");
            }catch (RuntimeException err){
                response.status(400);
                return "Error" + " " + err.toString();
            }
        });

        post("/project/:projectId/task", (request, response) ->{
            String stringJson= request.body();
            long projectId= Long.parseLong(request.params("projectId"));

            Validator.Result result = validator.validate(postTaskSchema, stringJson);
            if (!result.isValid()) {
                response.status(400);
                return "Error: wrong input parameters: " + result.toString();
            }

            try {
                JSONObject jsonObject = new JSONObject(stringJson);
                controllerDB.createNewTask(jsonObject,projectId);
                response.status(200);
                return "Created taskId: " + jsonObject.get("id")+", added to a project: "+projectId;
            }catch (RuntimeException err){
                response.status(400);
                return "Error" + " " + err.toString();
            }

        });

        post("/user", (request, response) -> {
            String stringJson = request.body();

            Validator.Result result = validator.validate(postDeveloperSchema, stringJson);
            if (!result.isValid()) {
                response.status(400);
                return "Error: wrong input parameters: " + result.toString();
            }

            try {
                JSONObject jsonObject = new JSONObject(stringJson);
                controllerDB.createNewDeveloper(jsonObject);
                response.status(200);
                return "Created developerId: " + jsonObject.get("id");
            } catch (RuntimeException err) {
                response.status(400);
                return "Error" + " " + err.toString();
            }
        });

        post("project/:projectId/assignment",(request, response) -> {
            String stringJson= request.body();
            long projectId= Long.parseLong(request.params("projectId"));

            Validator.Result result = validator.validate(postAssignmentSchema, stringJson);
            if (!result.isValid()) {
                response.status(400);
                return "Error: wrong input parameters: " + result.toString();
            }

            try {
                JSONObject jsonObject = new JSONObject(stringJson);
                JSONArray array=controllerDB.delegateTasks(projectId,jsonObject);
                response.status(200);
                return array.toString();
            }
            catch (RuntimeException err){
                response.status(400);
                return "Error" + " " + err.toString();
            }
        });
    }

    public static void activatePuts(String[] args,ControllerDB controllerDB)
    {
        URI putTaskSchema;
        URI putAssignmentSchema;
        try {
            FileInputStream fis1 = new FileInputStream("src/main/resources/JSONschemas/puttask.json");
            String data1 = IOUtils.toString(fis1, "UTF-8");
            putTaskSchema = validator.registerSchema(data1);

            FileInputStream fis2 = new FileInputStream("src/main/resources/JSONschemas/putassignment.json");
            String data2 = IOUtils.toString(fis2, "UTF-8");
            putAssignmentSchema = validator.registerSchema(data2);

        }catch (IOException err)
        {
            throw new RuntimeException(err.toString());
        }




        put("/project/:projectId/task/:taskId", (request, response) ->{
            String stringJson= request.body();
            long taskId= Long.parseLong(request.params("taskId"));
            long projectId= Long.parseLong(request.params("projectId"));

            Validator.Result result = validator.validate(putTaskSchema, stringJson);
            if (!result.isValid()) {
                response.status(400);
                return "Error: wrong input parameters: " + result.toString();
            }

            try {
                JSONObject jsonObject = new JSONObject(stringJson);
                controllerDB.EditNewTask(projectId,taskId,jsonObject);
                response.status(200);
                return "Status updated";
            }catch (RuntimeException err){
                response.status(400);
                return "Error" + " " + err.toString();
            }
        });

        put("project/:projectId/assignment/:assignmentId",(request, response) -> {
            String stringJson= request.body();
            long projectId= Long.parseLong(request.params("projectId"));
            long assignmentId= Long.parseLong(request.params("assignmentId"));

            Validator.Result result = validator.validate(putAssignmentSchema, stringJson);
            if (!result.isValid()) {
                response.status(400);
                return "Error: wrong input parameters: " + result.toString();
            }

            try{
                JSONObject jsonObject = new JSONObject(stringJson);
                controllerDB.decideDelegationOfTasks(assignmentId,projectId,jsonObject);
                response.status(200);
                return "Assignment is complete";
            } catch (RuntimeException err){
                response.status(400);
                return "Error" + " " + err.toString();
            }
        });
    }
}
