import java.awt.Point;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    // saving/files
    private static int currentVersion = 0;
    private static String currentDir = System.getProperty("user.dir");
    private static File version = new File(currentDir, "logs\\version.txt");
    private static File polygonSave;
    private static File wSave;
    private static File bSave;
    
    public static void fileSetup() {
        try {
            if (!version.exists()) {
                version.createNewFile();
            }
            Scanner scanner = new Scanner(version);
            String nextLn = scanner.nextLine();
            if (!nextLn.equals("0")) {
                currentVersion = Integer.parseInt(nextLn);
            }
            else {
                currentVersion = 0;
            }
            FileWriter writer = new FileWriter(version);
            writer.write(String.valueOf((currentVersion + 1)));
            writer.close();
            scanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private static void writeData(String data, File file) {
        try  {
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void writeData(String[] dataArr, File file) {
        String data = "";
        for (int i = 0; i < dataArr.length; i++) {
            data += (dataArr[i] + "\n");
        }
        writeData(data, file);
    }
    
    private static String[] readData(File file) {
        String[] result = new String[0];
        try {
            result = new String[(int)Files.lines(file.toPath()).count()];
            Scanner scanner = new Scanner(file);
            int index = 0;
            while (scanner.hasNextLine()) {
                result[index] = scanner.nextLine();
                index++;
            }
            scanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static void PolygonSave(List<Point> points) {
        polygonSave = new File(currentDir, "logs\\polygonData" + currentVersion + ".txt");
        try {
            if (!polygonSave.exists()) {
                polygonSave.createNewFile();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String[] polygonPoints = new String[points.size()];
        for (int i = 0; i < polygonPoints.length; i++) {
            polygonPoints[i] = String.valueOf(points.get(i));
        }
        writeData(polygonPoints, polygonSave);
    }
    
    public static void SaveingBrain(double[] w, double[] b) {
        wSave = new File(currentDir, "logs\\weights" + currentVersion + ".txt");
        try {
            if (!wSave.exists()) {
                wSave.createNewFile();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String[] weights = new String[w.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = String.valueOf(w[i]);
        }
        writeData(weights, wSave);
        
        bSave = new File(currentDir, "logs\\biases" + currentVersion + ".txt");
        try {
            if (!bSave.exists()) {
                bSave.createNewFile();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String[] biases = new String[b.length];
        for (int i = 0; i < biases.length; i++) {
            biases[i] = String.valueOf(b[i]);
        }
        writeData(biases, bSave);
    }
    
    public static String[] FileLoading(String dialog) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(version);
        fc.setDialogTitle(dialog);
        int returnVal = fc.showOpenDialog(fc);
        File file = fc.getSelectedFile();
        String[] data = new String[0];
        if (returnVal == 0) {
            try {
                data = new String[(int)Files.lines(file.toPath()).count()];
                data =  readData(file);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }
    
    public static double[][][] LoadBrain(int x, int h, int y) {
        String[] w = FileLoading("Weights values");
        double[] wDouble = new double[w.length];
        for (int i = 0; i < wDouble.length; i++) {
            wDouble[i] = Double.parseDouble(w[i]);
        }
        String[] b = FileLoading("Bias values");
        double[] bDouble = new double[b.length];
        for (int i = 0; i < bDouble.length; i++) {
            bDouble[i] = Double.parseDouble(b[i]);
        }
        
        double[][] w1 = new double[x][h];
        double[][] w2 = new double[h][y];
        double[][] b1 = new double[x][1];
        double[][] b2 = new double[h][1];
        
        int wIndex = 0;
        for (int i = 0; i < w1.length; i++) {
            for (int j = 0; j < w1[0].length; j++) {
                w1[i][j] = wDouble[wIndex];
                wIndex++;
            }
        }
        for (int i = 0; i < w2.length; i++) {
            for (int j = 0; j < w2[0].length; j++) {
                w2[i][j] = wDouble[wIndex];
                wIndex++;
            }
        }
        
        int bIndex = 0;
        for (int i = 0; i < b1.length; i++) {
            for (int j = 0; i < b1[0].length; i++) {
                b1[i][j] = bDouble[bIndex];
                bIndex++;
            }
        }
        for (int i = 0; i < b2.length; i++) {
            for (int j = 0; j < b2[0].length; j++) {
                b2[i][j] = bDouble[bIndex];
                bIndex++;
            }
        }
        double[][][] result = {w1, w2, b1, b2};
        return result;
    }
}
