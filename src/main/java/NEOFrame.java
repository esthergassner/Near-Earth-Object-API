import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class NEOFrame extends JFrame
{
    private JLabel startDateLabel, endDateLabel;
    private JTextField startDateField, endDateField;
    private String startDate, endDate;
    private JButton go;
    private HashMap<String, List<NearEarthObject>> listHashMap;
    private JList list;
    private Font defFont = new Font("Arial",Font.PLAIN,26);
    private JPanel controlPanel, neoInfoPanel;
    private JTextArea neoInfo;
    private NearEarthObject currentNEO;
    private JSplitPane splitPane;
    private JButton diagramLinkButton;

    private NEOFrame()
    {
        NasaClient client = new NasaClient();

        setTitle("Near Earth Object Finder");
        setSize(2000, 1500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());

        setUpDateFields();
        setUpGoButton(client);
        setUpControlPanel();
        setUpLinkButton();
        setUpSplitPane();

        root.add(splitPane, BorderLayout.CENTER);
        root.add(controlPanel, BorderLayout.NORTH);
        setContentPane(root);
    }

    private void setUpDateFields()
    {
        startDateLabel = new JLabel("Starting Date:");
        startDateLabel.setFont(defFont);

        endDateLabel = new JLabel("Ending Date:");
        endDateLabel.setFont(defFont);

        startDateField = new JTextField();
        startDateField.setSize(14,5);
        startDateField.setFont(defFont);
        startDateField.setText(LocalDate.now().toString());

        endDateField = new JTextField();
        endDateField.setSize(14,5);
        endDateField.setFont(defFont);
        endDateField.setText(LocalDate.now().plusDays(1).toString());
    }

    private void setUpGoButton(NasaClient client)
    {
        go = new JButton();
        go.setBackground(new Color(0x0EF9EF));
        go.setText("Get Near-Earth-Objects");
        go.setFont(defFont);

        go.addActionListener(e -> {
            startDate = startDateField.getText();
            endDate = endDateField.getText();
                Disposable disposable = client.getObject(startDate, endDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.trampoline())
                    .doOnError(throwable -> {
                        JOptionPane.showMessageDialog(null,
                                "Start and end dates must be entered in the following format: 1900-01-01, and no more than 1 week apart.",
                                "Invalid Date Entry",
                                JOptionPane.ERROR_MESSAGE);
                    })
                    .subscribe(NEOs -> {
                        listHashMap = NEOs.getNear_earth_objects();
                        displayObjects();
                    });
        });
    }

    private void setUpControlPanel()
    {
        controlPanel = new JPanel();
        controlPanel.setBackground(new Color(0x781D57C4, true));
        controlPanel.add(startDateLabel);
        controlPanel.add(startDateField);
        controlPanel.add(endDateLabel);
        controlPanel.add(endDateField);
        controlPanel.add(go);
    }

    private void setUpLinkButton()
    {
        diagramLinkButton = new JButton("Link to Orbital Diagram");
        diagramLinkButton.setSize(100, 50);
        diagramLinkButton.setFont(defFont);
        diagramLinkButton.setBackground(new Color(0x781D57C4, true));
        diagramLinkButton.setLocation(500, 800);
        diagramLinkButton.setVisible(false);
        diagramLinkButton.addActionListener(e -> {
            try {
                String orbitalDiagramLink = "https://ssd.jpl.nasa.gov/sbdb.cgi?sstr=" + currentNEO.getId() + ";old=0;orb=1;cov=0;log=0;cad=0#orb";
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(orbitalDiagramLink));
            }
            catch (java.io.IOException exc) {
                System.out.println(exc.getMessage());
            }
        });
    }

    private void setUpSplitPane()
    {
        //list scroll pane
        list = new JList();
        ListCellRenderer renderer = new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                NearEarthObject neo = (NearEarthObject) value;
                setText(neo.getName());
                if (neo.isPotentiallyHazardous())
                {
                    setForeground(Color.RED);
                }
                return  this;  }
        };
        list.setCellRenderer(renderer);
        list.setFont(new Font("Arial",Font.BOLD,25));
        list.addListSelectionListener(e -> {
            currentNEO = (NearEarthObject) list.getSelectedValue();
                displayFurtherData();
        });
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setBackground(new Color(0x81AF67FE, true));
        listScroller.setPreferredSize(new Dimension(770, getHeight()));
        TitledBorder border = new TitledBorder("Enter start and end dates to display NEOs below.");
        border.setTitleFont(defFont);
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        listScroller.setBorder(border);

        //neoInfo scroll pane
        neoInfo = new JTextArea();
        neoInfo.setEditable(false);
        neoInfo.setLineWrap(true);
        JScrollPane neoInfoScroller = new JScrollPane(neoInfo);
        neoInfoScroller.setBackground(new Color(0x87941C9A, true));
        TitledBorder neoBorder = new TitledBorder("Detailed Description of Selected Near Earth Object");
        neoBorder.setTitleFont(defFont);
        neoBorder.setTitleJustification(TitledBorder.CENTER);
        neoInfoScroller.setBorder(neoBorder);

        neoInfoPanel = new JPanel();
        neoInfoPanel.setLayout(new BorderLayout());
        neoInfoPanel.add(neoInfoScroller, BorderLayout.CENTER);
        neoInfoPanel.add(diagramLinkButton, BorderLayout.SOUTH);

        //split pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroller, neoInfoPanel);
    }

    private void displayObjects()
    {
        DefaultListModel<Object> listModel = new DefaultListModel<>();

        listHashMap.forEach((key, value) -> {
            List<NearEarthObject> daysList = listHashMap.get(key);
            for (NearEarthObject NEO : daysList)
            {
                listModel.addElement(NEO);
            }
        });
        list.setModel(listModel);

    }

    private void displayFurtherData()
    {
        NearEarthObject.CloseApproachData cad = currentNEO.getClose_approach_data().get(0);
        String closeApproachDate;
        if (cad.getClose_approach_date_full() == null)
        {
            closeApproachDate = cad.getClose_approach_date();
        }
        else
        {
            closeApproachDate = cad.getClose_approach_date_full();
        }
        String velocity = cad.getRelative_velocity().getMiles_per_hour();
        String missDistance = cad.getMiss_distance().getMiles();

        diagramLinkButton.setVisible(true);
        neoInfo.setFont(new Font("Arial",Font.PLAIN,30));
        neoInfo.setText("Name: " + currentNEO.getName() + "\n" +
                "Reference Id: " + currentNEO.getId() + "\n\n" +
                "Estimated minimum diameter (feet): " + currentNEO.getEstimated_diameter().getFeet().getEstimated_diameter_min() + "\n" +
                "Estimated maximum diameter (feet): " + currentNEO.getEstimated_diameter().getFeet().getEstimated_diameter_max() + "\n" +
                "Date of close approach to Earth: " + closeApproachDate + "\n" +
                "Velocity (miles per hour): " + velocity + "\n" +
                "Missing Earth by this many miles: " + missDistance
                );


        if (currentNEO.isPotentiallyHazardous())
        {
            neoInfo.setFont(new Font("Arial",Font.BOLD,30));
            neoInfo.setForeground(Color.RED);
            neoInfo.append("\n\nThis is a potentially hazardous asteroid. It is recommended that you avoid this area on this date.");
        }
    }

    public static void main(String[] args)
    {
        NEOFrame frame = new NEOFrame();
        frame.setVisible(true);
    }
}
