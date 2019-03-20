/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.homework.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nikola
 */
public class Dbutil {
    
    private Connection con = null;
    private static Dbutil instance;
    String url = "";
    String user = "";
    String pass = "";
    
    private Dbutil() {
        try {
        readConfigProperties(); 
        con = DriverManager.getConnection(url, user, pass);
        con.setAutoCommit(false);
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
    
    public static Dbutil getInstance() {
        if(instance == null) {
            instance = new Dbutil();
        }
        
        return instance;
    }
    
    public String createInsertQuery(Object inst) {
        Class<?> instance = inst.getClass();
       
        /*
        
        insert into ClassName(field1, field2, field3..) values(?,?,?,..)
        
        */
        
        StringBuilder fields = new StringBuilder();
        StringBuilder nOfVariables = new StringBuilder();
        
        for (Field declaredField : instance.getDeclaredFields()) {
            if(fields.length() > 1) {
                fields.append(",");
                nOfVariables.append(",");
            }
            fields.append(declaredField.getName());
            nOfVariables.append("?");
        }
        return "insert into "+instance.getSimpleName()+" ("+ fields.toString()+") "+" values ("+ nOfVariables.toString()+")";
    }

   

    private void readConfigProperties() {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            prop.load(fis);
            url = prop.getProperty(DBParameters.URL);
            user = prop.getProperty(DBParameters.USER);
            pass = prop.getProperty(DBParameters.PASS);
        } catch (IOException ex) {
            ex.getMessage();
        } 
    }
    
    public void commit() {
        try {
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Dbutil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void rollback() {
        try {
            con.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(Dbutil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public PreparedStatement createInsertStatement(Object instance) {
        PreparedStatement ps = null;
        
        Class<?> inst = instance.getClass();
        
        try {
            System.out.println(createInsertQuery(instance));
            ps = con.prepareStatement(createInsertQuery(instance));
            
            int i = 0;
            Field[] fields = inst.getDeclaredFields();
            
            for (Field field : fields) {
                Field f = fields[i];
                f.setAccessible(true);
                
                Object value = f.get(instance);
                ps.setObject((i+1), value);
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dbutil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Dbutil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Dbutil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ps;
    }
}
