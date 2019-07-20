package com.bhdl.gateway.servicessnapshot.entity;

import com.bhdl.gateway.servicessnapshot.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Layer7PolicyEntity extends Layer7BaseEntity {

    Layer7PolicyEntity(String serviceResponseString) {
        super(serviceResponseString);
    }

    public Layer7PolicyAssertions getTemplateAssertions() {
        Document newPolicyDocument = XmlUtil.makeCopy(getPolicyDocument());
//        removeNodes(newPolicyDocument, startNodeAttributeValue, true);
//        removeNodes(newPolicyDocument, endNodeAttributeValue, false);
        return new Layer7PolicyAssertions(newPolicyDocument);
    }

    private void removeNodes(Document document, String nodeAttributeFrom, boolean forward) {
        // Get start node of service policy assertions, xml node attribute contains: "COPIAR DESDE AQUI"
        Node customPolicyAssertionsStartNode = XmlUtil.
                findNode(document, nodeAttributeFrom,
                        true, true).getParentNode();

        // Remove nodes before policy assertions including start node
        Node customPolicyAssertionsParentNode = customPolicyAssertionsStartNode.getParentNode();
        Node nextNodeToRemove = customPolicyAssertionsStartNode;

        while (nextNodeToRemove != null) {
            Node currentNodeToRemove = nextNodeToRemove;
            if (forward) {
                nextNodeToRemove = currentNodeToRemove.getPreviousSibling();
            } else {
                nextNodeToRemove = currentNodeToRemove.getNextSibling();
            }
            customPolicyAssertionsParentNode.removeChild(currentNodeToRemove);
        }
    }
}
