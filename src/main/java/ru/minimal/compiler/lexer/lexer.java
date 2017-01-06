/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.lexer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import ru.minimal.compiler.lexer.lexerConst.tokenEnum;

/**
 *
 * @author vasl
 */
public class lexer {

    static Logger log = Logger.getLogger(lexer.class);
    private Hashtable<String, tokenEnum> symbols = new Hashtable<>();
    private Hashtable<String, tokenEnum> words = new Hashtable<>();
    private Reader fin;
    private List<token> tokenList = new ArrayList<>();
    private char currentCh = 0;
    private Pattern alphaPattern,
            digitPattern,
            operPattern,
            scobPattern,
            endOperPattern,
            wordPattern;

    public lexer(Reader in) {

        this.fin = in;
        // инициализируем символы
        symbols.put("{", tokenEnum.LBRA);
        symbols.put("}", tokenEnum.RBRA);
        symbols.put("=", tokenEnum.EQUAL);
        symbols.put(";", tokenEnum.SEMICOLON);
        symbols.put("(", tokenEnum.LPAR);
        symbols.put(")", tokenEnum.RPAR);
        symbols.put("+", tokenEnum.PLUS);
        symbols.put("-", tokenEnum.MINUS);
        symbols.put("<", tokenEnum.LESS);
        symbols.put("*", tokenEnum.MUL);

        // инициализируе слова
        words.put("if", tokenEnum.IF);
        words.put("else", tokenEnum.ELSE);
        words.put("do", tokenEnum.DO);
        words.put("while", tokenEnum.WHILE);
        words.put("for", tokenEnum.FOR);

        // Добавляе паттерны для проверки
        alphaPattern    = Pattern.compile("^[a-zA-Z]$");
        digitPattern    = Pattern.compile("^[0-9]$");
        operPattern     = Pattern.compile("^=|\\+|\\-|<|\\*$");
        scobPattern     = Pattern.compile("^\\{|\\}|\\(|\\)$");
        endOperPattern  = Pattern.compile("^;$");
        wordPattern     = Pattern.compile("^if|else|do|while|for$");

        this.currentCh = getChar();
        nextToken();
        log.debug("*******************************");
        for (token tok : tokenList) {
            log.debug(tok.getSym() + " : " + tok.getVal());
        }
    }

    public token nextToken() {
        token res = new token();
        res.setSym(null);
        res.setVal(null);
        while (/*(res.getSym() != null) &&*/(this.currentCh != 65535)) {
            //log.debug("ch = " + (int) this.currentCh);
            if (this.currentCh == 0) {
                res.setSym(tokenEnum.EOF);
                res.setVal(this.currentCh + "");
            } else if (isAlpha(currentCh + "")) {
                String ident = "";
                while (isAlpha(currentCh + "")) {
                    ident = ident + this.currentCh;
                    this.currentCh = getChar();
                    log.debug("this.currentCh : " + this.currentCh);
                }
                if (isWord(ident)) {
                    res = new token();
                    res.setSym(words.get(ident));
                    res.setVal(ident);
                    tokenList.add(res);
                    log.debug("word : " + ident);
                    //this.currentCh = getChar();
                } else {
                    res = new token();
                    res.setSym(tokenEnum.ID);
                    res.setVal(ident);
                    tokenList.add(res);
                    log.debug("Alpha : " + res.getSym());
                    //this.currentCh = getChar();
                }

            } else if (isDigit(currentCh + "")) {
                String ident = "";
                while (isDigit(currentCh + "")) {
                    ident = ident + this.currentCh;
                    this.currentCh = getChar();
                }
                res = new token();
                res.setSym(tokenEnum.NUM);
                res.setVal(ident);
                tokenList.add(res);
                log.debug("Digit : " + res.getSym());
                //this.currentCh = getChar();
            } else if (isOper(currentCh + "")) {
                res = new token();
                res.setSym(symbols.get(currentCh+""));
                res.setVal(currentCh+"");
                tokenList.add(res);
                log.debug("Oper : " + res.getSym());
                this.currentCh = getChar();
            } else if (isScob(currentCh + "")) {
                res = new token();
                res.setSym(symbols.get(currentCh+""));
                res.setVal(currentCh+"");
                tokenList.add(res);
                log.debug("Скобка : " + res.getSym());
                this.currentCh = getChar();
            } else if (isEndOper(currentCh + "")) {
                res = new token();
                res.setSym(symbols.get(currentCh+""));
                res.setVal(currentCh+"");
                tokenList.add(res);
                log.debug("End oper : " + res.getSym());
                this.currentCh = getChar();
            } else {
                this.currentCh = getChar();
            }
        }
        return res;
    }

    private char getChar() {
        char res = 0;
        try {
            res = (char) this.fin.read();
        } catch (IOException ex) {
            //        log.error(ex);
        }
        return res;
    }

    // Функции проверки типа токена
    private boolean isDigit(String ch) {
        boolean flag = false;
        Matcher m = this.digitPattern.matcher(ch);
        flag = m.matches();
        return flag;
    }

    private boolean isAlpha(String ch) {
        boolean flag = false;
        Matcher m = this.alphaPattern.matcher(ch);
        flag = m.matches();
        return flag;
    }

    private boolean isOper(String ch) {
        boolean flag = false;
        Matcher m = this.operPattern.matcher(ch);
        flag = m.matches();
        return flag;
    }

    private boolean isScob(String ch) {
        boolean flag = false;
        Matcher m = this.scobPattern.matcher(ch);
        flag = m.matches();
        return flag;
    }

    private boolean isEndOper(String ch) {
        boolean flag = false;
        Matcher m = this.endOperPattern.matcher(ch);
        flag = m.matches();
        return flag;
    }

    private boolean isWord(String ch) {
        boolean flag = false;
        Matcher m = this.wordPattern.matcher(ch);
        flag = m.matches();
        return flag;
    }

    public List<token> getTokenList() {
        return tokenList;
    }
}
