package it.bma.web;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import it.bma.comuni.*;

import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.ConsoleLogger;
//
import org.apache.fop.apps.XSLTInputHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class Xml2PdfServlet extends HttpServlet implements it.bma.comuni.BmaLiterals, it.bma.web.BmaJspLiterals {
	private BmaJsp jsp = new BmaJsp();
	private	InputStream isXml = null;
	private	InputStream isXsl = null;
	public Xml2PdfServlet() {
		super();
	}
	 public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		esegui(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		esegui(request, response);
	}
	private void esegui(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		// Recupero dei parametri in ingresso
		String xmlFile = request.getParameter("xml");
		String xslFile = request.getParameter("xsl");
		String xml = (String)request.getSession().getAttribute(BMA_JSP_BEAN_XML);
		try {
			if (xml!=null) {
				request.getSession().removeAttribute(BMA_JSP_BEAN_XML);
				isXml = new ByteArrayInputStream(xml.getBytes());
			}
			else if (xmlFile!=null && xmlFile.trim().length() > 0) {
				String filePath = request.getSession().getServletContext().getRealPath(xmlFile);
				isXml = new FileInputStream(filePath);
			}
			else {
				throw new BmaException(jsp.BMA_ERR_WEB_PARAMETRO, "Manca XML", "", jsp);
			}
			if (xslFile==null || xslFile.trim().length()==0) {
				throw new BmaException(jsp.BMA_ERR_WEB_PARAMETRO, "Manca XSL-FO", "", jsp);
			}
			else {
				String filePath = request.getSession().getServletContext().getRealPath(xslFile);
				isXsl = new FileInputStream(filePath);
			}
			response.setContentType("application/pdf;charset=ISO-8859-1");
			ByteArrayOutputStream out = makePdf();
      byte[] content = out.toByteArray();
      response.setContentLength(content.length);
      response.getOutputStream().write(content);
      response.getOutputStream().flush();
		}
		catch (BmaException e) {
			BmaErrore err = e.getErrore();
			vaiPaginaErrore(err, request, response);
		}
	}
	private ByteArrayOutputStream makePdf() throws BmaException {
		try {
			Source xmlSource = new StreamSource(isXml);
			Source xslSource = new StreamSource(isXsl);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(xslSource);

			Driver driver = new Driver();
			Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
			driver.setLogger(logger);
			MessageHandler.setScreenLogger(logger);
			driver.setRenderer(Driver.RENDER_PDF);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			driver.setOutputStream(out);

			Result result = new SAXResult(driver.getContentHandler());
			transformer.transform(xmlSource, result);

			return out;

		}
		catch (Exception e) {
			throw new BmaException(jsp.BMA_ERR_WEB_PARAMETRO,	e.getClass().getName(), e.getMessage(), jsp);
		}
	}
	private void vaiPaginaErrore(BmaErrore err, HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute(BMA_JSP_BEAN_ERRORE, err);
		String errPage = BMA_JSP_PAGINA_ERRORE;
		vaiPaginaJSP(errPage, request, response);
	}
	private void vaiPaginaJSP(String pagina, HttpServletRequest request, HttpServletResponse response) {
		if (!pagina.substring(0,1).equals("/")) pagina = "/" + pagina;
		try {
			getServletContext().getRequestDispatcher(pagina).forward(request, response);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
