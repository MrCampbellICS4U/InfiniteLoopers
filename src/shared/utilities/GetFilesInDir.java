package shared.utilities;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.file.spi.FileSystemProvider;
import java.nio.file.FileSystemNotFoundException;

public class GetFilesInDir {
	public static ArrayList<String> gimme(String dir) {
		ArrayList<String> files = new ArrayList<>();
		try {
			// getting a resource folder
			// https://stackoverflow.com/a/48298758
			// i have no clue how this works
			URI uri = GetFilesInDir.class.getResource(dir).toURI();

			if("jar".equals(uri.getScheme())){
			    for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
			        if (provider.getScheme().equalsIgnoreCase("jar")) {
			            try {
			                provider.getFileSystem(uri);
			            } catch (FileSystemNotFoundException e) {
			                // in this case we need to initialize it first:
			                provider.newFileSystem(uri, Collections.emptyMap());
			            }
			        }
			    }
			}

			// get all files in that folder
			// https://stackoverflow.com/a/47782558
			Path path = Paths.get(uri);
			// 1 = get all files 1 level deep
    		Files.walk(path, 1).forEach(p -> {
				String resPath = p.toString();
				resPath = resPath.substring(resPath.indexOf(dir) + dir.length());
				if (resPath.equals("")) return; // the first one added will be the directory name itself
				resPath = resPath.substring(1); // get rid of trailing /
				files.add(resPath);
			});
		} catch (Exception e) {
			System.out.println("Error loading tile images");
			e.printStackTrace();
		}
		return files;
	}
}
