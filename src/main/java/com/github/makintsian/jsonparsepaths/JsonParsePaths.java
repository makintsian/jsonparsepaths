package com.github.makintsian.jsonparsepaths;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JsonParsePaths {

    private JsonParser parser;
    private List<String> paths;
    private List<String> fullPaths;
    private String jsonPathsStr;
    private List<String> jsonPathsList;
    private int number = 0;

    public JsonParsePaths(String json) {
        this.parser = new JsonParser();
        this.paths = new ArrayList<>();
        this.fullPaths = new ArrayList<>();
        parseJson(json);
        setJsonPathsStr();
        setJsonPathsList();
    }

    public JsonParsePaths(FileReader fileReader) {
        this.parser = new JsonParser();
        this.paths = new ArrayList<>();
        this.fullPaths = new ArrayList<>();
        parseJson(fileReader);
        setJsonPathsStr();
        setJsonPathsList();
    }

    private void setJsonPathsStr() {
        StringBuilder stringBuilder = new StringBuilder();
        removeDuplicates(paths).forEach(path -> {
            if (stringBuilder.length() != 0) stringBuilder.append(", ");
            stringBuilder.append(path);
        });
        this.jsonPathsStr = stringBuilder.toString();
    }

    private void setJsonPathsList() {
        this.jsonPathsList = removeDuplicates(paths);
    }

    /**
     * @return String with json paths
     */
    public String getJsonPathsStr() {
        return jsonPathsStr;
    }

    /**
     * @return List with json paths
     */
    public List<String> getJsonPathsList() {
        return jsonPathsList;
    }

    /**
     * @return List with full json paths
     */
    public List<String> getJsonFullPathsList() {
        return fullPaths;
    }

    /**
     * @param json String
     */
    private void parseJson(String json) {
        JsonElement jsonTree = parser.parse(json);
        if (!jsonTree.isJsonObject() && !jsonTree.isJsonArray()) throw new JsonParsePathsException("Json is not valid");
        writeAndSortJson(jsonTree);
    }

    /**
     * @param fileReader FileReader
     */
    private void parseJson(FileReader fileReader) {
        JsonElement jsonTree = parser.parse(fileReader);
        if (!jsonTree.isJsonObject() && !jsonTree.isJsonArray()) throw new JsonParsePathsException("File is not valid");
        writeAndSortJson(jsonTree);
    }

    /**
     * @param jsonTree JsonElement
     */
    private void writeAndSortJson(JsonElement jsonTree) {
        writeJsonPaths(jsonTree, "");
        writeFullJsonPaths(jsonTree, "");
        Collections.sort(paths);
        Collections.sort(fullPaths);
    }

    /**
     * @param elem JsonElement
     * @param path current json path
     * @return list with json paths
     */
    private List<String> writeJsonPaths(JsonElement elem, String path) {
        List<String> jsonPaths = new ArrayList<>();
        if (elem.isJsonObject()) {
            JsonObject jsonObject = elem.getAsJsonObject();
            jsonObject.keySet().forEach(s -> {
                JsonElement jsonElement = jsonObject.get(s);
                if (jsonElement.isJsonPrimitive() || jsonElement.isJsonNull()) {
                    if (path.isEmpty()) paths.add(path + s);
                    else paths.add(path + "." + s);
                } else {
                    if (path.isEmpty()) writeJsonPaths(jsonElement, path + s);
                    else writeJsonPaths(jsonElement, path + "." + s);
                }
            });
        } else if (elem.isJsonArray()) {
            JsonArray jsonArray = elem.getAsJsonArray();
            jsonArray.forEach(e -> {
                if (e.isJsonArray() || e.isJsonObject()) writeJsonPaths(e, path + "[*]");
                else paths.add(path + "[*]");
            });
        }
        return jsonPaths;
    }

    /**
     * @param elem JsonElement
     * @param path current json path
     * @return list with full json paths
     */
    private List<String> writeFullJsonPaths(JsonElement elem, String path) {
        List<String> jsonPaths = new ArrayList<>();
        if (elem.isJsonObject()) {
            JsonObject jsonObject = elem.getAsJsonObject();
            jsonObject.keySet().forEach(s -> {
                JsonElement jsonElement = jsonObject.get(s);
                if (jsonElement.isJsonPrimitive() || jsonElement.isJsonNull()) {
                    if (path.isEmpty()) fullPaths.add(path + s);
                    else fullPaths.add(path + "." + s);
                } else {
                    if (path.isEmpty()) writeFullJsonPaths(jsonElement, path + s);
                    else writeFullJsonPaths(jsonElement, path + "." + s);
                }
            });
        } else if (elem.isJsonArray()) {
            JsonArray jsonArray = elem.getAsJsonArray();
            jsonArray.forEach(e -> {
                if (e.isJsonArray() || e.isJsonObject()) writeFullJsonPaths(e, path + "[" + number + "]");
                else fullPaths.add(path + "[" + number + "]");
                number++;
            });
        }
        return jsonPaths;
    }

    /**
     * @param list full list
     * @return list without duplicates
     */
    private List<String> removeDuplicates(List<String> list) {
        return list.stream().distinct().collect(Collectors.toList());
    }
}
