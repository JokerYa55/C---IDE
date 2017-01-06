/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.comiler;

import org.apache.log4j.Logger;
import ru.minimal.compiler.lexer.lexerConst.tokenEnum;
import ru.minimal.compiler.lexer.token;
import ru.minimal.compiler.interfaces.pNodeInterface;

/**
 *
 * @author vasl
 */
public class lexNode implements pNodeInterface {

    static Logger log = Logger.getLogger(lexNode.class);
    private token nToken;
    // Левый операнд
    private pNodeInterface lNode;
    // Правый операнд
    private pNodeInterface rNode;
    // Дополнительный операнд для if
    private pNodeInterface dNode;
    // Ссылка на родительский элемент
    private pNodeInterface parrentNode;
    // Уровень в дереве
    private long level;
    // Значение перации
    private long valNode;
    // Тип узла OPER - Операция ????
    private tokenEnum lexNodeType;

    @Override
    public String toString() {
        return "lexNode{" + "nToen=" + nToken + '}';
    }

    @Override
    public void setType(pNodeEnum expType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //this.lexNodeType = 
    }

    public token getnToken() {
        return nToken;
    }

    public void setnToken(token nToken) {
        this.nToken = nToken;
    }

    public pNodeInterface getlNode() {
        return lNode;
    }

    public void setlNode(pNodeInterface lNode) {
        this.lNode = lNode;
    }

    public pNodeInterface getrNode() {
        return rNode;
    }

    public void setrNode(pNodeInterface rNode) {
        this.rNode = rNode;
    }

    public pNodeInterface getdNode() {
        return dNode;
    }

    public void setdNode(pNodeInterface dNode) {
        this.dNode = dNode;
    }

    public pNodeInterface getParrentNode() {
        return parrentNode;
    }

    public void setParrentNode(pNodeInterface parrentNode) {
        this.parrentNode = parrentNode;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getValNode() {
        return valNode;
    }

    public void setValNode(long valNode) {
        this.valNode = valNode;
    }

    public tokenEnum getLexNodeType() {
        return lexNodeType;
    }

    public void setLexNodeType(tokenEnum lexNodeType) {
        this.lexNodeType = lexNodeType;
    }

    @Override
    public pNodeEnum getType() {
        //log.debug("getType");
        /*if (lexerConst.isOper(nToken)) {
            //log.debug("OPER");
            return pNodeEnum.OPER;
        } else {
            //log.debug("BLOCK");
            return pNodeEnum.BLOCK;
        }*/
        return pNodeEnum.OPER;
    }
}
