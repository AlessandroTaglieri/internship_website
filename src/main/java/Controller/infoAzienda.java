/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Framework.data.DataLayerException;
import Framework.result.FailureResult;
import Framework.result.TemplateManagerException;
import Framework.result.TemplateResult;
import Framework.security.SecurityLayer;
import Framework.security.SecurityLayerException;
import Model.Bean.Azienda;
import Model.DAO.Impl.AmministratoreDAOImpl;
import Model.DAO.Impl.AziendaDAOImpl;
import Model.DAO.Interface.AmministratoreDAO;
import Model.DAO.Interface.AziendaDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author alessandro
 */
public class infoAzienda extends AmministratoreSecurity {

    private AziendaDAO aziendaDAO;
    private AmministratoreDAO ammDAO;

    public infoAzienda() {
        super();
        ammDAO = new AmministratoreDAOImpl();
        aziendaDAO = new AziendaDAOImpl();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_registrata(HttpServletRequest request, HttpServletResponse response, long idAzienda) throws IOException, TemplateManagerException, DataLayerException, DocumentException {
        Map data = new HashMap();
        Azienda azienda = new Azienda(idAzienda);
//        Azienda azienda2 = new AziendaDAOImpl().getAziendaByID(idAzienda);

        if (request.getParameter("approva") != null) {
            //Action azienda approvata
            
            
            
            response.addHeader("Content-Disposition", "inline; filename=convenzione.pdf");
            response.setContentType("application/pdf");

            
            TemplateResult res = new TemplateResult(getServletContext());
            Azienda azienda2 = new AziendaDAOImpl().getAziendaByID(idAzienda);
            request.setAttribute("azienda", azienda2);
            String schemaConvenzione = res.getFilledTemplate("downloadConvenzione.ftl.html", request);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            //generating pdf
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(schemaConvenzione.getBytes(StandardCharsets.UTF_8)));
            document.close();
           
            ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(baos.toByteArray());
            
           
            new AziendaDAOImpl().setConvenzioneDOC(pdfInputStream, azienda);
            
            
            
            aziendaDAO.updateStato(new Azienda(idAzienda), "APPROVATA");
       

            s.setAttribute("message", "Azienda approvata correttamente");
            response.sendRedirect("homepage");

        } else if (request.getParameter("rifiuta") != null) {
            //Action azienda rifiutata
            aziendaDAO.removeAzienda(azienda);

            s.setAttribute("message", "Azienda rimossa correttamente");
            response.sendRedirect("homepage");
        } else {
            //Action default
            azienda = aziendaDAO.getAzienda(azienda);

            //TEMPLATE
            data.put("azienda", azienda);
            data.put("outline_tpl", "");//rimozione outline
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("approvazioneAzienda.ftl.html", data, response);

        }
    }

    private void action_convenzioanta(HttpServletRequest request, HttpServletResponse response, long idAzienda) throws IOException, TemplateManagerException, ServletException, DataLayerException {
        Map data = new HashMap();
        Azienda azienda = new Azienda(idAzienda);

        //Action default
        azienda = aziendaDAO.getAzienda(azienda);

        //TEMPLATE
        data.put("azienda", azienda);
        data.put("outline_tpl", "");//rimozione outline
        
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("convenzioneAzienda.ftl.html", data, response);

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action != null) {

                long idAzienda = SecurityLayer.issetInt(request.getParameter("refA"));

                switch (action) {
                    case "registrata":
                        action_registrata(request, response, idAzienda);
                        break;
                    case "convenzionata":
                        action_convenzioanta(request, response, idAzienda);
                        break;
                    default:
                        request.setAttribute("message", "Invalid action");
                        action_error(request, response);
                }
            } else {
                request.setAttribute("message", "Richiesta non valida");
                action_error(request, response);
            }
        } catch (DataLayerException | TemplateManagerException | SecurityLayerException | NumberFormatException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DocumentException ex) {
            Logger.getLogger(infoAzienda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
