package com.example.bankSystem.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Otpstorage
{
    private final Map<String,String> tempStorage = new ConcurrentHashMap<>();

    public Map<String, String> getTempStorage()
    {
        return tempStorage;
    }

    public void setTempStorage(String userId, String otp)
    {
        tempStorage.put(userId,otp);
    }

    public void removeTempStorage(String userId)
    {
        tempStorage.remove(userId);
    }

}
