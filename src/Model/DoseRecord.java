/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDateTime;

/**
 *
 * @author jamal
 */
public class DoseRecord {
    Double dose;
    LocalDateTime time;

    public DoseRecord(Double dose, LocalDateTime time) {
        this.dose = dose;
        this.time = time;
    }
}