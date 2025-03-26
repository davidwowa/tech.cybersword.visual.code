package tech.cybersword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;

public class App {
    public static void main(String[] args) {
        String folderPath = "/Users/david/git/tech.cybersword.payloads/";

        Set<String> uniqueStrings = new HashSet<>();

        try {
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
            // TODO something with the files is wrong
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                uniqueStrings.add(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Gesammelte Strings: " + uniqueStrings.size());
        App app = new App();
        int c = 0;
        for (String string : uniqueStrings) {
            app.createQRandBarcode(string);
            c++;
        }
        System.out.println("✅ " + c + " QR-Codes und Barcodes erfolgreich erstellt!");
    }

    public void createQRandBarcode(String content) {
        try {
            if (null != content && content.length() > 5) {
                String base64 = Base64.getEncoder().encodeToString(content.getBytes());

                generateQRCodeImage(content, content.length(), content.length(),
                        "viusal_bar_qr_codes/qr-" + base64 + ".png");
                generateCode128BarcodeImage(content, content.length(), content.length(),
                        "viusal_bar_qr_codes/barcode-" + base64 + ".png");
                System.out.println("✅ QR-Code und Barcode erfolgreich gespeichert!");
            } else {
                System.out.println("❌ Kein Inhalt für QR-Code und Barcode gefunden! " + content);
            }

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void generateCode128BarcodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        Code128Writer code128Writer = new Code128Writer();
        BitMatrix bitMatrix = code128Writer.encode(text, BarcodeFormat.CODE_128, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
