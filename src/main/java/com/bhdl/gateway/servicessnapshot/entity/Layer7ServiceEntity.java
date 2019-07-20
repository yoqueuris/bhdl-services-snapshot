package com.bhdl.gateway.servicessnapshot.entity;

import com.bhdl.gateway.servicessnapshot.util.XmlUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Layer7ServiceEntity extends Layer7BaseEntity {

    Layer7ServiceEntity(String serviceResponseString) {
        super(serviceResponseString);
    }

    public String formatEntityDocument(String serviceUrlPatternXpath, String serviceRevisionXpath) {
        NamedNodeMap rootNodeAttributes = getEntityDocument().getFirstChild().getAttributes();
        rootNodeAttributes.removeNamedItem("id");
        rootNodeAttributes.removeNamedItem("version");
        Node urlPatternNode = XmlUtil.getNodeByXpath(getEntityDocument(), serviceUrlPatternXpath);
        String newUrlPattern = null;
        String urlPattern = urlPatternNode.getTextContent();
        Matcher matcher = Pattern.compile("\\/v(\\d)").matcher(urlPattern);
        if (matcher.find()) {
            Integer version = Integer.parseInt(matcher.group(1));
            newUrlPattern = urlPattern.replaceAll("\\/v\\d", "/v" + ++version);
            urlPatternNode.setTextContent(newUrlPattern);
        }
        Node revisionNode = XmlUtil.getNodeByXpath(getEntityDocument(), serviceRevisionXpath);
        revisionNode.setTextContent("1");
        getEntityDocument().getDocumentElement().setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:l7", "http://ns.l7tech.com/2010/04/gateway-management");
        return newUrlPattern;
    }

    public void setPolicyToEntityDocument(Layer7PolicyAssertions policyAssertions, String xpathToPolicy) {
        if (getEntityDocument() == null) {
            throw new IllegalStateException("Entity document has not been initialized, call initializeEntity method");
        }
        String policyContent = XmlUtil.nodeToString(policyAssertions.getAssertionsDocument());
        Node policyNode = XmlUtil.getNodeByXpath(getEntityDocument(), xpathToPolicy);
        policyNode.setTextContent(policyContent);
    }
}