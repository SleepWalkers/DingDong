package com.sleepwalker.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InitDaoUtils {

    private static final String ORIGINAL_DAO_SUFFIX           = "Mapper";
    private static final String DAO_SUFFIX                    = "Dao";

    private static final String INSERT_METHOD_PREFIX          = "int insert";
    private static final String REPLACED_INSERT_METHOD_PREFIX = "void insert";

    private static final String UPDATE_METHOD_PREFIX          = "int update";
    private static final String REPLACED_UPDATE_METHOD_PREFIX = "void update";

    private static final String DELETE_METHOD_PREFIX          = "int delete";
    private static final String REPLACED_DELETE_METHOD_PREFIX = "void delete";

    private static final String NEED_DELETE_CONTENT_1         = "Selective";

    private static final String NEED_DELETE_CONTENT_2         = "WithBLOBs";
    private static final String PRIMARYKEY                    = "PrimaryKey";

    private static final String ORIGINAL_PARAM_NAME           = "record";

    private static final String REPLACED_PRIMARYKEY           = "Id";

    public static void initDao(String filePath) {
        File fileDic = new File(filePath);
        if (fileDic == null || !fileDic.isDirectory()) {
            return;
        }

        try {
            for (File file : fileDic.listFiles()) {
                if (!file.getName().endsWith(ORIGINAL_DAO_SUFFIX + ".java")) {
                    continue;
                }
                String initedFileText = readFileByLines(file);
                File newFile = new File(
                    filePath + "/" + file.getName().replaceAll(ORIGINAL_DAO_SUFFIX, DAO_SUFFIX));

                FileWriter fileWritter = new FileWriter(newFile);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write(initedFileText);
                bufferWritter.close();
                file.delete();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getModelName(String content) {
        String[] arr = content.split(" ");
        String modelName = arr[2].replaceAll(ORIGINAL_DAO_SUFFIX, "");

        String first = modelName.charAt(0) + "";
        modelName = modelName.replaceFirst(first, first.toLowerCase());
        return modelName;
    }

    public static void main(String[] args) {
        initDao(
            "/home/sleepwalker/workspace/BuyerologieService/src/main/java/com/buyerologie/user/dao");
    }

    private static String getPackagePrefix(String content) {
        if (content.startsWith("package")) {
            return content.split(" ")[1].replaceAll("\\.dao", "");
        }
        return null;
    }

    public static String readFileByLines(String filePath) {
        return readFileByLines(new File(filePath));
    }

    public static String readFileByLines(File file) {
        BufferedReader reader = null;
        StringBuilder fileBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String lineContent = null;
            String modelName = null;
            while ((lineContent = reader.readLine()) != null) {
                if (lineContent.contains(ORIGINAL_DAO_SUFFIX)) {
                    modelName = getModelName(lineContent);
                    lineContent = lineContent.replaceAll(ORIGINAL_DAO_SUFFIX, DAO_SUFFIX);
                    fileBuilder.append(lineContent).append("\n");
                    continue;
                }

                if (lineContent.contains(NEED_DELETE_CONTENT_1)
                    || lineContent.contains(NEED_DELETE_CONTENT_2)) {
                    continue;
                }
                if (lineContent.contains(INSERT_METHOD_PREFIX)) {
                    lineContent = lineContent.replaceAll(INSERT_METHOD_PREFIX,
                        REPLACED_INSERT_METHOD_PREFIX);
                    lineContent = lineContent.replaceAll(ORIGINAL_PARAM_NAME, modelName);
                } else if (lineContent.contains(UPDATE_METHOD_PREFIX)) {
                    lineContent = lineContent.replaceAll(UPDATE_METHOD_PREFIX,
                        REPLACED_UPDATE_METHOD_PREFIX);
                    lineContent = lineContent.replaceAll(ORIGINAL_PARAM_NAME, modelName);
                } else if (lineContent.contains(DELETE_METHOD_PREFIX)) {
                    lineContent = lineContent.replaceAll(DELETE_METHOD_PREFIX,
                        REPLACED_DELETE_METHOD_PREFIX);
                    lineContent = lineContent.replaceAll("Long", "int");
                }
                if (lineContent.contains(PRIMARYKEY)) {
                    lineContent = lineContent.replaceAll(PRIMARYKEY, REPLACED_PRIMARYKEY);
                    lineContent = lineContent.replaceAll("Long", "int");
                }
                fileBuilder.append(lineContent).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return fileBuilder.toString();
    }
}
