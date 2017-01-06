/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.comiler;

import org.apache.log4j.Logger;
import ru.minimal.compiler.lexer.token;
import ru.minimal.compiler.interfaces.pNodeInterface;

/**
 *
 * @author vasl
 */
public class lexTree implements pNodeInterface {

    private static final Logger log = Logger.getLogger(lexTree.class);
    private lexNode rootNode;
    private pNodeEnum expType = pNodeEnum.OPER;
    
    public lexTree(token current) {
        rootNode = new lexNode();
        rootNode.setnToken(current);
        rootNode.setLevel(0);
    }
    
    public lexTree() {
        
    }
    
    public lexTree(lexNode rNode) {
        this.rootNode = rNode;
    }
    
    public lexNode addNode(lexNode parent, token current) {
        lexNode res = new lexNode();
        res.setnToken(current);
        
        if (this.rootNode == null) {
            rootNode = res;
            res.setParrentNode(null);
            res.setlNode(null);
            res.setLevel(0);
        } else {
            res.setLevel(parent.getLevel() + 1);
            if (parent.getlNode() != null) {
                parent.setrNode(res);
            } else {
                parent.setlNode(res);
            }
            res.setParrentNode(parent);
        }
        //res.setLevel(parent.getLevel()+1);
        return res;
    }
    
    private String nodeToString(pNodeInterface node, String par) {
       // boolean flag = false;
        StringBuilder res = new StringBuilder();
        try {
            //log.debug("nodeToString => {par = "+ par +"}\t=>" + node);
            if (node.getType() == pNodeEnum.OPER) {
                lexNode tempNode = (lexNode) node;
                res.append(par).append("\t-> ").append(String.format("%" + (tempNode.getnToken().toString().length() + 5 * tempNode.getLevel()) + "s%n", tempNode.getnToken().toString() + " - " + tempNode.getLevel()));
                if (tempNode.getlNode() != null) {
                    //log.debug("Обрабатываем lNode");
                    res.append(nodeToString(tempNode.getlNode(), "left"));
                }
                
                if (tempNode.getrNode() != null) {
                    //log.debug("Обрабатываем rNode");
                    res.append(nodeToString(tempNode.getrNode(), "right"));
                }
                
                if (tempNode.getdNode() != null) {
                    //log.debug("Обрабатываем dNode");
                    res.append(nodeToString(tempNode.getdNode(), "dop"));
                }
            } else {
                //log.debug("Обрабатываем BLOCK");
                programBlock tempPB = (programBlock) node;
                for (pNodeInterface item : tempPB.getOperList()) {
                    res.append(nodeToString(((lexTree)item).rootNode, par));
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        //log.debug(res.toString());        
        return res.toString();
    }
    
    @Override
    public String toString() {
        return nodeToString(rootNode, "root");
    }
    
    public lexNode getRootNode() {
        return rootNode;
    }
    
    @Override
    public pNodeEnum getType() {
        return this.expType;
    }
    
    @Override
    public void setType(pNodeEnum expType) {
        this.expType = pNodeEnum.OPER;
    }
    
    @Override
    public long getLevel() {
        return this.rootNode.getLevel();
    }
    
    @Override
    public void setLevel(long level) {
        this.rootNode.setLevel(level);
    }
    
}
