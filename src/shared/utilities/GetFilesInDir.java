package shared.utilities;

import java.io.File;
import java.net.URISyntaxException;

public class GetFilesInDir {
	public static String[] gimme(String dir) {
		try {
			return new File(GetFilesInDir.class.getResource(dir).toURI()).list();
		} catch (URISyntaxException e) {
			System.out.println("Error loading directory for tile images");
			e.printStackTrace();
			return null;
		}
	}
}
