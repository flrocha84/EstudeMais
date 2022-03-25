package com.proj.fab.estudemais;

public class CategoryModel {

    private int docId;
    private String name;
    private int noOfTests;

    public CategoryModel(int docId, String name, int noOfTests) {
        this.docId = docId;
        this.name = name;
        this.noOfTests = noOfTests;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfTests() {
        return noOfTests;
    }

    public void setNoOfTests(int noOfTests) {
        this.noOfTests = noOfTests;
    }
}
