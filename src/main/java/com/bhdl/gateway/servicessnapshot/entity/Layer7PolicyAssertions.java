package com.bhdl.gateway.servicessnapshot.entity;

import com.bhdl.gateway.servicessnapshot.util.XmlUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Getter
public class Layer7PolicyAssertions {

    private static Logger LOG = LoggerFactory.getLogger(Layer7PolicyAssertions.class);

    private final Document assertionsDocument;

    public Layer7PolicyAssertions(Document assertionsDocument) {
        this.assertionsDocument = assertionsDocument;
    }

    public String getPolicyName(String xpathToName) {
        try {
            return XmlUtil.getValueByXpath(assertionsDocument, xpathToName);
        } catch (DOMException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeCustomLogicAssertions(String customLogicNodeAttributeValue) {
        // Get service custom assertions, xml node contains: "LOGICA"
        try {
            Node templateCustomLogicAssertionsNode = XmlUtil.
                    findNode(assertionsDocument, customLogicNodeAttributeValue,
                            true, true).getParentNode().getParentNode().getParentNode().getParentNode();

            Node templateCustomerLogicAssertionsParentNode = templateCustomLogicAssertionsNode.getParentNode();
            templateCustomerLogicAssertionsParentNode.removeChild(templateCustomLogicAssertionsNode);
        } catch (Exception e) {
//            LOG.error("Node LOGICA not found", e);
            System.out.println("No se encontró el nodo LOGICA");
        }
    }

    public String getAssertionsDocumentAsString() {
        if (assertionsDocument == null) {
            throw new IllegalStateException("Entity document has not been initialized, call initializeEntity method");
        }
        return XmlUtil.nodeToString(assertionsDocument);
    }
}
