/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.DashboardView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jamal
 */
public class MessageDisplayManager extends Thread {

    private List<String> messages = new ArrayList<>();
    private int index = 0;
    private DashboardView view;

    public MessageDisplayManager(DashboardView view) {
        this.view = view;
    }

    public synchronized void addMessage(String msg) {
        messages.add(msg);

        if (messages.size() > 10) {
            messages.remove(0);
        }
    }
    public synchronized void clearMessages() {
    messages.clear();
    index = 0;

    javax.swing.SwingUtilities.invokeLater(() -> {
        view.getbufferMessages().setText("");
    });
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}

            if (messages.isEmpty()) continue;

            String msg = messages.get(index);

            javax.swing.SwingUtilities.invokeLater(() -> {
                view.getbufferMessages().setText(msg);
            });

            index++;
            if (index >= messages.size()) {
                index = 0;
            }
        }
    }
}