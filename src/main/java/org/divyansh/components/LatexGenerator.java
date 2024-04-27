package org.divyansh.components;

public class LatexGenerator {
    private final StringBuilder latexBuilder=  new StringBuilder();
    public LatexGenerator(){

    }

    public void appendLatexString(String s){
        latexBuilder.append(s);
    }
    public void appendLatexString(String s, int index){
        latexBuilder.insert(index,s);
    }
    public void deleteLatexKeyword(int index){

    }
    public String getLatex(){
        return latexBuilder.toString();
    }
}
