/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Bean;


public class Docente {
    
    private String nome, cognome, email, telefono;

    
    public Docente(String nome, String cognome, String email) {

        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }
    
    public Docente(String nome, String cognome, String email, String telefono) {

        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }    
    
     public String getTelefono() {
        return email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }    

    @Override
    public String toString() {
        return "Docente{" + "nome=" + nome + ", cognome=" + cognome + ", email=" + email + '}';
    }
    
    
    
}
