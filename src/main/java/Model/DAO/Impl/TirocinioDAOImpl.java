/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO.Impl;

import Model.Bean.Annuncio;
import Model.Bean.Azienda;
import Model.Bean.Convenzione;
import Model.Bean.Resoconto;
import Model.Bean.Tirocinio;
import Model.Bean.Docente;
import Framework.data.DB;
import Framework.data.DataLayerException;
import Model.Bean.Referente;
import Model.Bean.Studente;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import Model.DAO.Interface.TirocinioDAO;


public class TirocinioDAOImpl implements TirocinioDAO {

    private static final String GET_INFO_TIROCINIO = "SELECT Tirocinio.dataInizio, Tirocinio.dataFine, Tirocinio.Resoconto_idResoconto,Resoconto.nome,Resoconto.valutazione,Azienda.ragSociale, Azienda.indirizzoSede, Azienda.citta, Azienda.nomeResponsabile, Azienda.cognomeResponsabile, Azienda.emailResponsabile, Azienda.telResponsabile, Annuncio.nomeDocente, Annuncio.cognomeDocente, Annuncio.emailDocente FROM Tirocinio JOIN Annuncio ON Tirocinio.Annuncio_idAnnuncio = Annuncio.idAnnuncio LEFT JOIN Resoconto ON Tirocinio.Resoconto_idResoconto = Resoconto.idResoconto JOIN Azienda ON Annuncio.Azienda_idAzienda = Azienda.idAzienda WHERE Tirocinio.Studente_idStudente=?";

    private static final String SET_VALUTAZIONE = "UPDATE Resoconto SET valutazione=? WHERE idResoconto=?";

    private static final String DOWNLOAD_RESOCONTO = "SELECT Resoconto.file FROM Resoconto WHERE idResoconto=?";

    private static final String UPLOAD_RESOCONTO = "UPDATE Resoconto SET Nome= ?, File=?, Estensione=?, Peso=? WHERE idResoconto=?;";

    private static final String GET_TIROCINIO ="SELECT * FROM Tirocinio JOIN Annuncio ON Tirocinio.Annuncio_idAnnuncio = Annuncio.idAnnuncio JOIN Studente ON Tirocinio.Studente_idStudente = Studente.idStudente JOIN Resoconto ON Tirocinio.Resoconto_idResoconto = Resoconto.idResoconto JOIN Azienda ON Annuncio.Azienda_idAzienda = Azienda.idAzienda WHERE Tirocinio.Studente_idStudente=?";
   
    private static final String UPLOAD_FORMATIVO = "UPDATE Tirocinio SET formativo_doc=? WHERE Annuncio_idAnnuncio = ? AND Studente_idStudente=?";
    
    private static final String GET_PROGETTO_FORMATIVO = "SELECT formativo_doc FROM Tirocinio WHERE Studente_idStudente = ?";    
    @Override
    public List<Tirocinio> getTirocini(long idStudente) throws DataLayerException {
        List<Tirocinio> tirocini = new ArrayList();
        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(GET_INFO_TIROCINIO)) {
                ps.setLong(1, idStudente);
                try (ResultSet rset = ps.executeQuery()) {

                    while (rset.next()) {
                        Resoconto resocontoAnnuncio = new Resoconto(rset.getInt("Tirocinio.Resoconto_idResoconto"), rset.getString("Resoconto.nome"), rset.getInt("Resoconto.valutazione"));
                        Docente docenteAnnuncio = new Docente(rset.getString("Annuncio.nomeDocente"), rset.getString("Annuncio.cognomeDocente"), rset.getString("Annuncio.emailDocente"));
                        Azienda aziendaAnnuncio = new Azienda(rset.getString("ragSociale"), rset.getString("indirizzoSede"), rset.getString("citta"), rset.getString("nomeResponsabile"), rset.getString("cognomeResponsabile"), rset.getString("emailResponsabile"), rset.getString("telResponsabile"));
                        Annuncio annuncio = new Annuncio(aziendaAnnuncio, docenteAnnuncio);
                        tirocini.add(new Tirocinio(resocontoAnnuncio, annuncio, rset.getDate("Tirocinio.dataInizio").toLocalDate(), rset.getDate("Tirocinio.dataFine").toLocalDate()));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("GET TIROCINI", ex);
        }

        return tirocini;
    }
    
   

    @Override
    public int setValutazione(int valutazione, long idResoconto) throws DataLayerException {

        int result = -1;
        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SET_VALUTAZIONE)) {

                //Prepare query
                ps.setInt(1, valutazione);
                ps.setLong(2, idResoconto);

                result = ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataLayerException("SAVE VALUTAZIONE", ex);
        }
        return result;
    }

    @Override
    public InputStream downloadResoconto(Resoconto resoconto) throws DataLayerException {
        InputStream inputStream = null;

        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(DOWNLOAD_RESOCONTO)) {
                //Prepare query
                ps.setLong(1, resoconto.getId());

                try (ResultSet rset = ps.executeQuery()) {
                    if (rset.next()) {
                        Blob blob = rset.getBlob("file");
                        inputStream = blob.getBinaryStream();
                    }
                }
            }

        } catch (SQLException e) {
            throw new DataLayerException("DOWNLOAD RESOCONTO", e);
        }

        return inputStream;
    }

    @Override
    public int uploadResoconto(Resoconto resoconto) throws DataLayerException {

        int result = -1;
        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(UPLOAD_RESOCONTO)) {

                //Prepare query
                ps.setString(1, resoconto.getNome());
                ps.setBlob(2, resoconto.getFile());
                ps.setString(3, resoconto.getEstensione());
                ps.setLong(4, resoconto.getPeso());
                ps.setLong(5, resoconto.getId());

                result = ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataLayerException("UPLOAD RESOCONTO", ex);
        }

        return result;
    }
    
    public int setProgettoFormativo(InputStream progettoFormativo, Resoconto resoconto) throws DataLayerException{
        int result = -1;
        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(UPLOAD_RESOCONTO)) {

                //Prepare query
                ps.setString(1, resoconto.getNome());
                ps.setBlob(2, resoconto.getFile());
                ps.setString(3, resoconto.getEstensione());
                ps.setLong(4, resoconto.getPeso());
                ps.setLong(5, resoconto.getId());

                result = ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataLayerException("UPLOAD RESOCONTO", ex);
        }

        return result;
    }
    
    public Tirocinio getTirocinio(long idStudente) throws DataLayerException{
        
        Tirocinio tirocinio = null;
        
        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(GET_TIROCINIO)) {
                ps.setLong(1, idStudente);
                try (ResultSet rset = ps.executeQuery()) {
                    while (rset.next()) {
                        Resoconto resocontoAnnuncio = new Resoconto(rset.getInt("Tirocinio.Resoconto_idResoconto"), rset.getString("Resoconto.nome"), rset.getInt("Resoconto.valutazione"));
                        Studente studente = new Studente(rset.getInt("Tirocinio.Studente_idStudente"), rset.getString("Studente.nome"), rset.getString("Studente.cognome"), rset.getString("Studente.codFiscale"),
                                rset.getString("Studente.telefono"), rset.getBoolean("Studente.handicap"), rset.getDate("Studente.dataNascita").toLocalDate(), rset.getString("Studente.indirizzoResidenza"), rset.getString("Studente.corsoLaurea"), rset.getString("Studente.diploma"), rset.getString("Studente.laurea"), rset.getString("Studente.dottorato"),
                                rset.getString("Studente.cap_nascita"), rset.getString("Studente.citta_Nascita"), rset.getString("Studente.provincia_nascita"), rset.getString("Studente.cap_residenza"), rset.getString("Studente.citta_residenza"), rset.getString("Studente.provincia_residenza") );
                        Docente docenteAnnuncio = new Docente(rset.getString("Annuncio.nomeDocente"), rset.getString("Annuncio.cognomeDocente"), rset.getString("Annuncio.emailDocente"));
                        Azienda aziendaAnnuncio = new Azienda(rset.getString("Azienda.ragSociale"), rset.getString("Azienda.indirizzoSede"), rset.getString("Azienda.citta"), rset.getString("Azienda.nomeResponsabile"), rset.getString("Azienda.cognomeResponsabile"), rset.getString("Azienda.emailResponsabile"), rset.getString("Azienda.telResponsabile"));
                        Annuncio annuncio = new Annuncio(aziendaAnnuncio, docenteAnnuncio, rset.getString("Annuncio.titolo"), rset.getString("Annuncio.corpo") ,rset.getDate("Annuncio.dataAvvio").toLocalDate(), rset.getDate("Annuncio.dataTermine").toLocalDate(), rset.getString("Annuncio.modalita"), rset.getString("Annuncio.settore"), rset.getString("Annuncio.sussidio"));
                        tirocinio = new Tirocinio(rset.getDate("Tirocinio.dataInizio").toLocalDate(), rset.getDate("Tirocinio.dataFine").toLocalDate(), rset.getInt("Tirocinio.crediti"), resocontoAnnuncio,studente, annuncio);
                        
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("GET TIROCINI", ex);
        }
        
        return tirocinio;
    }


    
    public int setProgettoFormativoDoc(InputStream progettoFormativo, Tirocinio tirocinio) throws DataLayerException{
        int result = 0;
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = DB.getConnection(); // CREAZIONE CONNESSIONE

            ps = connection.prepareStatement(UPLOAD_FORMATIVO);

            ps.setBlob(1, progettoFormativo);
            ps.setLong(2, tirocinio.getAnnuncio().getId());
            ps.setLong(3, tirocinio.getStudente().getId());

            result = ps.executeUpdate();
    } catch (SQLException ex) {
            throw new DataLayerException("ERRORE UPDATE STATO TIROCINIO", ex);
        } finally {
            //CHIUSURA CONNESSIONE
            try {
                ps.close();
                connection.close();
            } catch (SQLException ex) {
                throw new DataLayerException("ERRORE CHIUSURA CONNESSIONE", ex);
            }
        }
        return result;
    }
    
    
    public InputStream getProgettoFormativo(Studente studente) throws DataLayerException{
        PreparedStatement ps;
        InputStream docStream = null;
       
        try (Connection connection = DB.getConnection()) {
            ps = connection.prepareStatement(GET_PROGETTO_FORMATIVO);
            
            ps.setLong(1, studente.getId());
            
            ResultSet rset = ps.executeQuery();
            
            if (rset.next()) {
                
                Blob formativoDoc = rset.getBlob("formativo_doc");
                docStream = formativoDoc.getBinaryStream();
            }

            ps.close();
            connection.close();

        } catch (SQLException e) {
            throw new DataLayerException("Get modulo convenzione", e);
        }

        return docStream;
    }
            
}
