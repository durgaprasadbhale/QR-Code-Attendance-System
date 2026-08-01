package com.attendance;

import com.attendance.ui.LoginUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginUI();
        });

    }

}