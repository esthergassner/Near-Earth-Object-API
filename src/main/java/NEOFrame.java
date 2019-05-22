import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class NEOFrame extends JFrame
{
    private JLabel startDateLabel, endDateLabel;
    private JTextField startDateField, endDateField;
    private String startDate, endDate;
    private JButton go;
    private NEOResponse response;
    private HashMap<String, List<NearEarthObject>> listHashMap;

    public NEOFrame()
    {
        NasaClient client = new NasaClient();

        setTitle("Near Earth Object Finder");
        setSize(2000, 1500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());

        setUpDateFields();
        setUpButton(client);


        JPanel controlPanel = new JPanel();

        controlPanel.add(startDateLabel);
        controlPanel.add(startDateField);
        controlPanel.add(endDateLabel);
        controlPanel.add(endDateField);
        controlPanel.add(go);

        root.add(controlPanel);

        setContentPane(root);
    }


    private void setUpDateFields()
    {
        startDateLabel = new JLabel("Starting Date:");
        endDateLabel = new JLabel("Ending Date:");
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);
    }

    private void setUpButton(NasaClient client)
    {
        go = new JButton();
        go.setBackground(new Color(0x0EF9EF));
        go.setText("Get Near-Earth-Objects");
        go.addActionListener(e -> {
            startDate = startDateField.getText();
            endDate = endDateField.getText();
            Disposable disposable = client.getObject(startDate, endDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.trampoline())
                    .subscribe(NEOs -> {
                        response = NEOs;
                        listHashMap = response.getNear_earth_objects();
                        displayObjects();
                    });
        });
    }

    private void displayObjects()
    {
        listHashMap.forEach((key, value) -> {
            List<NearEarthObject> daysList = listHashMap.get(key);
            System.out.println(key + ":\n");
            for (NearEarthObject NEO : daysList)
            {
                System.out.println("\t" + NEO.getName());
            }
            //System.out.println(key + ":\t" + " Value: " + value );
        });
    }

    public static void main(String[] args)
    {
        NEOFrame frame = new NEOFrame();
        frame.setVisible(true);
    }
}
