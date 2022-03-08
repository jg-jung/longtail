package com.ktds.longtail.elastic;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
 
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SpringBootTest
class ElasticApplicationTests {

	static Set fieldSet = new HashSet();
    static List fieldNames = Arrays.asList("cardNumber", "cvv", "expDate");
    
	@Test
	void contextLoads() {
	}
	
	/*
	@Test
	public void mask() throws IOException {
		String jsonString = "{\n" +
		            "    \"key1\":\"value1\",\n" +
		            "    \"key2\":\"value2\",\n" +
		            "    \"key3\":\"value3\"\n" +
		            "}";
		    Map<String, Object> map;    

		    // Convert json to map
		    ObjectMapper mapper = new ObjectMapper();
		    try {
		        TypeReference ref = new TypeReference<Map<String, Object>>() { };
		        map = (Map<String, Object>) mapper.readValue(jsonString, ref);
		    } catch (IOException e) {
		        System.out.print("cannot create Map from json" + e.getMessage());
		        throw e;
		    }

		    // Process map
		    if(map.containsKey("key2")) {
		        map.put("key2","xxxxxxxxx");
		    }

		    // Convert back map to json
		    String jsonResult = "";
		    try {
		        jsonResult = mapper.writeValueAsString(map);
		    } catch (IOException e) {
		        System.out.print("cannot create json from Map" + e.getMessage());
		    }

		    System.out.print(jsonResult);
		    
	}
	*/
	
	/*
	@Test
	public void maskingTest() throws IOException {
		
		StringBuilder contentBuilder = new StringBuilder();
        try (Stream stream = Files.lines(
                Paths.get(
                        "D:\\Saurabh Gupta\\Workspace\\JavaTestExamples\\src\\main\\resources\\AccountDetail.json"),
                StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create GSON object
        //apply NullSearialization and Pretty formatting by GSON Builder
        Gson gson = getJsonBuilder().create();
        AccountDetail accounDetail = gson.fromJson(contentBuilder.toString(), AccountDetail.class);
        mask(accounDetail);
        System.out.println(gson.toJson(accounDetail));
        
	}
	*/
	
}
