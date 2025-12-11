import java.awt.BorderLayout;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SlowedAndReverbed {
    
    public static void main(String[] args){
        JFrame frame = new JFrame("SlowedAndReverbed");
        JPanel panel = new JPanel();
        JPanel panelMid = new JPanel();
        JButton newButton = new JButton("Select file");
        JButton saveButton = new JButton("Save file");
        JSlider sliderSpeed = new JSlider(0, 200, 100);
        JSlider sliderReverb = new JSlider(0, 100, 0);
        JLabel pathLabel = new JLabel("No file selected");
        JLabel speedLabel = new JLabel("Speed: 100%");
        JLabel reverbLabel = new JLabel("Reverb: 0%");

        newButton.addActionListener(e -> {
            String filePath = getFile(frame);
            String pathLabelText = new java.io.File(filePath).getName(); //Object has no references after, garbage collector kills it.
            pathLabel.setText(pathLabelText);
            newButton.setText("Select new file");
        });

        sliderSpeed.addChangeListener(e -> {
            String valueSpeed = String.valueOf(sliderSpeed.getValue());
            speedLabel.setText("Speed: " + valueSpeed + "%");
        });
        
        sliderReverb.addChangeListener(e -> {
            String valueReverb = String.valueOf(sliderReverb.getValue());
            reverbLabel.setText("Reverb: " + valueReverb + "%");
        });


        frame.setSize(400,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());

        sliderSpeed.setPaintTicks(true);
        sliderSpeed.setPaintLabels(true);

        panel.add(newButton);
        panel.add(saveButton);
        panelMid.setLayout(new BoxLayout(panelMid, BoxLayout.Y_AXIS));
        panelMid.add(pathLabel);
        panelMid.add(sliderSpeed);
        panelMid.add(speedLabel);
        panelMid.add(sliderReverb);
        panelMid.add(reverbLabel);
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
    
   

   }
