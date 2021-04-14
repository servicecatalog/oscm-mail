package org.oscm.mail.client;

import org.oscm.security.SOAPSecurityHandler;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WSClient {

  private static final String BASE_URL = "http://oscm-core:8080/oscm-webservices/";
  private static final String NAMESPACE = "http://oscm.org/xsd";

  public static <T> T getWS(
      String authMode, Class<T> remoteInterface, String userName, String password)
      throws Exception {

    String wsdlUrl = BASE_URL + remoteInterface.getSimpleName() + "/BASIC?wsdl";

    URL url = new URL(wsdlUrl);
    QName qName = new QName(NAMESPACE, remoteInterface.getSimpleName());
    Service service = Service.create(url, qName);

    T port = service.getPort(remoteInterface);
    BindingProvider bindingProvider = (BindingProvider) port;

    if ("OIDC".equals(authMode)) {
      password = "WS" + password;
    }

    Binding binding = bindingProvider.getBinding();
    List<Handler> handlerChain = binding.getHandlerChain();
    if (handlerChain == null) {
      handlerChain = new ArrayList<>();
    }

    handlerChain.add(new SOAPSecurityHandler(userName, password));
    binding.setHandlerChain(handlerChain);

    return port;
  }
}
