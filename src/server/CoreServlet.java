package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/app")
public class CoreServlet extends HttpServlet {
	
@Override
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    out.println("Hello World");
  }

@Override
public void doPost(HttpServletRequest request,
                  HttpServletResponse response)
    throws ServletException, IOException {
  PrintWriter out = response.getWriter();
  out.println("Hello World");
}

}