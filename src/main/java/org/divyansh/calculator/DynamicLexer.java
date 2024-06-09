package org.divyansh.calculator;

import org.jetbrains.annotations.NotNull;

public class DynamicLexer extends Lexer{
    public DynamicLexer() {
        super("");
    }
    public void updateLexer(String text){
        super.tokenArrayList.clear();
        super.parseCharArray(text.toCharArray());
    }
}