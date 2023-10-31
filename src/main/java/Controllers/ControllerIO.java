package Controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static spark.Spark.*;

public class ControllerIO {
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
        post("/project",(request, response) -> {

            String stringJson= request.body();

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
            String stringJson= request.body();

            try {
                JSONObject jsonObject = new JSONObject(stringJson);
                controllerDB.createNewDeveloper(jsonObject);
                response.status(200);
                return "Created developerId: " + jsonObject.get("id");
            }catch (RuntimeException err){
                response.status(400);
                return "Error" + " " + err.toString();
            }
        });

        post("project/:projectId/assignment",(request, response) -> {
            String stringJson= request.body();
            long projectId= Long.parseLong(request.params("projectId"));
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
        put("/project/:projectId/task/:taskId", (request, response) ->{
            String stringJson= request.body();
            long taskId= Long.parseLong(request.params("taskId"));
            long projectId= Long.parseLong(request.params("projectId"));
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
