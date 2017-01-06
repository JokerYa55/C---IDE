/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.interfaces;

/**
 *
 * @author vasl Интерфейс для выражения. Выражение может быть либо одиночным
 * операндом (переменная, значение), либо выражением состоящим из операции и
 * операндов (5+3, a+6, a+b*(12+4)) и т. д. exp : ID|exp
 */
public interface pNodeInterface {

    /**
     *
     * @return
     */
    public enum pNodeEnum {
        OPER, BLOCK
    }

    public pNodeEnum getType();

    public void setType(pNodeEnum expType);

    public long getLevel();
    
    public void setLevel(long level);
}
