package com.google.sps.servlets;

import com.google.sps.data.RunnerPost;
import java.util.ArrayList;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.IncompleteKey;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
/** Handles requests sent to the /hello URL. Try running a server and navigating to /hello! */
@WebServlet("/record")
public class RecordPostServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    // Retrieve the last 100 posts from the datastore, ordered by timestamp.
    Query<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("RunnerPost")
            .setOrderBy(StructuredQuery.OrderBy.desc("timestamp"))
            .setLimit(100)
            .build();
    QueryResults<Entity> results = datastore.run(query);

    ArrayList<RunnerPost> posts = new ArrayList<>();
    while (results.hasNext()) {
    Entity entity = results.next();
      posts.add(new RunnerPost(entity.getString("username"), entity.getDouble("distance"),entity.getDouble("time"), 
      entity.getLong("avgBPM"), entity.getString("description"), entity.getTimestamp("timestamp")));
    }

    String json = convertStrToJson(posts);
    response.setContentType("application/json;");
    response.getWriter().println(json);

  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username = request.getParameter("uname");
    double distance = Float.valueOf(request.getParameter("distance"));
    double time = Float.valueOf(request.getParameter("time"));
    long avgBPM = Integer.valueOf(request.getParameter("avgBPM"));
    String description = request.getParameter("description");
    Timestamp timestamp = Timestamp.now();

    // store a message to the database.
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("RunnerPost");
    FullEntity<IncompleteKey> post =
        Entity.newBuilder(keyFactory.newKey())
            .set("username", username)
            .set("distance", distance)
            .set("time", time)
            .set("avgBPM",avgBPM)
            .set("description",description)
            .set("timestamp", timestamp)
            .build();
    datastore.put(post);
    // redirect to the main page
    response.sendRedirect("https://summer22-sps-21.appspot.com/");
  }

  private String convertStrToJson(ArrayList<RunnerPost> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }
}