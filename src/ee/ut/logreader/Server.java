package ee.ut.logreader;

//package Projekt.ProjektVer2;

//import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.List;

public class Server {

	private String username;
	private String password;
	private String host;
	private int port = 22; // default v�iks olla 22
	private Session session;
	private Channel channel;
	private InputStreamReader reader;
	private String Endofline;
	PrintWriter toChannel;
	InputStream inStream;

	public Server(String host, String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.host = host;
		// this.port = 22;
		this.setEndofline("ENDOPERATION");
	}

	Boolean SSHConnection() {
		try {

			JSch jsch = new JSch();
			session = jsch.getSession(username, host, 22);// v�iks alati t��tada
															// pordiga 22,
															// v�ibolla hiljem
															// vaja muuta
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no"); // - et ei viskaks
																// RSA key
																// fingerprint
																// errori
			session.connect();
			channel = session.openChannel("shell");// kutsub v�lja n�htamatud
													// shelli, nagu putty

			inStream = channel.getInputStream();

			OutputStream outStream = channel.getOutputStream();
			toChannel = new PrintWriter(new OutputStreamWriter(outStream), true);

			channel.connect();
			reader = new InputStreamReader(inStream);

			Thread.sleep(100);
			sendCommandAndReadOutput("cd /home/GrepToolPt");// et alustaks t��d
															// meie kaustast
			return true;

		} catch (JSchException | IOException | InterruptedException e) {
			//System.out.print(e.toString());
			return false;
		}
	}

	public List<String> sendCommandAndReadOutput(String command) {
		List<String> output = new ArrayList<String>();
		if (session != null && session.isConnected()) {
			try {
				toChannel.println(command + " ;echo " + Endofline); // peab
																	// lisama
																	// l�petava
																	// s�na,
																	// mida
																	// tagastab
																	// server
																	// t��
																	// l�petamisel,
																	// muidu
																	// l�heb
																	// lugemisel
																	// endless
																	// loopi
				Thread.sleep(300);
				output = readLines();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	private List<String> readLines() {
		List<String> output = new ArrayList<String>();
		try {

			BufferedReader reader1 = new BufferedReader(reader);
			String line;
			boolean nextline = true;
			boolean startsaving = false;

			while (nextline) {
				line = reader1.readLine();
				if (line.contains(Endofline) && !line.contains("echo"))// nii
																		// saame
																		// aru,
																		// et
																		// server
																		// on
																		// l�petanud
																		// vastamise
				{
					nextline = false;
					break;
				} else if (line.contains(Endofline))
					startsaving = true;// alustab sisuga arvestamist alastest
										// j�rgmisest reast peale k�sku, muidu
										// server v�ib �elda erinevaid tervitusi
										// ja muud meid mitte huvitavat infot
				else if (startsaving)
					output.add(line);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;

	}

	void Close() {
		channel.disconnect();
		session.disconnect();
		session = null;
		channel = null;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getEndofline() {
		return Endofline;
	}

	public void setEndofline(String endofline) {
		Endofline = endofline;
	}

	public PrintWriter getToChannel() {
		return toChannel;
	}

	public void setToChannel(PrintWriter toChannel) {
		this.toChannel = toChannel;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public InputStreamReader getReader() {
		return reader;
	}

	public void setReader(InputStreamReader reader) {
		this.reader = reader;
	}

}
