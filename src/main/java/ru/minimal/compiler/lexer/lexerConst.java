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
public class lexerConst {

    public enum tokenEnum {
        NUM, ID, IF, ELSE, WHILE, DO, FOR, LBRA, RBRA, LPAR, RPAR, PLUS, MINUS, MUL, LESS, EQUAL, SEMICOLON, EOF
    };

   
    public static boolean isOper(token tok) {
        boolean flag = false;
        switch (tok.getSym()) {
            case EQUAL:
                flag = true;
                break;
            case MINUS:
                flag = true;
                break;
            case PLUS:
                flag = true;
                break;
            case MUL:
                flag = true;
                break;
            case LESS:
                flag = true;
                break;
            default:
                break;
        }
        return flag;
    }
}
