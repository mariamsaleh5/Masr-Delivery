package org.example;

public class PlatformConfig {

    private static PlatformConfig instance;

    private double serviceFeeRate;
    private int poolSize;
    private String filePath;

    private PlatformConfig() {
        serviceFeeRate = 0.10;
        poolSize = 10;
        filePath = "data/";
    }

    public static PlatformConfig getInstance() {

        if (instance == null) {
            instance = new PlatformConfig();
        }

        return instance;
    }

    public double getServiceFeeRate() {
        return serviceFeeRate;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public String getFilePath() {
        return filePath;
    }
}