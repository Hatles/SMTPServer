//package com;
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ConnClient extends ServerRunnable
//{
//	private static int count = 0;
//
//	private Socket socket;
//
//	private BufferedReader in;
//	private DataOutputStream out;
//
//	private int id;
//
//	public ConnClient(Server server, Socket socket)
//	{
//		super(server);
//		this.socket = socket;
//		id = count++;
//	}
//
//	@Override
//	protected void loop()
//	{
//		try
//		{
//			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			out = new DataOutputStream(socket.getOutputStream());
//
//			String line;
//			List<String> lines = new ArrayList<String>();
//			String message = "";
//
//			while((line = in.readLine())!=null && line.length() > 0)
//			{
//				message += line + "\n";
//				lines.add(line);
//			}
//
//			if(lines.size() > 0)
//				applyRequest(lines);
//
//		} catch (IOException e)
//		{
//			//e.printStackTrace();
//			log(e.getMessage());
//		}
//
//		close();
//	}
//
//	private void applyRequest(List<String> lines)
//	{
//		if(lines.size() == 0)
//		{
//			sendServerError();
//			return;
//		}
//
//		Method method = Utils.getMethod(lines.get(0));
//
//		switch(method)
//		{
//			case GET:
//				applyGet(lines);
//				break;
//			/*case HEAD:
//
//				break;*/
//			default:
//				this.sendMethodError();
//				break;
//		}
//	}
//
//	private void applyGet(List<String> lines)
//	{
//		if(!Utils.verifHTTP(new String[]{"1.0", "1.1"}, lines.get(0)))
//		{
//			this.sendHTTPError();
//			return;
//		}
//
//		log("");
//		log("Request : ");
//		for(int i  = 0; i < lines.size(); i++)
//			log(lines.get(i));
//		try
//		{
//			File file = Utils.getFile(server.getRootDir(), lines.get(0));
//
//			if(Utils.isForbiddenFile(file.getAbsolutePath()))
//			{
//				this.sendForbiddenError();
//				return;
//			}
//
//			if(file.exists())
//			{
//				log("Write status line");
//				writeLine("HTTP/1.0 200 OK");
//				log("Start log headers");
//				writeLine("Date: "+Utils.getDate());
//				writeLine("Server: "+this.server.getServerName());
//				writeLine("Content-Length: " + (file.length()));
//				writeLine("Content-Type: " + Utils.getContentType(file));
//				writeLine("Set-Cookie: clientID=123513486; path=/ ;");
//				writeLine("Connection: close");
//				writeLine("");
//				log("End log headers");
//				writeData(file);
//				out.flush();
//				log("Flush");
//			}
//			else
//			{
//				this.sendNotFoundError(file);
//			}
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private void writeData(File file) throws IOException
//	{
//		log("Start log data");
//		FileInputStream fea;
//		try
//		{
//			fea = new FileInputStream(file);
//			int content;
//			while((content = fea.read()) != -1)
//			{
//				out.writeByte(content);
//			}
//			fea.close();
//			//out.writeByte(-1);
//		} catch (FileNotFoundException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		log("End log data");
//	}
//
//	private void writeLine(String message) throws IOException
//	{
//		out.writeBytes(message+"\r\n");//CR LF
//	}
//
//	private void close()
//	{
//		log("close client");
//		try
//		{
//			socket.close();
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		server.removeClient(this);
//		this.stop();
//	}
//
//	public void sendNotFoundError(File f)
//	{
//		log("File not found : " + f.getAbsolutePath());
//
//		sendError(404, "Not Found", "notfound.html");
//	}
//
//	public void sendForbiddenError()
//	{
//		sendError(403, "Forbidden");
//	}
//
//	public void sendMethodError()
//	{
//		sendError(405, "Method Not Allowed");
//	}
//
//	public void sendServerError()
//	{
//		this.sendError(500, "Internal Server Error");
//	}
//
//	public void sendHTTPError()
//	{
//		this.sendError(505, "HTTP Version not supported");
//	}
//
//	public void sendError(int code, String title, String path)
//	{
//		File file = new File("src/page/"+path);
//
//		try
//		{
//			writeLine("HTTP/1.0 "+code+" "+title);
//			writeLine("Date: "+Utils.getDate());
//			writeLine("Server: "+this.server.getServerName());
//			writeLine("Connection: close");
//			writeLine("Content-Length: " + (file.length()));
//			writeLine("Content-Type: " + Utils.getContentType(file));
//			writeLine("");
//			writeData(file);
//			out.flush();
//			log("Erreur : "+code+" "+title);
//		} catch (IOException e)
//		{
//			log(e.getMessage());
//			e.printStackTrace();
//		}
//
//	}
//
//	public void sendError(int code, String title)
//	{
//		try
//		{
//			String page = Utils.getPageError(code, title);
//			writeLine("HTTP/1.0 "+code+" "+title);
//			writeLine("Date: "+Utils.getDate());
//			writeLine("Server: "+this.server.getServerName());
//			writeLine("Connection: close");
//			writeLine("Content-Length: " + (page.getBytes().length));
//			writeLine("Content-Type: " + "text/html");
//			writeLine("");
//			writeLine(Utils.getPageError(code, title));
//			out.flush();
//			log("Erreur : "+code+" "+title);
//		} catch (IOException e)
//		{
//			log(e.getMessage());
//			e.printStackTrace();
//		}
//
//	}
//
//
//	public int getID()
//	{
//		return id;
//	}
//
//	@Override
//	protected String getTag()
//	{
//		return "["+id+"]";
//	}
//}
