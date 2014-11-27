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
	private ServletContext mCntx;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		mCntx = getServletContext();
//		PrintWriter out = response.getWriter();
//		Vectorizer myVectorizer = new Vectorizer();
//		out.println(myVectorizer.getmString());
		String pathEditedPicture = CreateEdgePicture(BuildRealPath(request.getParameter("Pfad")));
		PrintPicture(response, pathEditedPicture);
		
	}
	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		mCntx = getServletContext();
		PrintWriter out = response.getWriter();
		out.println("Hello World");
	}
	
	public String BuildRealPath(String filename)
	{	
		String RealPath;	
		
		if((filename != "") || (filename != null))
			RealPath = mCntx.getRealPath("Images/" + filename);
		else
			RealPath = mCntx.getRealPath("Images/button_.png");
		
		return RealPath;		
	}
	
	public String CreateEdgePicture(String path)	//returns the path of the edited picture
	{
		MarvinImage	image;
		MarvinImagePlugin imagePlugin;
		
		MarvinDefinitions.setImagePluginPath("D:/Dropbox/FH/ITP3/Shadowboard/marvin/plugins/image/");
		image = MarvinImageIO.loadImage(path);      
		imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.edge.edgeDetector.jar");
		imagePlugin.process(image, image);
		imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.invert.jar");
		imagePlugin.process(image, image);
        image.update();
        
        String newFilename = path;
        int Index = newFilename.lastIndexOf('.');
        String nameOfFile = newFilename.substring(0, Index);
        String endingOfFile = newFilename.substring(Index, newFilename.length());
        newFilename = nameOfFile + ".edited" + endingOfFile;
        
        MarvinImageIO.saveImage(image, newFilename);
        System.out.println("Saved file as: " + newFilename);
        
        return newFilename;
	}
	
	public void PrintPicture(HttpServletResponse resp, String path) throws IOException 
	{		
		File file = new File(path);
		if(!file.exists())
		{
			PrintWriter out = resp.getWriter();
			//Vectorizer myVectorizer = new Vectorizer();
			out.println("Datei nicht gefunden");
			out.println("realPath: " + path);
			return;
		}
				
		// retrieve mimeType dynamically
		String mime = mCntx.getMimeType(path);
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
