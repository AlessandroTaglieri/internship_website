/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO.Interface;

import Framework.data.DataLayerException;
import Model.Bean.Studente;


public interface StudenteDAO {

    public int setRegistrazioneStudente(Studente studente) throws DataLayerException;

    public Studente getStudente(long id) throws DataLayerException;
    
     public Studente getStudenteForPdf(long id) throws DataLayerException;

}
