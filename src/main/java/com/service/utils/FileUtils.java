package com.service.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

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
}
