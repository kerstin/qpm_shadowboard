package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/app")
public class CoreServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
//		PrintWriter out = response.getWriter();
//		Vectorizer myVectorizer = new Vectorizer();
//		out.println(myVectorizer.getmString());
		PrintPicture(response, request.getParameter("Pfad"));
		
	}
	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		out.println("Hello World");
	}
	
	public void PrintPicture(HttpServletResponse resp, String path) throws IOException 
	{
		ServletContext cntx= getServletContext();
		// Get the absolute path of the image
		String filename = cntx.getRealPath("Images/button.png");
		if((path != "") || (path != null))
			filename = cntx.getRealPath("Images/" + path);
		
		File file = new File(filename);
		if(!file.exists())
		{
			PrintWriter out = resp.getWriter();
			Vectorizer myVectorizer = new Vectorizer();
			out.println("Datei nicht gefunden");
			out.println("realPath: " + filename);
			return;
		}
		
		// retrieve mimeType dynamically
		String mime = cntx.getMimeType(filename);
		if (mime == null) 
		{
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	
		resp.setContentType(mime);
		resp.setContentLength((int)file.length());
	
		FileInputStream in = new FileInputStream(file);
		OutputStream out = resp.getOutputStream();
	
		// Copy the contents of the file to the output stream
		byte[] buf = new byte[1024];
		int count = 0;
		
		while ((count = in.read(buf)) >= 0) 
			out.write(buf, 0, count);
		
		out.close();
		in.close();
	}
}
