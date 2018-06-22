package com.service.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.service.dao.PetDAO;
import com.service.model.Pet;
import com.service.utils.FileUtils;

@RestController
public class RESTController {

	private static String FOLDERWORK = "C:\\blueway\\work\\";
	@Autowired
	private PetDAO petDAO;
	
	@RequestMapping(value="/")
	@ResponseBody
	public String welcome(){
		return "Welcome to RESTful service";
	}
	@RequestMapping(value="/pet", method=RequestMethod.POST, produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public Pet addPet(@RequestBody Pet pet) {
		return petDAO.addPet(pet);
	}
	
	@RequestMapping(value="/pets",method=RequestMethod.GET,produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<Pet> getPets() {
		return petDAO.getAllPet();
	}
	
	@RequestMapping(value="/pet{petName}",method=RequestMethod.GET,produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public Pet getPet(@PathVariable("petName") String petName) {
		return petDAO.getPet(petName);
	}
	
	@RequestMapping(value="/pet{petName}",method=RequestMethod.DELETE,produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public void deletePets(@PathVariable("petName") String petName) {
		petDAO.deletePet(petName);
	}
	
	@RequestMapping(value="/pet", method=RequestMethod.PUT, produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public Pet updatePet(@RequestBody Pet pet) {
		return petDAO.updatePet(pet);
	}
	
	@RequestMapping("/sendData")
	public ResponseEntity<MultiValueMap<String, Object>> handleRequestEntity(HttpServletRequest request, HttpServletResponse response) {
		MultiValueMap<String, Object> resp = new LinkedMultiValueMap<String, Object>();
		if(request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			
			try {
		        Iterator<Part> i = multipartRequest.getParts().iterator();
		        while(i.hasNext()) { 
		        	Part part = i.next();
		            String contentType = part.getContentType();
		            String name = part.getName();
		            System.out.println("RESTService.handleRequestEntity PartInfo: name = "+name+" /contentType = "+contentType);
		            HttpHeaders xHeader = new HttpHeaders();
	                xHeader.setContentType(FileUtils.getMediaType(contentType));
	                InputStream inputStreamPart = part.getInputStream();
		            if(name.indexOf("BWFile") >= 0) {
		            	String fileName = part.getSubmittedFileName();
		            	File f = FileUtils.copyInputStreamToFile(inputStreamPart, FOLDERWORK + fileName);
		            	xHeader.setContentDisposition(ContentDisposition.builder("attachment").filename(fileName).build());
		            	resp.add(name, new HttpEntity<Object>(Files.readAllBytes(f.toPath()),xHeader));
		            	//reponseMessage.append("\n- RESTful service received a file "+fileName +" from "+url);
		            }
		            else {
		            	String result = FileUtils.readFileToString(inputStreamPart);
		            	resp.add(name, new HttpEntity<Object>(result,xHeader));
		            	//reponseMessage.append("\n- RESTful service received a object ("+contentType+")" + result +" from "+url);
		            }
		        }
		        int x = resp.keySet().size();
		        System.out.println("Response keys = "+ resp.keySet());
		        if( x > 0) {
	        		//multi part response
	        		return new ResponseEntity<MultiValueMap<String, Object>>(resp,HttpStatus.OK);
		        }else return new ResponseEntity<MultiValueMap<String, Object>>(resp,HttpStatus.PARTIAL_CONTENT);
			}catch (Exception e) {
				resp.add("REST Exception", "RESTService.handleRequestEntity MultipartHttpServletRequest not have any Part");
				return new ResponseEntity<MultiValueMap<String, Object>>(resp,HttpStatus.NO_CONTENT);
			}
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(resp);
		//return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("RESTService.handleRequestEntity failure");
	}
	
	@RequestMapping(value="/sendDataXML")
	public ResponseEntity<MultiValueMap<String, Object>> handleRequestEntityXML(HttpServletRequest request, HttpServletResponse response) {
		MultiValueMap<String, Object> resp = new LinkedMultiValueMap<String, Object>();
		if(request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			
			try {
		        Iterator<Part> i = multipartRequest.getParts().iterator();
		        while(i.hasNext()) { 
		        	Part part = i.next();
		            String contentType = part.getContentType();
		            String name = part.getName();
		            HttpHeaders xHeader = new HttpHeaders();
	                xHeader.setContentType(FileUtils.getMediaType(contentType));
	                InputStream inputStreamPart = part.getInputStream();
		            if(name.indexOf("BWFile") >= 0) {
		            	String fileName = part.getSubmittedFileName();
		            	File f = FileUtils.copyInputStreamToFile(inputStreamPart, FOLDERWORK + fileName);
		            	xHeader.setContentDisposition(ContentDisposition.builder("attachment").filename(fileName).build());
		            	resp.add(name, new HttpEntity<Object>(Files.readAllBytes(f.toPath()),xHeader));
		            	//reponseMessage.append("\n- RESTful service received a file "+fileName +" from "+url);
		            }
		            else {
		            	String result = FileUtils.readFileToString(inputStreamPart);
		            	resp.add(name, new HttpEntity<Object>(result,xHeader));
		            	//reponseMessage.append("\n- RESTful service received a object ("+contentType+")" + result +" from "+url);
		            }
		        }
		        int x = resp.keySet().size();
		        if( x > 0) {
	        		//multi part response
	        		return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(resp);
		        }else return new ResponseEntity<MultiValueMap<String, Object>>(resp,HttpStatus.PARTIAL_CONTENT);
			}catch (Exception e) {
				resp.add("REST Exception", "RESTService.handleRequestEntity MultipartHttpServletRequest not have any Part");
				return new ResponseEntity<MultiValueMap<String, Object>>(resp,HttpStatus.NO_CONTENT);
			}
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(resp);
		//return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("RESTService.handleRequestEntity failure");
	}
	@RequestMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(
            @RequestParam("fileName") String fileName, HttpServletRequest request) throws IOException {
 
        MediaType mediaType = MediaType.parseMediaType(request.getServletContext().getMimeType(fileName));
        System.out.println("downloadFile filepath: " + fileName);
        System.out.println("downloadFile mediaType: " + mediaType);
        
 
        File file = new File(FOLDERWORK+ fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
 
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
}
