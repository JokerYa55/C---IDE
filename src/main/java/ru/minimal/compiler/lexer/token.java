/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.lexer;

/**
 *
 * @author vasl
 */
public class token {
    private String val; // Значение токена
    private lexerConst.tokenEnum sym; // Тип токена

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public lexerConst.tokenEnum getSym() {
        return sym;
    }

    public void setSym(lexerConst.tokenEnum sym) {
        this.sym = sym;
    }

    @Override
    public String toString() {
        return "token{" + "val=" + val + ", sym=" + sym + '}';
    }
    
}
