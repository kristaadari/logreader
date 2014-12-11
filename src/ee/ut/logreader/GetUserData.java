package ee.ut.logreader;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetUserData {
	static File dir = new File("tmp/test");
	static File IPfile = new File(dir, "IP.txt");;


	static List<String> existingData() {
		List<String> lines = new ArrayList<>();

		try {
			Path path = Paths.get("tmp/test");
			   if (Files.notExists(path))
			    dir.mkdirs();
			   // teeb kausta, kui seda pole

			   if (!IPfile.exists())
			    IPfile.createNewFile();
			   // teeb faili, kui seda pole
		
				// loeb faili sisu kui see juba olemas
				lines = Files.readAllLines(IPfile.toPath());
							
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	static void saveData(String[] data) {
		try {
			
			
			Path path = Paths.get("tmp/test");
			if (Files.notExists(path))
				dir.mkdirs();
			// teeb kausta, kui seda pole

			if (!IPfile.exists())
				IPfile.createNewFile();
			// teeb faili, kui seda pole
			
			List<String> lines = Files.readAllLines(IPfile.toPath());
			if (lines.indexOf(data[0]) != -1)
			{		
				//Alati ülesalvestab kui server oli juba salvestatud (nt. kui vaja parooli/kasutajast) muuta
				lines.set(lines.indexOf(data[0]) + 1, data[1]);
				lines.set(lines.indexOf(data[0]) + 2, data[2]);
				
			}
			else
			{
			lines.add("---server");
			for (String row : data)
				lines.add(row);
			}
			Files.write(IPfile.toPath(), lines, StandardCharsets.UTF_8);
			System.out.println("Andmed edukalt salvestatud");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
// algselt vajalik IP aadressi kontrollimiseks. hiljem deprecated
	static boolean isValidIP4Address(String ipAddress) {
		return true;
	}

//kontrollimaks, kas andmeid sisestati
	static boolean isValidInput(String input) {
		if (input.length() == 0) {
			return false;
		} else
			return true;
	}

}
