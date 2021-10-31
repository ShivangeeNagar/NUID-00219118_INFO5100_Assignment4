/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package userInteface.Patient;

import business.MyStringVerifier;
import business.Patient;
import business.Person;
import business.PersonDirectory;
import business.VitalSign;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.InputVerifier;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author shivanginagar
 */
public class ManageVitalSignsJPanel extends javax.swing.JPanel {

    /**
     * Creates new form VitalSignManagerJJPanel
     */
    private PersonDirectory personDirectory;
    private JPanel userProcessContainer;

    public ManageVitalSignsJPanel(JPanel userProcessContainer, PersonDirectory personDirectory) {
        initComponents();
        this.userProcessContainer = userProcessContainer;
        this.personDirectory = personDirectory;
        InputVerifier stringVerifier = new MyStringVerifier();
        searchBoxJTextField.setInputVerifier(stringVerifier);
        ArrayList<Person> personList = personDirectory.getPersonHistory();
        populatePatientsTable(personList);
        populateVitalSignTable(null);
        populateAbnormalCommunityTable(new ArrayList<Person>(), null);
        populateCommunities(personList);
    }

    private void populatePatientsTable(ArrayList<Person> personList) {
        DefaultTableModel model = (DefaultTableModel) viewPatientsJTable.getModel();
        model.setRowCount(0);
        if (personList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Persons found. Please add Persons",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        for (Person person : personList) {
            Object[] row = new Object[4];
            row[0] = person;
            row[1] = person.getAge();
            row[3] = person.getCommunity2().getName();
            if (person.getPatient() != null) {
                row[2] = person.getPatient().getPatientID();
            } else {
                row[2] = "Patient Not Created";
            }

            model.addRow(row);
        }
    }

    private void populateVitalSignTable(Person person) {

        DefaultTableModel model = (DefaultTableModel) viewVitalSignsJTable.getModel();
        model.setRowCount(0);
        if (person != null) {
            int patientAge = person.getAge();
            ArrayList<VitalSign> vitalSignList = person.getPatient().getVitalSignHistory().getHistory();

            if (vitalSignList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No vital signs found. Please"
                        + " add vital signs", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            for (VitalSign vitalSign : vitalSignList) {
                Object[] row = new Object[2];
                row[0] = vitalSign;
                row[1] = VitalSignStatus(patientAge, vitalSign);
                model.addRow(row);
            }
        }
    }

    private void populateCommunities(ArrayList<Person> personList) {
        ArrayList<String> communities = new ArrayList<String>();
        for (Person person : personList) {
            communities.add(person.getCommunity2().getName());
        }
        Set<String> uniqueCommunities = new HashSet<String>(communities);
        for (String str : uniqueCommunities) {
            CBCommunity.addItem(str);
        }
    }

    private void populateAbnormalCommunityTable(ArrayList<Person> personList, String community) {

        DefaultTableModel model = (DefaultTableModel) viewAbnormalCommunityJTable.getModel();
        model.setRowCount(0);
        for (Person person : personList) {
            if (person != null) {
                if (!person.getCommunity2().getName().equalsIgnoreCase(community)) {
                    continue;
                }
                int patientAge = person.getAge();
                ArrayList<VitalSign> vitalSignList = person.getPatient().getVitalSignHistory().getHistory();
                if (vitalSignList.isEmpty()) {
                    continue;
                }
                String vitalSignStatus = VitalSignStatus(patientAge, vitalSignList.get(vitalSignList.size() - 1));
                if ("Abnormal".equalsIgnoreCase(vitalSignStatus)) {
                    Object[] row = new Object[2];
                    row[0] = community;
                    row[1] = vitalSignStatus;
                    model.addRow(row);
                }
            }
        }
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No persons found", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }

    private String VitalSignStatus(int patientAge, VitalSign vitalSign) {
        String vitalSignStatus = "Normal";

        int respirationRate = vitalSign.getRespiratoryRate();
        int heartRate = vitalSign.getHeartRate();
        int bloodPressure = vitalSign.getBloodPressure();
        float weight = vitalSign.getWeight();

        /*Toddler*/
        if (patientAge >= 1 && patientAge <= 3) {
            if ((respirationRate < 20 || respirationRate > 30) /*Respiration Rate*/
                    || (heartRate < 80 || heartRate > 130) /*Heart Rate*/
                    || (bloodPressure < 80 || bloodPressure > 110) /*Blood Pressure*/
                    || (weight < 22 || weight > 31)) /*Weight*/ {
                vitalSignStatus = "Abnormal";
            }
        }
        /*Preschooler*/
        if (patientAge >= 4 && patientAge <= 5) {
            if ((respirationRate < 20 || respirationRate > 30)
                    || (heartRate < 80 || heartRate > 120)
                    || (bloodPressure < 80 || bloodPressure > 110)
                    || (weight < 31 || weight > 40)) {
                vitalSignStatus = "Abnormal";
            }
        }
        /*School Age*/
        if (patientAge >= 6 && patientAge <= 12) {
            if ((respirationRate < 20 || respirationRate > 30)
                    || (heartRate < 70 || heartRate > 110)
                    || (bloodPressure < 80 || bloodPressure > 120)
                    || (weight < 41 || weight > 92)) {
                vitalSignStatus = "Abnormal";
            }
        }
        /*Adolescent*/
        if (patientAge >= 13) {
            if ((respirationRate < 12 || respirationRate > 20)
                    || (heartRate < 55 || heartRate > 105)
                    || (bloodPressure < 110 || bloodPressure > 120)
                    || (weight < 110)) {
                vitalSignStatus = "Abnormal";
            }
        }
        return vitalSignStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayDetailsJButton = new javax.swing.JButton();
        editVitalSignsJButton = new javax.swing.JButton();
        addVitalSignsJButton = new javax.swing.JButton();
        deleteVitalSignJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        viewPatientsJTable = new javax.swing.JTable();
        backJButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        viewVitalSignsJTable = new javax.swing.JTable();
        viewVitalSignJButton = new javax.swing.JButton();
        refreshVitalSignsJButton = new javax.swing.JButton();
        searchBoxJTextField = new javax.swing.JTextField();
        searchCommunityJButton = new javax.swing.JButton();
        refreshPatientsJButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        viewAbnormalCommunityJTable = new javax.swing.JTable();
        viewVitalSignJButton1 = new javax.swing.JButton();
        CBCommunity = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(204, 255, 255));
        setMinimumSize(new java.awt.Dimension(500, 700));
        setPreferredSize(new java.awt.Dimension(500, 700));

        displayDetailsJButton.setText("Display Status");
        displayDetailsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayDetailsJButtonActionPerformed(evt);
            }
        });

        editVitalSignsJButton.setText("Edit Vital Signs");
        editVitalSignsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editVitalSignsJButtonActionPerformed(evt);
            }
        });

        addVitalSignsJButton.setText("Add Vital Signs");
        addVitalSignsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addVitalSignsJButtonActionPerformed(evt);
            }
        });

        deleteVitalSignJButton.setText("Delete Vital Signs");
        deleteVitalSignJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteVitalSignJButtonActionPerformed(evt);
            }
        });

        viewPatientsJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Patient Name", "Age", "Patient ID", "Community"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(viewPatientsJTable);

        backJButton.setText("<- Previous");
        backJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backJButtonActionPerformed(evt);
            }
        });

        viewVitalSignsJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Timestamp", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(viewVitalSignsJTable);
        if (viewVitalSignsJTable.getColumnModel().getColumnCount() > 0) {
            viewVitalSignsJTable.getColumnModel().getColumn(0).setResizable(false);
            viewVitalSignsJTable.getColumnModel().getColumn(1).setResizable(false);
        }

        viewVitalSignJButton.setText("View Vital Signs");
        viewVitalSignJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewVitalSignJButtonActionPerformed(evt);
            }
        });

        refreshVitalSignsJButton.setText("Refresh Vital Signs");
        refreshVitalSignsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshVitalSignsJButtonActionPerformed(evt);
            }
        });

        searchCommunityJButton.setText("Search Community");
        searchCommunityJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCommunityJButtonActionPerformed(evt);
            }
        });

        refreshPatientsJButton.setText("Refresh Patients");
        refreshPatientsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshPatientsJButtonActionPerformed(evt);
            }
        });

        viewAbnormalCommunityJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Community", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(viewAbnormalCommunityJTable);
        if (viewAbnormalCommunityJTable.getColumnModel().getColumnCount() > 0) {
            viewAbnormalCommunityJTable.getColumnModel().getColumn(0).setResizable(false);
            viewAbnormalCommunityJTable.getColumnModel().getColumn(1).setResizable(false);
        }

        viewVitalSignJButton1.setText("Search Abnormal Pateints by Community");
        viewVitalSignJButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewVitalSignJButton1ActionPerformed(evt);
            }
        });

        CBCommunity.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        CBCommunity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBCommunityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(viewVitalSignJButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CBCommunity, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(viewVitalSignJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(editVitalSignsJButton)
                                        .addGap(12, 12, 12)
                                        .addComponent(deleteVitalSignJButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(backJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(refreshPatientsJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(searchCommunityJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(searchBoxJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(displayDetailsJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(addVitalSignsJButton)))))
                                .addGap(0, 55, Short.MAX_VALUE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(485, 485, 485))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(refreshVitalSignsJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(displayDetailsJButton)
                    .addComponent(backJButton)
                    .addComponent(searchCommunityJButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchBoxJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addVitalSignsJButton)
                    .addComponent(refreshPatientsJButton))
                .addGap(44, 44, 44)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewVitalSignJButton)
                    .addComponent(editVitalSignsJButton)
                    .addComponent(deleteVitalSignJButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(refreshVitalSignsJButton)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewVitalSignJButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBCommunity, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backJButtonActionPerformed
        // TODO add your handling code here:
        userProcessContainer.remove(this);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.previous(userProcessContainer);
    }//GEN-LAST:event_backJButtonActionPerformed

    private void viewVitalSignJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewVitalSignJButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = viewVitalSignsJTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row from table.",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        VitalSign vitalSign = (VitalSign) viewVitalSignsJTable.getValueAt(selectedRow, 0);
        ViewUpdateVitalSignsJPanel vuvsJPanel = new ViewUpdateVitalSignsJPanel(userProcessContainer,
                vitalSign, Boolean.FALSE);
        userProcessContainer.add("vuvsJPanel", vuvsJPanel);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.next(userProcessContainer);
    }//GEN-LAST:event_viewVitalSignJButtonActionPerformed

    private void editVitalSignsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editVitalSignsJButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = viewVitalSignsJTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row from table.",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        VitalSign vitalSign = (VitalSign) viewVitalSignsJTable.getValueAt(selectedRow, 0);

        ViewUpdateVitalSignsJPanel vuvsJPanel = new ViewUpdateVitalSignsJPanel(userProcessContainer,
                vitalSign, Boolean.TRUE);
        userProcessContainer.add("vuvsJPanel", vuvsJPanel);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.next(userProcessContainer);
    }//GEN-LAST:event_editVitalSignsJButtonActionPerformed

    private void addVitalSignsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addVitalSignsJButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = viewPatientsJTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row from table.",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Person person = (Person) viewPatientsJTable.getValueAt(selectedRow, 0);
        Patient patient = person.getPatient();
        if (patient != null) {
            CreateVitalSignJPanel cvsJPanel = new CreateVitalSignJPanel(userProcessContainer, patient);
            userProcessContainer.add("cvsJPanel", cvsJPanel);
            CardLayout layout = (CardLayout) userProcessContainer.getLayout();
            layout.next(userProcessContainer);
        } else {
            JOptionPane.showMessageDialog(this, "Patient not created, Please create"
                    + " Patient first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addVitalSignsJButtonActionPerformed

    private void displayDetailsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayDetailsJButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = viewPatientsJTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row from table.",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Person person = (Person) viewPatientsJTable.getValueAt(selectedRow, 0);
        Patient patient = person.getPatient();
        if (patient != null) {
            populateVitalSignTable(person);
        } else {
            JOptionPane.showMessageDialog(this, "Patient not created, Please create "
                    + "Patient first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_displayDetailsJButtonActionPerformed

    private void deleteVitalSignJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteVitalSignJButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = viewPatientsJTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row from table.");
            return;
        }
        Person person = (Person) viewPatientsJTable.getValueAt(selectedRow, 0);
        Patient patient = person.getPatient();
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Patient not created, Please create"
                    + " Patient first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedRow = viewVitalSignsJTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row from table.",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        VitalSign vitalSign = (VitalSign) viewVitalSignsJTable.getValueAt(selectedRow, 0);

        int flag = JOptionPane.showConfirmDialog(this, "Are you sure want to remove?",
                "Warning", JOptionPane.YES_NO_OPTION);
        if (flag == 0) {
            patient.getVitalSignHistory().deleteVitalSign(vitalSign);
            refreshVialSigns();
        }
    }//GEN-LAST:event_deleteVitalSignJButtonActionPerformed

    private void refreshVitalSignsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshVitalSignsJButtonActionPerformed
        // TODO add your handling code here:
        refreshVialSigns();
    }//GEN-LAST:event_refreshVitalSignsJButtonActionPerformed

    private void searchCommunityJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCommunityJButtonActionPerformed
        // TODO add your handling code here:
        String key = searchBoxJTextField.getText().trim();
        if (key.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter key.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        /*Storing searched patients in an array to display in table.*/
        ArrayList<Person> searchPatients = personDirectory.searchPatient(key);
        populatePatientsTable(searchPatients);
    }//GEN-LAST:event_searchCommunityJButtonActionPerformed

    private void refreshPatientsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshPatientsJButtonActionPerformed
        // TODO add your handling code here:
        searchBoxJTextField.setText("");
        populatePatientsTable(personDirectory.getPersonHistory());
    }//GEN-LAST:event_refreshPatientsJButtonActionPerformed

    private void viewVitalSignJButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewVitalSignJButton1ActionPerformed
        if (CBCommunity.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No community selected", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String community = CBCommunity.getSelectedItem().toString();
        populateAbnormalCommunityTable(personDirectory.getPersonHistory(), community);
    }//GEN-LAST:event_viewVitalSignJButton1ActionPerformed

    private void CBCommunityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBCommunityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBCommunityActionPerformed
    private void refreshVialSigns() {
        int selectedRow = viewPatientsJTable.getSelectedRow();
        if (selectedRow < 0) {
            populateVitalSignTable(null);
        } else {
            Person person = (Person) viewPatientsJTable.getValueAt(selectedRow, 0);
            Patient patient = person.getPatient();
            if (patient != null) {
                populateVitalSignTable(person);
            } else {
                populateVitalSignTable(null);
            }
        }
    }

    private void createChart() {
        DefaultCategoryDataset vitalSignDataset = new DefaultCategoryDataset();
        int selectedRow = viewPatientsJTable.getSelectedRow();
        Person person = (Person) viewPatientsJTable.getValueAt(selectedRow, 0);
        Patient patient = person.getPatient();
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Patient not created, Please create "
                    + "Patient first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<VitalSign> vitalSignList = patient.getVitalSignHistory().getHistory();
        /*At least 2 vital sign records needed to show chart */
        if (vitalSignList.isEmpty() || vitalSignList.size() == 1) {
            JOptionPane.showMessageDialog(this, "No vital signs or only one vital "
                    + "sign found. At least 2 vital sign records needed to show chart!",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        for (VitalSign vitalSign : vitalSignList) {
            vitalSignDataset.addValue(vitalSign.getRespiratoryRate(), "RR", vitalSign.getTimestamp());
            vitalSignDataset.addValue(vitalSign.getHeartRate(), "HR", vitalSign.getTimestamp());
            vitalSignDataset.addValue(vitalSign.getBloodPressure(), "BP", vitalSign.getTimestamp());
            vitalSignDataset.addValue(vitalSign.getWeight(), "WT", vitalSign.getTimestamp());
        }

        JFreeChart vitalSignChart = ChartFactory.createBarChart3D("Vital Sign Chart",
                "Time Stamp", "Rate", vitalSignDataset, PlotOrientation.VERTICAL, true, false, false);
        vitalSignChart.setBackgroundPaint(Color.white);
        CategoryPlot vitalSignChartPlot = vitalSignChart.getCategoryPlot();
        vitalSignChartPlot.setBackgroundPaint(Color.lightGray);

        CategoryAxis vitalSignDomainAxis = vitalSignChartPlot.getDomainAxis();
        vitalSignDomainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );

        NumberAxis vitalSignRangeAxis = (NumberAxis) vitalSignChartPlot.getRangeAxis();
        vitalSignRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartFrame chartFrame = new ChartFrame("Chart", vitalSignChart);
        chartFrame.setVisible(true);
        chartFrame.setSize(500, 500);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CBCommunity;
    private javax.swing.JButton addVitalSignsJButton;
    private javax.swing.JButton backJButton;
    private javax.swing.JButton deleteVitalSignJButton;
    private javax.swing.JButton displayDetailsJButton;
    private javax.swing.JButton editVitalSignsJButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton refreshPatientsJButton;
    private javax.swing.JButton refreshVitalSignsJButton;
    private javax.swing.JTextField searchBoxJTextField;
    private javax.swing.JButton searchCommunityJButton;
    private javax.swing.JTable viewAbnormalCommunityJTable;
    private javax.swing.JTable viewPatientsJTable;
    private javax.swing.JButton viewVitalSignJButton;
    private javax.swing.JButton viewVitalSignJButton1;
    private javax.swing.JTable viewVitalSignsJTable;
    // End of variables declaration//GEN-END:variables
}
