/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.comiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import ru.minimal.compiler.lexer.token;

/**
 *
 * @author vasil
 * Стек для хранения выражений
 */
public class StackExp {
    private Stack<List<token>> stackExp = new Stack<>();
    // Метод добавления элемента
    public void push(token exp)
    {
        List<token> item = new ArrayList<>();
        item.add(exp);
        this.stackExp.push(item);
    }
    
    public void push(List<token> exp)
    {
        this.stackExp.push(exp);
    }
    
    public List<token> pop()
    {
        return this.stackExp.pop();
    }
}
