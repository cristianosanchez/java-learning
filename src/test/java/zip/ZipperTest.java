package zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import zip.ZipEvent;
import zip.Zipper;

/**
 * a.txt | +-- bbb/ | | | +-- b.txt | +-- ccc/ | +-- c.txt | +-- ddd/ | +--
 * d.txt
 * 
 * To test replacement when unzipping files: The content of zipper_test.zip
 * a.txt file is "version 1 of a file" The content of zipper_test2.zip a.txt
 * file is "version 2 of a file"
 * 
 */
public class ZipperTest implements Observer {

  File outputDir;
  File zipFile, zipFile2;
  Zipper zipper = new Zipper();

  @Before
  public void initOutputDir() {
    String tempDir = System.getProperty("java.io.tmpdir");
    outputDir = new File(tempDir);
    zipFile = new File("./zipper_test.zip");
    zipFile2 = new File("./zipper_test2.zip");
    zipper.addObserver(this);
  }

  @Test
  public void extractAll() throws IOException {
    int count = zipper.extractFiles(zipFile, outputDir);
    assertEquals(4, count);
  }

  @Test
  public void extractOneFile() throws IOException {
    File extracted = zipper.extractFileByName("a.txt", zipFile, outputDir);
    assertTrue(extracted.exists());
  }

  @Test
  public void extractOneFileInsideSubdir() throws IOException {
    File extracted = zipper.extractFileByName("bbb/b.txt", zipFile, outputDir);
    assertTrue(extracted.exists());
  }

  @Test
  public void extractReplacingFiles() throws IOException {
    int count = 0;

    count = zipper.extractFiles(zipFile, outputDir);
    assertEquals(4, count);
    assertContentOfFile(outputDir, "a.txt", "version 1 of a file");

    count = zipper.extractFiles(zipFile2, outputDir);
    assertEquals(4, count);
    assertContentOfFile(outputDir, "a.txt", "version 2 of a file");
  }

  private void assertContentOfFile(File dir, String fileName, String content)
      throws FileNotFoundException, IOException {
    File aFile = new File(dir, fileName);
    assertTrue(aFile.exists());
    BufferedReader input = new BufferedReader(new FileReader(aFile));
    assertEquals(content, input.readLine());
    input.close();
  }

  @Override
  public void update(Observable o, Object event) {
    ZipEvent e = (ZipEvent) event;
  }
}
