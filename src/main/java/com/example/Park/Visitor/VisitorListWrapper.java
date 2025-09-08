package com.example.Park.Visitor;

import java.util.ArrayList;
import java.util.List;

public class VisitorListWrapper {
    private List<Visitor> visitors = new ArrayList<>();
    public List<Visitor> getVisitors() {
        return visitors;
    }
    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
    }
}
