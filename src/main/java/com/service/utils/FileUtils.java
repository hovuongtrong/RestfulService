package com.service.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;

public class FileUtils {

	public static MediaType getMediaType(String mineType) {
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
	public static String readFileToString(InputStream instream) {
		Scanner scan = new Scanner( instream);
    	Scanner s =scan.useDelimiter("\\A");
    	String result = s.hasNext() ? s.next() : "";
    	scan.close();
    	return result;
	}
	public static File copyInputStreamToFile(InputStream inputStream, String filePath) {
		File f = new File(filePath);
    	OutputStream output;
		try {
			output = new FileOutputStream(f);
			IOUtils.copy(inputStream, output);
	    	output.close();
		} catch (IOException e) {
		}
    	return f;
	}
	public static boolean addFileToZip(File fZip, File f) {
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fZip));
			ZipEntry e = new ZipEntry(f.getName());
			out.putNextEntry(e);
	
			byte[] data = Files.readAllBytes(f.toPath());
			out.write(data, 0, data.length);
			out.closeEntry();
			out.close();
			return true;
		} catch (IOException e) {
			
		}
		return false;
	}
}
