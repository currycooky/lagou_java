package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");

        List<Element> list = rootElement.selectNodes("//select");
        setSelects(list, namespace);
        List<Element> inserts = rootElement.selectNodes("//insert");
        List<Element> updates = rootElement.selectNodes("//update");
        List<Element> deletes = rootElement.selectNodes("//delete");
        setExecutes(inserts, namespace);
        setExecutes(updates, namespace);
        setExecutes(deletes, namespace);
    }

    private void setSelects(List<Element> list, String namespace) {
        for (Element element : list) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sqlText);
            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key, mappedStatement);
        }
    }

    private void setExecutes(List<Element> list, String namespace) {
        list.forEach(e -> {
            MappedStatement mappedStatement = new MappedStatement();
            String id = e.attributeValue("id");
            mappedStatement.setId(id);
            mappedStatement.setSql(e.getTextTrim());
            configuration.getMappedStatementMap().put(namespace + "." + id, mappedStatement);
        });
    }
}
