package server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import marvin.MarvinDefinitions;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.GenericDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;


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
		
		System.out.println("Starting with batik");
		TestBatik();
		
	}
	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		mCntx = getServletContext();
		PrintWriter out = response.getWriter();
		out.println("Hello World");
	}
	
	public void InsertPictureIntoSVG(Graphics2D g2d) 
	{
		//g2d.setPaint(Color.red);
		//g2d.fill(new Rectangle(10, 10, 100, 100));
		
		System.out.println("appending realpath1");
		String RealPath;
		//RealPath = BuildRealPath("tooltest.edited.jpg");	
		System.out.println("Loading edited image");
		RealPath = "C:/test.jpg";
		File file = new File(RealPath);
		
		if(file.exists())
			System.out.println("file gibts.");
		else
			System.out.println("file gibts ned");
				
		Image img;
		try
		{
			img = ImageIO.read(new File(RealPath));
			g2d.drawImage(img, 0, 0, null);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("drwan image to g2d");
	}

	public void TestBatik() throws IOException 
	{

		// Get a DOMImplementation.
		DOMImplementation domImpl =
		GenericDOMImplementation.getDOMImplementation();
	
		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);
	
		// Create an instance of the SVG Generator.
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
	
		// Ask the test to render into the SVG Graphics2D implementation.
		CoreServlet test = new CoreServlet();
		test.InsertPictureIntoSVG(svgGenerator);
	
		// Finally, stream out SVG to the standard output using
		// UTF-8 encoding.
		boolean useCSS = true; // we want to use CSS style attributes
		
		String TempRealPath = BuildRealPath("Temp.svg");
		File yourFile = new File(TempRealPath);
		if(!yourFile.exists()) 
		{
		    yourFile.createNewFile();
		} 
		FileOutputStream oFile = new FileOutputStream(yourFile, false); 
		
		Writer out = new OutputStreamWriter(oFile, "UTF-8");
		
		svgGenerator.stream(out, useCSS);
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
