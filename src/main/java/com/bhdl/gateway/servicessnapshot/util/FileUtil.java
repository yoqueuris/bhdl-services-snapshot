package com.bhdl.gateway.servicessnapshot.util;

import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class FileUtil {

    public String readFromFilePath(String filePath) throws IOException {
        InputStream inputStream = FileUtil.class.getResourceAsStream(filePath);
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public void writeToFilePath(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(content);
        }
    }
}
