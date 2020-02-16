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

    private List<String> paths;
    private List<String> fullPaths;

    public JsonParsePaths(String json) {
        this.paths = new ArrayList<>();
        this.fullPaths = new ArrayList<>();
        parseJson(json);
    }

    public JsonParsePaths(FileReader fileReader) {
        this.paths = new ArrayList<>();
        this.fullPaths = new ArrayList<>();
        parseJson(fileReader);
    }

    private String jsonPathsToStr() {
        StringBuilder stringBuilder = new StringBuilder();
        removeDuplicates(paths).forEach(path -> {
            if (stringBuilder.length() != 0) stringBuilder.append(", ");
            stringBuilder.append(path);
        });
        return stringBuilder.toString();
    }

    /**
     * @return String with json paths
     */
    public String getJsonPathsStr() {
        return jsonPathsToStr();
    }

    /**
     * @return List with json paths
     */
    public List<String> getJsonPathsList() {
        return removeDuplicates(paths);
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
        JsonElement jsonTree = JsonParser.parseString(json);
        if (!jsonTree.isJsonObject() && !jsonTree.isJsonArray()) throw new JsonParsePathsException("Json is not valid");
        writeAndSortJson(jsonTree);
    }

    /**
     * @param fileReader FileReader
     */
    private void parseJson(FileReader fileReader) {
        JsonElement jsonTree = JsonParser.parseReader(fileReader);
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
                if (jsonElement.isJsonPrimitive() || jsonElement.isJsonNull()
                        || (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().keySet().isEmpty())) {
                    if (path.isEmpty()) paths.add(path + s);
                    else paths.add(path + "." + s);
                } else {
                    if (path.isEmpty()) writeJsonPaths(jsonElement, path + s);
                    else writeJsonPaths(jsonElement, path + "." + s);
                }
            });
        } else if (elem.isJsonArray()) {
            JsonArray jsonArray = elem.getAsJsonArray();
            if (jsonArray.size() == 0) paths.add(path + "[*]");
            else jsonArray.forEach(e -> {
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
                if (jsonElement.isJsonPrimitive() || jsonElement.isJsonNull()
                        || (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().keySet().isEmpty())) {
                    if (path.isEmpty()) fullPaths.add(path + s);
                    else fullPaths.add(path + "." + s);
                } else {
                    if (path.isEmpty()) writeFullJsonPaths(jsonElement, path + s);
                    else writeFullJsonPaths(jsonElement, path + "." + s);
                }
            });
        } else if (elem.isJsonArray()) {
            final int[] number = {0};
            JsonArray jsonArray = elem.getAsJsonArray();
            if (jsonArray.size() == 0) fullPaths.add(path + "[]");
            else jsonArray.forEach(e -> {
                if (e.isJsonArray() || e.isJsonObject()) writeFullJsonPaths(e, path + "[" + number[0] + "]");
                else fullPaths.add(path + "[" + number[0] + "]");
                number[0]++;
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
