package curdOperations;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class AddNewDevice {

	@Test
	public void addNewDevice() throws IOException {
		RestAssured.baseURI = "https://api.restful-api.dev";
		String response = 
				    given()
					    .header("Content-Type", "application/json")
					    .body(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\src\\test\\java\\testData\\newDevicePayload.json"))))
				    
				    .when()
				        .post("/objects")
				    .then()
				         .statusCode(200)
				         .log().body()
				         .extract().response().asString();
		
		// Parse the response into a JsonPath to extract all the required fields
	    JsonPath js = new JsonPath(response); 
	    String id = js.getString("id");
	    String name = js.getString("name");
	    String createdAt = js.getString("createdAt");
	    int year = js.getInt("data.year");
	    double price = js.getDouble("data.price");
	    String cpuModel = js.getString("data['CPU model']");
        String hardDiskSize = js.getString("data['Hard disk size']");
        
        
        //Validate the added new device details from the response
        Assert.assertEquals(name,"Apple MacBook Pro 14");
        Assert.assertEquals(year, 2020, "Year should be 2020");
        Assert.assertEquals(price, 1949.99, "Price should be 1949.99");
        Assert.assertEquals(cpuModel, "Intel Core i9", "CPU model should be 'Intel Core i9'");
        Assert.assertEquals(hardDiskSize, "1 TB", "Hard disk size should be '1 TB'");
        
        
        // Validate id and createdAt should not be null
        Assert.assertNotNull(id,"ID should not be null");
        Assert.assertNotNull(createdAt,"Creation date should not be null");
        
        
	    System.out.println("Extracted ID: " + id);
		
	}
	
	@Test
	public void validateThatIDAndCreationDateCanNotAcceptNullValues() throws Exception {
		
		
		RestAssured.baseURI = "https://api.restful-api.dev";

		try {
			given()
			    .header("Content-Type", "application/json")
			    .body(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\src\\test\\java\\testData\\newDeviceInvalidPayload.json"))))
			
			.when()
			    .post("/objects")
			.then()
			     .statusCode(200)
			     .log().body()
			     .extract().response().asString();
					    
		} catch (IOException e) {
			throw new Exception("Year can not have null values");
	
		}
		

	}
	
}
