/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;
import java.util.ArrayList;

/**
 *
 * @author shivanginagar
 */
public class EncounterHistory {
    ArrayList<Encounter> encounterList;
 
    public ArrayList<Encounter> getEncounterList() {
        return encounterList;
    }
 
    public void setEncounterList(ArrayList<Encounter> encounterList) {
        this.encounterList = encounterList;
    }
 
    public EncounterHistory() {
        encounterList = new ArrayList<Encounter>();
    }
 
    public void addEncounter(Encounter encounter) {
        this.encounterList.add(encounter);
    }
    
}
