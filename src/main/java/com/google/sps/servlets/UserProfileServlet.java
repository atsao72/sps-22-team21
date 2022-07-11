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
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
@WebServlet("/profiles/*")
public class UserProfileServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String[] pathInfo = request.getPathInfo().split("/");
    String uid = pathInfo[1]; // {id}
    
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    // Retrieve posts from that user.
    Query<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("RunnerPost")
            .setOrderBy(StructuredQuery.OrderBy.desc("timestamp"))
            .setFilter(PropertyFilter.eq("UID", uid))
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


  private String convertStrToJson(ArrayList<RunnerPost> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }
}