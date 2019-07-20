package com.bhdl.gateway.servicessnapshot.entity;

import com.bhdl.gateway.servicessnapshot.util.XmlUtil;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Getter
public abstract class Layer7BaseEntity {

    private final String serviceResponseString;
    private Document entityDocument;
    private Document policyDocument;

    Layer7BaseEntity(String serviceResponseString) {
        this.serviceResponseString = serviceResponseString;
    }

    public static Layer7BaseEntity createEntity(String serviceResponseString, Layer7EntityType entityType) {
        switch (entityType) {
            case POLICY:
                return new Layer7PolicyEntity(serviceResponseString);
            case SERVICE:
                return new Layer7ServiceEntity(serviceResponseString);
        }
        return null;
    }

    public void initializePolicy() {
//        String policyContent = XmlUtil.getNodeContent(serviceResponseString, xpathToPolicyDocument);
        policyDocument = XmlUtil.parseXml(serviceResponseString);
    }

    public void initializeEntity(String xpathToEntityDocument) {
        Document serviceResponseDocument = XmlUtil.parseXml(serviceResponseString);
        Node entityNode = XmlUtil.getNodeByXpath(serviceResponseDocument, xpathToEntityDocument);
        entityDocument = XmlUtil.createDocumentFromNode(entityNode);
    }

    public String getEntityDocumentAsString() {
        if (getEntityDocument() == null) {
            throw new IllegalStateException("Entity document has not been initialized, call initializeEntity method");
        }
        return XmlUtil.nodeToString(getEntityDocument());
    }
}
