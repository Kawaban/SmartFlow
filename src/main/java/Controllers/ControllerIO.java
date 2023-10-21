package Controllers;

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
            long projectId= Long.parseLong(request.pathInfo().substring(request.pathInfo().lastIndexOf(":")+1));
            String obj=controllerDB.findProjectById(projectId);
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

        get("/project/:projectId/task/:taskId",(request, response) -> {
            long taskId= Long.parseLong(request.pathInfo().substring(request.pathInfo().lastIndexOf("/")+1));
            long projectId= Long.parseLong(request.pathInfo().substring(10,19));
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
                return "Created projectId: " + jsonObject.get("_id");
            }catch (JSONException err){
                response.status(400);
                return "Error" + " " + err.toString();
            }
        });

        post("/project/:projectId/task", (request, response) ->{
            String stringJson= request.body();
            long projectId= Long.parseLong(request.pathInfo().substring(request.pathInfo().lastIndexOf(":")+1,request.pathInfo().lastIndexOf("/")));
            try {
                JSONObject jsonObject = new JSONObject(stringJson);
                if(!controllerDB.createNewTask(jsonObject,projectId))
                {
                    response.status(404);
                    return "Error: Object was not found";
                }
                response.status(200);
                return "Created taskId: " + jsonObject.get("_id")+", added to a project: "+projectId;
            }catch (JSONException err){
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


    }

    public static void activatePuts(String[] args,ControllerDB controllerDB)
    {
        put("/project/:projectId/task/:taskId", (request, response) ->{
            String stringJson= request.body();
            long taskId= Long.parseLong(request.pathInfo().substring(request.pathInfo().lastIndexOf("/")+1));
            long projectId= Long.parseLong(request.pathInfo().substring(10,19));
            try {
                JSONObject jsonObject = new JSONObject(stringJson);
                if(!controllerDB.EditNewTask(projectId,taskId,jsonObject))
                {
                    response.status(404);
                    return "Error: Object was not found";
                }
                response.status(200);
                return "Created developerId: " + jsonObject.get("id");
            }catch (JSONException err){
                response.status(400);
                return "Error" + " " + err.toString();
            }
        });
    }
}
