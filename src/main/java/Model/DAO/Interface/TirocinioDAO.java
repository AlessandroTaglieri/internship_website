/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Model.DAO.Interface;

import Framework.data.DataLayerException;
import Model.Bean.Annuncio;
import Model.Bean.Resoconto;
import Model.Bean.Studente;
import Model.Bean.Tirocinio;
import java.io.InputStream;
import java.util.List;


public interface TirocinioDAO {
    
    public List<Tirocinio> getTirocini(long idTirocinante) throws DataLayerException;
    
    public int setValutazione(int valutazione, long idResoconto) throws DataLayerException;
    
    public InputStream downloadResoconto(Resoconto resoconto) throws DataLayerException;
    
    public int uploadResoconto(Resoconto resoconto) throws DataLayerException;
    
    public Tirocinio getTirocinio(long idStudente) throws DataLayerException;
    
    public int setProgettoFormativoDoc(InputStream progettoFormativo, Tirocinio tirocinio) throws DataLayerException;
    
    public InputStream getProgettoFormativo(Studente studente) throws DataLayerException;
}
