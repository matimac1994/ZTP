import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MM
 * @version 1.0
 */
public class NbpUtil {

    private static final String LINK =
            "http://www.nbp.pl/kursy/xml/LastA.xml";
    private static final String PLN_CODE = "pln";

    private Map<String, Double> mapCurrency = new HashMap<>();

    /**
     * @param local local currency code
     * @param remote remote currency code
     * @return current rate by NBP data
     */
    public String calculateRateByCurrency(
            String local,
            String remote
    ){
        Double result = 0.0;
        DecimalFormat format = new DecimalFormat("#.####");
        try {
            Document exchangeRates = getDocumentWithData();
            Element element = exchangeRates.getDocumentElement();

            fillMapByData(element.getElementsByTagName("pozycja"));

            result = getExchangeRate(local, remote);

        } catch (IOException e){
            System.out.println("NbpUtil - IOException");
            e.printStackTrace();
        } catch (SAXException e){
            System.out.println("NbpUtil - SAXException");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            System.out.println("NbpUtil - ParserConfigurationException");
            e.printStackTrace();
        }

        return format.format(result);
    }

    /**
     * @param local currency code
     * @param remote currency code
     * @return result
     */
    private Double getExchangeRate(String local, String remote) {
        if (mapCurrency.containsKey(local)
                && mapCurrency.containsKey(remote)
                && mapCurrency.get(remote) > 0.0)
            return mapCurrency.get(local) / mapCurrency.get(remote);
        else
            return 0.0;
    }

    /**
     * @param listOfCurrency list with currency objects
     */
    private void fillMapByData(NodeList listOfCurrency) {
        mapCurrency.put(PLN_CODE, 1.0);
        for (int i = 0; i < listOfCurrency.getLength(); i++){
            Element element = (Element) listOfCurrency.item(i);
            String code = getCurrencyCodeFromElement(element);
            Double averageExchange = getAverageExchangeFromElement(element);
            mapCurrency.put(code, averageExchange);
        }
    }

    /**
     * @param element element list
     * @return currency code from element
     */
    private String getCurrencyCodeFromElement(Element element) {
        return element.getElementsByTagName("kod_waluty")
                .item(0)
                .getTextContent();
    }

    /**
     * @param element element list
     * @return current average exchange
     */
    private Double getAverageExchangeFromElement(Element element) {
        String averageString = element
                .getElementsByTagName("kurs_sredni")
                .item(0)
                .getTextContent()
                .replace(",", ".");
        return Double.parseDouble(averageString);
    }


    /**
     * @return Document with data from URL
     * @throws IOException when I/O occur
     * @throws SAXException when saxe error occur
     * @throws ParserConfigurationException when parser error
     */
    private Document getDocumentWithData()
            throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory
                .newDocumentBuilder();
        return documentBuilder.parse(getDataFromNBP());
    }

    /**
     * @return stream from url
     * @throws IOException when I/O occur
     */
    private InputStream getDataFromNBP() throws IOException {
        URL url = new URL(LINK);
        return  url.openStream();
    }
}
