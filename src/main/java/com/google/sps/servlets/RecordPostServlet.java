package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Handles requests sent to the /hello URL. Try running a server and navigating to /hello! */
@WebServlet("/record")
public class RecordPostServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username = request.getParameter("uname");
    String distance = request.getParameter("distance");
    String time = request.getParameter("time");
    String avgBPM = request.getParameter("avgBPM");
    String description = request.getParameter("description");
    // log data into cloud log
    System.out.println("User="+username + " Distance=" + distance + " time="+time+" avgBPM="+avgBPM+ " description=" + description);
  }
}