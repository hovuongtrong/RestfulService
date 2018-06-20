package com.service.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
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

@RestController
public class RESTController {

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
	public String handleRequestEntity(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("handleRequestEntity requestClass = "+request.getClass());
		if(request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			try {
		        Iterator<Part> i = multipartRequest.getParts().iterator(); 
		        while(i.hasNext()) { 
		        	Part part = i.next();
		            String contentType = part.getContentType();
		            String name = part.getName();
		            System.out.println("handleRequestEntity name = "+name+" /contentType = "+contentType);
		            if(name.indexOf("BWFile") >= 0) {
		            	String fileName = part.getSubmittedFileName();
		            	OutputStream output = new FileOutputStream(new File("C:\\blueway\\work\\"+fileName));
		            	IOUtils.copy(part.getInputStream(), output);
		            	output.close();
		            	System.out.println("handleRequestEntity saving "+name+" at C:\\blueway\\work\\"+fileName);
		            }
		            else {
		            	Scanner s = new Scanner(part.getInputStream()).useDelimiter("\\A");
		            	String result = s.hasNext() ? s.next() : "";
		            	System.out.println("handleRequestEntity content = "+result);
		            }
		            //}else System.out.println("handleRequestEntity value = "+part.get);
		           
		        }
			}catch (Exception e) {
				return "RESTService MultipartHttpServletRequest not have any Part";
			}
			return "RESTService got request entity";
		}
		return "RESTService not got MultipartHttpServletRequest";
	}
	
}
