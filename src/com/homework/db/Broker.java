/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.homework.db;

import com.homework.util.Dbutil;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Nikola
 */
public class Broker {

    //  System.out.println(query);
    public void saveObject(Object object) {
        try {
            PreparedStatement ps = Dbutil.getInstance().createInsertStatement(object);
            ps.executeUpdate();
            Dbutil.getInstance().commit();
        } catch (SQLException ex) {
            Dbutil.getInstance().rollback();
            Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
