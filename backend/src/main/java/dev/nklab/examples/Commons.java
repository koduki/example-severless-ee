/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.nklab.examples;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author koduki
 */
public class Commons {

    public static ZonedDateTime toDateTime(String date) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");
        return ZonedDateTime.parse(date, formatter);
    }
}
