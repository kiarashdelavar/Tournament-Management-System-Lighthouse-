package com.kiarash.tournamentsystem;

import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class PDFGenerator {
    public static void generate(Node content, Stage ownerStage) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(ownerStage)) {
            boolean success = job.printPage(content);
            if (success) {
                job.endJob();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("PDF Exported");
                alert.setContentText("✅ Match schedule exported successfully.");
                alert.showAndWait();
            } else {
                showError("Print job failed.");
            }
        } else {
            showError("Printer dialog was cancelled or unavailable.");
        }
    }

    private static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("❌ " + message);
        alert.showAndWait();
    }

    public static void generate(String s, String string) {
    }
}
