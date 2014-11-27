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

import marvin.MarvinDefinitions;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;


@WebServlet("/app")
public class CoreServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private MarvinImage		image, backupImage;
	private MarvinImagePlugin     imagePlugin;

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
		String filename = cntx.getRealPath("Images/button_.png");
		if((path != "") || (path != null))
			filename = cntx.getRealPath("Images/" + path);
		
		File file = new File(filename);
		if(!file.exists())
		{
			PrintWriter out = resp.getWriter();
			//Vectorizer myVectorizer = new Vectorizer();
			out.println("Datei nicht gefunden");
			out.println("realPath: " + filename);
			return;
		}
		
		MarvinDefinitions.setImagePluginPath("D:/Dropbox/FH/ITP3/Shadowboard/marvin/plugins/image/");
		image = MarvinImageIO.loadImage(filename);
		backupImage = image.clone();        
		imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.edge.edgeDetector.jar");
		imagePlugin.process(image, image);
		imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.invert.jar");
		imagePlugin.process(image, image);
        image.update();
        
        String newFilename = filename;
        int Index = newFilename.lastIndexOf('.');
        String nameOfFile = newFilename.substring(0, Index);
        String endingOfFile = newFilename.substring(Index, newFilename.length());
        newFilename = nameOfFile + ".edited" + endingOfFile;
        
        MarvinImageIO.saveImage(image, newFilename);
        System.out.println("Saved file as: " + newFilename);
		
		// retrieve mimeType dynamically
		String mime = cntx.getMimeType(newFilename);
		if (mime == null) 
		{
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	
		File NewFile = new File(newFilename);
		resp.setContentType(mime);
		resp.setContentLength((int)NewFile.length());
	
		FileInputStream in = new FileInputStream(NewFile);
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
