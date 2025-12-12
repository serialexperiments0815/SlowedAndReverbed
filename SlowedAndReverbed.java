import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;

public class SlowedAndReverbed {
    
    private String filePath;
    public void main(String[] args){

        NumberFormat doubleFormat = NumberFormat.getNumberInstance(Locale.US);
        doubleFormat.setGroupingUsed(false);

        NumberFormatter doubleFormater = new NumberFormatter(doubleFormat);
        doubleFormater.setValueClass(Double.class);
        doubleFormater.setAllowsInvalid(true);
        doubleFormater.setCommitsOnValidEdit(true);


        JFrame frame = new JFrame("SlowedAndReverbed");
        JPanel panel = new JPanel();
        JPanel panelMid = new JPanel();
        JPanel panelMidButtons = new JPanel();
        JButton newButton = new JButton("Select file");
        JButton saveButton = new JButton("Save file");
        JFormattedTextField speedField = new JFormattedTextField(doubleFormater);
        JFormattedTextField inGainField = new JFormattedTextField(doubleFormater);
        JFormattedTextField outGainField = new JFormattedTextField(doubleFormater);
        JFormattedTextField delayField = new JFormattedTextField(doubleFormater);
        JFormattedTextField decayField = new JFormattedTextField(doubleFormater);
        JLabel pathLabel = new JLabel("No file selected");
        JLabel speedLabel = new JLabel("Speed from 0.5 - 2.0");
        JLabel inGainLabel = new JLabel("In-Gain from 0.0 - 1.0");
        JLabel outGainLabel = new JLabel("Out-Gain from 0.0 - 1.0");
        JLabel delayLabel = new JLabel("delay from 0 - 2000");
        JLabel decayLabel = new JLabel("decay from 0.0 - 1.0");
        JLabel reverbLabel = new JLabel("Reverb: 0%");


        newButton.addActionListener(e -> {
            filePath = getFile(frame);
            String pathLabelText = new java.io.File(filePath).getName(); //Object has no references after, garbage collector kills it.
            pathLabel.setText(pathLabelText);
            newButton.setText("Select new file");
        });


        frame.setSize(400,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());


        panel.add(newButton);
        panel.add(saveButton);

        panelMidButtons.setLayout(new GridLayout(0, 1, 5, 5));
        panelMidButtons.add(speedLabel);
        panelMidButtons.add(speedField);
        panelMidButtons.add(inGainLabel);
        panelMidButtons.add(inGainField);
        panelMidButtons.add(outGainLabel);
        panelMidButtons.add(outGainField);
        panelMidButtons.add(delayLabel);
        panelMidButtons.add(delayField);
        panelMidButtons.add(decayLabel);
        panelMidButtons.add(decayField);

        panelMid.setLayout(new BoxLayout(panelMid, BoxLayout.Y_AXIS));
        panelMid.add(pathLabel);
        panelMid.add(reverbLabel);
        panelMid.add(panelMidButtons);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(panelMid, BorderLayout.CENTER);
    }
    
    
    public static String getFile(JFrame frame){
        
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("mp3", "wav", "aac", "flac", "ogg", "m4a", "wma", "alac", "opus", "amr");
        fileChooser.setFileFilter(filter);
        fileChooser.showOpenDialog(frame);
        File file = fileChooser.getSelectedFile();
        return file.getPath();
        
    }

    public static double[] getReverbValues(int percentageValue){
        int delay = 120;
        double decay = 0.5, wet = percentageValue/100, dry = 1 - wet;
        double[] reverbValues = {dry, wet, delay, decay};
        return reverbValues;
    }

    public static double getSpeedValue(int percentageValue){
        double speed = percentageValue/100.0;
        return speed;
    }

    public static String getCodec(String inputPath){
        
        String codec;
        if (inputPath.endsWith(".wav")) codec = "pcm_s16le";
        else if (inputPath.endsWith(".mp3")) codec = "libmp3lame";
        else if (inputPath.endsWith(".m4a") || inputPath.endsWith(".aac")) codec = "aac";
        else if (inputPath.endsWith(".flac")) codec = "flac";
        else if (inputPath.endsWith(".ogg")) codec = "libvorbis";
        else if (inputPath.endsWith(".opus")) codec = "libopus";
        else if (inputPath.endsWith(".alac")) codec = "alac";
        else if (inputPath.endsWith(".amr")) codec = "libopencore_amrnb";
        else if (inputPath.endsWith(".wma")) codec = "wmav2";
        else codec = "aac"; // fallback

        return codec;
    }

    public static void setFile(JFrame frame, String filePath, double[] reverbValues, double speedValue) throws IOException, InterruptedException{
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("output.wav"));
        FileFilter filter = new FileNameExtensionFilter("WAV files", "wav");
        fileChooser.setFileFilter(filter);
        fileChooser.showSaveDialog(frame);
        File selectedChooser = fileChooser.getSelectedFile();
        String pathSelected = selectedChooser.getAbsolutePath();
        String filterCmd = "atempo=" + speedValue + ",aecho=" + reverbValues[0] + ":" + reverbValues[1] + ":" + reverbValues[2] + ":" + reverbValues[3];
        
        String[] cmd = {"ffmpeg",
        "-i", filePath,
        "-filter:a", filterCmd,
        "-c:a", getCodec(pathSelected),
        pathSelected
        };

        System.out.println(Arrays.toString(cmd));

        new ProcessBuilder(cmd).inheritIO().start().waitFor();
    }

   }
