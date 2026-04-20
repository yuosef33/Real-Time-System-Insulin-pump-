/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.DashboardView;

/**
 *
 * @author jamal
 */
public class ClockThread extends Thread {

    private DashboardView dashboard;

    public ClockThread(DashboardView dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}

            String time = java.time.LocalTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

            javax.swing.SwingUtilities.invokeLater(() -> {
                dashboard.setCurrentTime(time);
            });
        }
    }
}