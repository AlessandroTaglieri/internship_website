/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Bean;

import java.io.InputStream;
import java.time.LocalDate;


public class Tirocinio {
    
    private Annuncio annuncio;
    private LocalDate dataInizio, dataFine;
    private int crediti;
    private Studente studente;
    private Resoconto resoconto;
    InputStream file;
    
    public Tirocinio(Resoconto resoconto) {
        this.resoconto = resoconto;
    }
    
    public Tirocinio (Studente studente){
        this.studente = studente;
    }

    public Tirocinio(LocalDate dataInizio, LocalDate dataFine) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }
    
    public Tirocinio(LocalDate dataInizio, LocalDate dataFine, int crediti) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.crediti = crediti;
    }

    public Tirocinio(Resoconto resoconto, Annuncio annuncio, LocalDate dataInizio, LocalDate dataFine) {
       this.resoconto=resoconto;
       this.annuncio=annuncio;
       this.dataInizio=dataInizio;
       this.dataFine = dataFine;
    }
    
     public Tirocinio(LocalDate dataInizio, LocalDate dataFine, int crediti, Resoconto resoconto,Studente studente, Annuncio annuncio) {
       this.resoconto=resoconto;
       this.studente=studente;
       this.annuncio=annuncio;
       this.dataInizio=dataInizio;
       this.dataFine = dataFine;
       this.crediti = crediti;
    }

    public Tirocinio(Studente s, Annuncio an, Resoconto r) {
        this.studente=s;
        this.annuncio=an;
        this.resoconto=r;
    }
    
        public Tirocinio(Studente s, Annuncio an) {
        this.studente=s;
        this.annuncio=an;
    }
        
         public Tirocinio(Studente s, Annuncio an, InputStream file) {
        this.studente=s;
        this.annuncio=an;
        this.file = file;
    }
        
    public Tirocinio(Studente s, Annuncio an, LocalDate dataInizio, LocalDate dataFine) {
         this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.studente=s;
        this.annuncio=an;
    }
    
    public Tirocinio(Studente s, Annuncio an, LocalDate dataInizio, LocalDate dataFine, int crediti) {
        this.crediti = crediti;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.studente=s;
        this.annuncio=an;
    }

    public Tirocinio() {
    }

    public int getCrediti() {
        return crediti;
    }

    public void setCrediti(int crediti) {
        this.crediti = crediti;
    }

    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    public Resoconto getResoconto() {
        return resoconto;
    }

    public void setIdResoconto(Resoconto resoconto) {
        this.resoconto = resoconto;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    @Override
    public String toString() {
        return "Tirocinio{" + "annuncio=" + annuncio + ", dataInizio=" + dataInizio + ", dataFine=" + dataFine + ", studente=" + studente + ", resoconto=" + resoconto + '}';
    }
    
    public long getIdStud(Studente studente){
        long id = studente.getId();
        return id;
    }
    
    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }
    
}
