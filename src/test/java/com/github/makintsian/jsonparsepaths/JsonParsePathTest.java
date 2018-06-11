package com.github.makintsian.jsonparsepaths;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class JsonParsePathTest {

    private String json = "{\n" +
            "  \"firstName\": \"John\",\n" +
            "  \"lastName\" : \"doe\",\n" +
            "  \"age\"      : 26,\n" +
            "  \"address\"  : {\n" +
            "    \"streetAddress\": \"naist street\",\n" +
            "    \"city\"         : \"Nara\",\n" +
            "    \"postalCode\"   : \"630-0192\"\n" +
            "  },\n" +
            "  \"phoneNumbers\": [\n" +
            "    {\n" +
            "      \"type\"  : \"iPhone\",\n" +
            "      \"number\": \"0123-4567-8888\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"type\"  : \"home\",\n" +
            "      \"number\": \"0123-4567-8910\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    @Test
    public void testGetJsonPathsList() {
        JsonParsePaths jsonParsePaths = new JsonParsePaths(json);
        List<String> actual = jsonParsePaths.getJsonPathsList();
        List<String> expected = Arrays.asList("address.city", "address.postalCode", "address.streetAddress", "age",
                "firstName", "lastName", "phoneNumbers[*].number", "phoneNumbers[*].type");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetJsonPathsStr() {
        JsonParsePaths jsonParsePaths = new JsonParsePaths(json);
        String actual = jsonParsePaths.getJsonPathsStr();
        String expected = "address.city, address.postalCode, address.streetAddress, age, firstName, lastName, " +
                "phoneNumbers[*].number, phoneNumbers[*].type";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetJsonFullPathsList() {
        JsonParsePaths jsonParsePaths = new JsonParsePaths(json);
        List<String> actual = jsonParsePaths.getJsonFullPathsList();
        List<String> expected = Arrays.asList("address.city", "address.postalCode", "address.streetAddress", "age",
                "firstName", "lastName", "phoneNumbers[0].number", "phoneNumbers[0].type", "phoneNumbers[1].number",
                "phoneNumbers[1].type");
        Assert.assertEquals(expected, actual);
    }
}
