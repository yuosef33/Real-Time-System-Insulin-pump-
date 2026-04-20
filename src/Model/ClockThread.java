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
     private InsulinController controller;

    private boolean resetDoneToday = false;

    public ClockThread(DashboardView dashboard,
                       InsulinController controller) {
        this.dashboard = dashboard;
        this.controller=controller;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}

            java.time.LocalTime now = java.time.LocalTime.now();

            String time = now.format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
            );

            javax.swing.SwingUtilities.invokeLater(() -> {
                dashboard.setCurrentTime(time);
            });

            if (now.getHour() == 0 && now.getMinute() == 0) {

                if (!resetDoneToday) {
                    controller.dailyReset();
                    resetDoneToday = true;
                }

            }
        }
    }
}