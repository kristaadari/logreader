package ee.ut.logreader;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ServerCommands extends Server {

	public ServerCommands(String host, String username, String password) {
		super(host, username, password);
	}

	public List<String> getLogFiles() {
		List<String> logfailid = sendCommandAndReadOutput("find . -name '*.log'"); // küsib
																					// serverilt
																					// kõik
																					// log
																					// failid
		return logfailid;

	}

	public void readChosenFile(String file, TextArea output) {
			List<String> fileData = sendCommandAndReadOutput("more "
					+ file);
			String alllines = "";
			for (String line : fileData)
				alllines += line + "\r\n";
				output.setText(alllines);		
	}
	
	public void grepAllByKeyword(String keyword, TextArea output) {
		List<String> logfailid = getLogFiles();
		List<String> sisu = new ArrayList<String>();
		for (String file : logfailid)
			sisu.addAll(sendCommandAndReadOutput("grep '" + keyword + "' "
					+ file));
		
		if (sisu.size() == 0)
			output.setText("Midagi ei leitud");
		else
		{
		String alllines = "";
		for (String line : sisu)
			alllines += line + "\r\n";
			output.setText(alllines);
		}
		
	}

}
