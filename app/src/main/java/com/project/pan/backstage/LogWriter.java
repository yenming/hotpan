package com.project.pan.backstage;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LogWriter {

    private String name;
    private String baseDir;
    private String filePath;
    private File f;
    private long startTimestamp = 0;
    private Map<String, String> columns = new HashMap<>();
    private String[] labels = null;

    public LogWriter(String filename) {
        name = filename;
        baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        filePath = baseDir + File.separator + name;
        f = new File(filePath);
    }

    public void rename(String newFilename) {
        String newFilePath = baseDir + File.separator + newFilename;
        File newF = new File(newFilePath);
        if(f.exists()&&!f.isDirectory()) {
            f.renameTo(newF);
        }
        filePath = newFilePath;
        f = newF;
        name = newFilename;
    }

    public void write(String inputString) {
        String[] lines = inputString.split("\r");

        if(lines.length != (columns.size()-2)) {
            columns.clear();
            columns.put("timestamp", "");
            columns.put("elapsed", "");
            for(String line : lines) {
                String[] substrings = line.split("\t");
                if (substrings.length < 1) continue;
                String label = substrings[0];
                columns.put(label, "");
            }
            writeLabels();
        }
        for(String line : lines) {
            String[] substrings = line.split("\t");
            if(substrings.length < 2) continue;
            String label = substrings[0];
            String value = substrings[1];
            switch (label)
            {
                case "tube":
                case "plate":
                case "triac":
                    value = new DecimalFormat("0.0").format(Double.parseDouble(value)/10);
                    break;
                case "pid_Setpoint":
                    value = new DecimalFormat("0.0").format(Double.parseDouble(value)/10);
                    break;
            }
            columns.put(label, value);
        }
        writeValues();
    }

/*
    String adcToTemp(String adc) {
        double v = Integer.parseInt(adc) * 5.0 / 1023.0;
        double r = 10000.0 * v / (5.0 - v);
        double t = 3950.0 / (Math.log(r) + 1.73543945201787) - 273.15;
        return new DecimalFormat("0.00").format(t);
    }
*/
    public void writeLabels() {
        startTimestamp = 0;
        labels = new String[columns.size()];
        labels = columns.keySet().toArray(labels);
        try {
            CSVWriter writer;
            // File exist
            if(f.exists()&&!f.isDirectory()) {
                f.delete();
            }
            writer = new CSVWriter(new FileWriter(filePath));
            writer.writeNext(labels);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeValues() {
        long timestamp = Calendar.getInstance().getTimeInMillis();
        if(startTimestamp == 0) {
            startTimestamp = Calendar.getInstance().getTimeInMillis();
        }
        columns.put("timestamp", String.valueOf(timestamp));
        columns.put("elapsed", String.valueOf(timestamp-startTimestamp));
        try {
            CSVWriter writer;
            String[] data = new String[columns.size()];
            data = columns.values().toArray(data);
            // File exist
            if(f.exists()&&!f.isDirectory()) {
                FileWriter mFileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(mFileWriter);
            } else {
                writer = new CSVWriter(new FileWriter(filePath));
            }
            writer.writeNext(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
