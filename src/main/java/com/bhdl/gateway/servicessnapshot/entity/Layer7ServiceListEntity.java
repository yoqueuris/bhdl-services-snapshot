package com.bhdl.gateway.servicessnapshot.entity;

import com.bhdl.gateway.servicessnapshot.util.XmlUtil;
import lombok.Getter;
import org.w3c.dom.NodeList;

import java.util.List;

@Getter
public class Layer7ServiceListEntity {

    private final String serviceResponseString;
    private NodeList policies;

    public Layer7ServiceListEntity(String serviceResponseString) {
        this.serviceResponseString = serviceResponseString;
    }

    public NodeList getServiceNameList(String xpathToServiceName) {
        return XmlUtil.getNodeListByXpath(serviceResponseString, xpathToServiceName);
    }

    public NodeList getServiceUrlList(String xpathToServiceUrl) {
        return XmlUtil.getNodeListByXpath(serviceResponseString, xpathToServiceUrl);
    }

    public void initializePolicies(String xpathToPolicyDocument) {
        policies = XmlUtil.getNodeListByXpath(serviceResponseString, xpathToPolicyDocument);
    }
}