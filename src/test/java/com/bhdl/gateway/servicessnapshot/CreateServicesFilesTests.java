package com.bhdl.gateway.servicessnapshot;

import com.bhdl.gateway.servicessnapshot.entity.*;
import com.bhdl.gateway.servicessnapshot.util.FileUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.matchers.CompareMatcher;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class CreateServicesFilesTests {

    private static Logger LOG = LoggerFactory.getLogger(CreateServicesFilesTests.class);

    private final String RESTMAN_URL = "https://api-dev.bhdleon.com.do/restman/1.0/";
    private final String RESTMAN_USER = "rgarcia";
    private final String RESTMAN_PASSWORD = "Pas$w0rd";
    private final String POLICY_CONTENT_XPATH = "/List/Item/Resource/Policy/Resources/ResourceSet/Resource";
    private final String SERVICE_CONTENT_XPATH = "/List/Item/Resource/Service/Resources/ResourceSet/Resource";
    private final String SERVICE_NAME_XPATH = "/List/Item/Name";
    private final String SERVICE_URL_XPATH = "/List/Item/Resource/Service/ServiceDetail/ServiceMappings/HttpMapping/UrlPattern";
//    private final String  = "/Policy/All/CommentAssertion/Comment/@stringValue";
    private final String SERVICE_ENTITY_XPATH = "/List/Item/Resource/Service";
    private final String SERVICE_ENTITY_POLICY_XPATH = "/Service/Resources/ResourceSet/Resource";
    private final String SERVICE_URLPATERN_XPATH = "/Service/ServiceDetail/ServiceMappings/HttpMapping/UrlPattern";
    private final String SERVICE_REVISION_XPATH = "/Service/ServiceDetail/Properties/Property[@key='policyRevision']/LongValue";
    private final String POLICY_CUSTOM_START = "COPIAR DESDE AQUI";
    private final String POLICY_CUSTOM_END = "COPIAR HASTA AQUI";
    private final String SERVICE_CUSTOM = "LOGICA";

    @Test
    public void when_get_services_then_create_file_for_each_one() throws IOException {
        String servicesXml = FileUtil.readFromFilePath("/services.xml");
        String templateXml = FileUtil.readFromFilePath("/template.xml");
        Layer7ServiceListEntity entity = new Layer7ServiceListEntity(servicesXml);
        entity.initializePolicies(SERVICE_CONTENT_XPATH);
        NodeList servicesNodeList = entity.getPolicies();
        NodeList serviceNameNodeList = entity.getServiceNameList(SERVICE_NAME_XPATH);
        NodeList serviceUrlNodeList = entity.getServiceUrlList(SERVICE_URL_XPATH);
        for (int i = 0; i < servicesNodeList.getLength(); i++) {
            String serviceContent = servicesNodeList.item(i).getTextContent();
            FileUtil.writeToFilePath("C:\\Users\\Roberto Garcia\\Documents\\sprints\\13\\services\\service" + i + ".xml", serviceContent);
            Layer7PolicyEntity policy = (Layer7PolicyEntity) Layer7BaseEntity
                    .createEntity(serviceContent, Layer7EntityType.POLICY);
            policy.initializePolicy();
            Layer7PolicyAssertions serviceAssertions = policy.getTemplateAssertions();
            String serviceName = serviceNameNodeList.item(i).getTextContent();
            String serviceUrl = serviceUrlNodeList.item(i).getTextContent();
            System.out.println("----------------------"+ i +"-------------------------");
            System.out.println("service name:" + serviceName);
            System.out.println("service url:" + serviceUrl);
            serviceAssertions.removeCustomLogicAssertions(SERVICE_CUSTOM);
            serviceAssertions.getAssertionsDocumentAsString();
            String serviceXml = serviceAssertions.getAssertionsDocumentAsString();
//            assertThat(templateXml, CompareMatcher.isSimilarTo(serviceXml));
            Diff myDiff = DiffBuilder.compare(Input.fromString(templateXml))
                    .checkForSimilar()
                    .ignoreComments()
                    .ignoreWhitespace()
                    .withTest(Input.fromString(serviceXml))
                    .build();
            System.out.println("-----------------------------------------------");
//            LOG.info("COMPARING:" + templateXml);
//            LOG.info("TO:" + serviceXml);
//            LOG.info("ARE DIFF:" + myDiff.hasDifferences());
//            for (Difference difference: myDiff.getDifferences()) {
//                LOG.info("DIFFERENCE: {}", difference.toString());
//            }

        }
    }
}
