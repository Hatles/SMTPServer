package com;
import com.factory.SMTPCommunicationFactory;

import java.io.IOException;
import java.net.Socket;

public class AcceptClient extends ServerRunnable
{
	   private Socket socket;
	   private int nbrclient = 1;
	   
		public AcceptClient(Server s){
			super(s);
		}

		@Override
		protected void loop()
		{
			try {
	        		socket = this.getServer().getServerSocket().accept(); // Un client se connecte on l'accepte
	                //log("Le client numéro "+nbrclient+ " est connecté !");
	                nbrclient++;
	                
	                CommunicationRunnable client = new SMTPCommunication(this.server, socket, new SMTPCommunicationFactory());
	                server.addClient(client);
	                Thread t = new Thread(client);
	                t.setDaemon(true);
	    			t.start();
	        } catch (IOException e) {
				//e.printStackTrace();
				log(e.getMessage());
			}
		}

		@Override
		protected String getTag()
		{
			return "[SERVER]";
		}

	@Override
	public void close() {
        log("closing connection thread");
	}
}