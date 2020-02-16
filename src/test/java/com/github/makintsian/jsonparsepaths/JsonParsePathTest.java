package com.github.makintsian.jsonparsepaths;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JsonParsePathTest {

    private static final String FILE_PATH = "src/test/resources/test.json";

    @Test
    public void testGetJsonPathsListFromString() throws IOException {
        String json = Files.readString(Paths.get(FILE_PATH));
        JsonParsePaths jsonParsePaths = new JsonParsePaths(json);
        List<String> actual = jsonParsePaths.getJsonPathsList();
        List<String> expected = Arrays.asList("address.city", "address.postalCode", "address.streetAddress", "age",
                "devices[*]", "firstName", "languages[*]", "lastName", "phoneNumbers[*].number",
                "phoneNumbers[*].type", "tax");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetJsonPathsStrFromString() throws IOException {
        String json = Files.readString(Paths.get(FILE_PATH));
        JsonParsePaths jsonParsePaths = new JsonParsePaths(json);
        String actual = jsonParsePaths.getJsonPathsStr();
        String expected = "address.city, address.postalCode, address.streetAddress, age, devices[*], firstName, " +
                "languages[*], lastName, phoneNumbers[*].number, phoneNumbers[*].type, tax";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetJsonFullPathsListFromString() throws IOException {
        String json = Files.readString(Paths.get(FILE_PATH));
        JsonParsePaths jsonParsePaths = new JsonParsePaths(json);
        List<String> actual = jsonParsePaths.getJsonFullPathsList();
        List<String> expected = Arrays.asList("address.city", "address.postalCode", "address.streetAddress", "age",
                "devices[]", "firstName", "languages[0]", "languages[1]", "lastName", "phoneNumbers[0].number",
                "phoneNumbers[0].type", "phoneNumbers[1].number", "phoneNumbers[1].type", "tax");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetJsonPathsListFromFileReader() throws FileNotFoundException {
        FileReader fileReader = new FileReader(FILE_PATH);
        JsonParsePaths jsonParsePaths = new JsonParsePaths(fileReader);
        List<String> actual = jsonParsePaths.getJsonPathsList();
        List<String> expected = Arrays.asList("address.city", "address.postalCode", "address.streetAddress", "age",
                "devices[*]", "firstName", "languages[*]", "lastName", "phoneNumbers[*].number",
                "phoneNumbers[*].type", "tax");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetJsonPathsStrFromFileReader() throws FileNotFoundException {
        FileReader fileReader = new FileReader(FILE_PATH);
        JsonParsePaths jsonParsePaths = new JsonParsePaths(fileReader);
        String actual = jsonParsePaths.getJsonPathsStr();
        String expected = "address.city, address.postalCode, address.streetAddress, age, devices[*], firstName, " +
                "languages[*], lastName, phoneNumbers[*].number, phoneNumbers[*].type, tax";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetJsonFullPathsListFromFileReader() throws FileNotFoundException {
        FileReader fileReader = new FileReader(FILE_PATH);
        JsonParsePaths jsonParsePaths = new JsonParsePaths(fileReader);
        List<String> actual = jsonParsePaths.getJsonFullPathsList();
        List<String> expected = Arrays.asList("address.city", "address.postalCode", "address.streetAddress", "age",
                "devices[]", "firstName", "languages[0]", "languages[1]", "lastName", "phoneNumbers[0].number",
                "phoneNumbers[0].type", "phoneNumbers[1].number", "phoneNumbers[1].type", "tax");
        Assert.assertEquals(expected, actual);
    }
}
