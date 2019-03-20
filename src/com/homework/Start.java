/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.homework;

import com.homework.db.Broker;
import com.homework.domain.User;

/**
 *
 * @author Nikola
 */
public class Start {
    public static void main(String[] args) {
        Broker b = new Broker();
        
        User u = new User(3, "Nikola", "Obradovic", "no@gmail.com");
        
        b.saveObject(u);
    }
}
