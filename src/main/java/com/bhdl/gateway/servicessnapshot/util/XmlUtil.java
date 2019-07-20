package com.bhdl.gateway.servicessnapshot.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;

@UtilityClass
public class XmlUtil {

    private static Logger LOG = LoggerFactory.getLogger(XmlUtil.class);

    public String getNodeContent(final String xmlContent, final String xpathToNode) {
        // create xml document object from xmlContent param
        Document document = parseXml(xmlContent);

        // locate node to return
        Node node = getNodeByXpath(document, xpathToNode);

        // return policy node content
        return node.getTextContent();
    }

    public Node findNode(Node root, String attributeValueToSearch,
                         boolean deep, boolean elementsOnly) {
        //Check to see if root has any children if not return null
        if (!(root.hasChildNodes()))
            return null;

        //Root has children, so continue searching for them
        Node matchingNode = null;
        String attributeValue = null;
        Node child = null;

        NodeList childNodes = root.getChildNodes();
        int noChildren = childNodes.getLength();
        for (int i = 0; i < noChildren; i++) {
            if (matchingNode == null) {
                child = childNodes.item(i);
                attributeValue = child.getAttributes() != null &&
                        child.getAttributes().getNamedItem("stringValue") != null ?
                        child.getAttributes().getNamedItem("stringValue").getNodeValue() : null;
                if ((attributeValue != null) && (attributeValue.contains(attributeValueToSearch))) {
                    return child;
                }
                if (deep) {
                    matchingNode = findNode(child, attributeValueToSearch, deep, elementsOnly);
                }
            } else
                break;
        }

        if (!elementsOnly) {
            NamedNodeMap childAttrs = root.getAttributes();
            noChildren = childAttrs.getLength();
            for (int i = 0; i < noChildren; i++) {
                if (matchingNode == null) {
                    child = childAttrs.item(i);
                    attributeValue = child.getAttributes() != null &&
                            child.getAttributes().getNamedItem("stringValue") != null ?
                            child.getAttributes().getNamedItem("stringValue").getNodeValue() : null;
                    if ((attributeValue != null) && (attributeValue.contains(attributeValueToSearch)))
                        return child;
                } else
                    break;
            }
        }
        return matchingNode;
    }

    public Document createDocumentFromNode(Node root) {
        Document newDocument = null;
        try {
            newDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            LOG.error("Ups! something happened", ex);
        }
        newDocument.appendChild(newDocument.importNode(root, true));
        return newDocument;
    }

    public Document makeCopy(Document documentToCopy) {
        Document newDocument = null;
        try {
            newDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            LOG.error("Ups! something happened", ex);
        }
        Node documentRoot = documentToCopy.getDocumentElement();
        newDocument.appendChild(newDocument.importNode(documentRoot, true));
        return newDocument;
    }

    public Document parseXml(final String xmlContent) {
        try {
            return DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new InputSource(new StringReader(xmlContent)));
        } catch (Exception ex) {
            LOG.error("Ups! something happened", ex);
            return null;
        }
    }

    public NodeList getNodeListByXpath(final String xmlContent, final String xpathString) {
        try {
            Document document = parseXml(xmlContent);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xpath.evaluate(xpathString, document, XPathConstants.NODESET);
            return nodeList;
        } catch (Exception ex) {
            LOG.error("Ups! something happened", ex);
            return null;
        }
    }

    public Node getNodeByXpath(final Document document, final String xpathString) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xpath.evaluate(xpathString, document, XPathConstants.NODE);
            return node;
        } catch (Exception ex) {
            LOG.error("Ups! something happened", ex);
            return null;
        }
    }

    public String getValueByXpath(final Document document, final String xpathString) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            return (String) xpath.evaluate(xpathString, document, XPathConstants.STRING);
        } catch (Exception ex) {
            LOG.error("Ups! something happened", ex);
            return null;
        }
    }

    public String nodeToString(Node node) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (Exception ex) {
            LOG.error("Ups! something happened", ex);
            return null;
        }
    }
}
