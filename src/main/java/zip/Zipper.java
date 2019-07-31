package zip;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Observable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper extends Observable {

    private static final int BUFFER = 2048;

    /**
     * Compact file list
     * 
     * @param zipFile
     * @return zipped file
     */
    public File compactaArquivos(File zipFile, Collection<File> fileCollection) throws IOException {
        if(!zipFile.exists()) {
        	if (zipFile.getParent() != null) {
        		zipFile.getParentFile().mkdirs();
    		}
        	zipFile.createNewFile();
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        try {
            byte[] buffer = new byte[BUFFER];
            for (File file : fileCollection) {
                FileInputStream input = new FileInputStream(file);
                ZipEntry zipentry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipentry);
                int len;
                while ((len = input.read(buffer, 0, BUFFER)) != -1) {
                    zipOutputStream.write(buffer, 0, len);
                }
                zipOutputStream.closeEntry();
                input.close();
                this.setChanged();
                this.notifyObservers(new ZipEvent(file, "compressed"));
            }
        } finally {
            try {
                if (zipOutputStream !=null)
                    zipOutputStream.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
        return zipFile;
    }
    
    /**
     * Extract zipFile to outputDir
     * 
     * @param zipFile
     * @param outputDir the output directory must exist prior to extract files into
     * @return total extracted files
     */
    public int extractFiles(File zipFile, File outputDir) throws IOException {
        int zipEntryCounter = 0;
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry zipentry = null;
            while ((zipentry = zin.getNextEntry()) != null) {
                if (zipentry.isDirectory())
            		continue;
                zipEntryCounter++;
                File file = new File(outputDir, zipentry.getName());
                writeFile(file, zin);
                this.setChanged();
                this.notifyObservers(new ZipEvent(file, "extracted"));
            }
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
            if (zipEntryCounter == 0) {
                throw new IOException("zip file empty");
            }
        }
        return zipEntryCounter;
    }
    
    /**
     * Search by pathname and extract file
     * 
     * @filename
     * @zipFile
     * @outputDir
     * 
     * @param outputDir the output directory must exist prior to extract files into
     */
    public File extractFileByName(String filename, File zipFile, File outputDir) throws FileNotFoundException, IOException {
        File file = null;
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry zipentry = null;
            while ((zipentry = zin.getNextEntry()) != null) {
                if (zipentry.getName().equals(filename)) {
                	file = new File(outputDir, zipentry.getName());
                	writeFile(file, zin);
                	this.setChanged();
                	this.notifyObservers(new ZipEvent(file, "extracted"));
                    break;
                } else {
                	continue;
                }
            }
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file == null)
                throw new FileNotFoundException("zip entry not found: " + filename);
            
        }
        return file;
    }

	private void writeFile(File output, ZipInputStream zin) throws IOException {
		if (output.getParent() != null) {
			output.getParentFile().mkdirs();
		}
		BufferedOutputStream dest = null;
		try {
			dest = new BufferedOutputStream(new FileOutputStream(output), BUFFER);
			byte[] data = new byte[BUFFER];
			int count;
			while ((count = zin.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
		} finally {
			dest.flush();
			dest.close();
		}
	}
}
