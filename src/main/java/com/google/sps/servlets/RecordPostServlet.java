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
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.*;
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
      entity.getLong("avgBPM"), entity.getString("description"), entity.getTimestamp("timestamp"),entity.getString("UID")));
    }

    String json = convertStrToJson(posts);
    response.setContentType("application/json;");
    response.getWriter().println(json);

  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // String username = request.getParameter("uname");
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            .setAudience(Collections.singletonList("126364799474-iuabk1c066p90m76da18q2fc2jjkikij.apps.googleusercontent.com"))
            .build();
        
    // (Receive idTokenString by HTTPS POST)
    GoogleIdToken idToken = null;
    String idTokenString = request.getParameter("token");
    try {
        idToken = verifier.verify(idTokenString);
    } catch (Exception e) {
        
    }
    
    if (idToken != null) {
        Payload payload = idToken.getPayload();

        // Print user identifier
        String userId = payload.getSubject();
        System.out.println("User ID: " + userId);
        String username = (String) payload.get("name");
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
                .set("UID", userId)
                .set("username", username)
                .set("distance", distance)
                .set("time", time)
                .set("avgBPM",avgBPM)
                .set("description",description)
                .set("timestamp", timestamp)
                .build();
        datastore.put(post);
    } else {
        System.out.println("Invalid ID token.");
    }

    // redirect to the main page
    response.sendRedirect("https://summer22-sps-21.appspot.com/");
  }

  private String convertStrToJson(ArrayList<RunnerPost> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }
}