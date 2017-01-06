/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.comiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.apache.log4j.Logger;
import ru.minimal.compiler.interfaces.pNodeInterface;

/**
 *
 * @author vasl programBlock - блок операторов объедененных логически. Всегда
 * есть осноовной блок содержащий все операторы состоит из списка операторов
 * (синтаксических деревьев разбора оператора)
 */
public class programBlock implements Iterable<lexTree>, pNodeInterface {

    private static final Logger log = Logger.getLogger(programBlock.class);
    private List<pNodeInterface> operList;
    //Ссылка на родительский блок
    private pNodeInterface pBlock;
    private String blockName;
    private pNodeEnum expType;
    private long level = 0;

    public programBlock(programBlock parentBlock) {
        this.pBlock = parentBlock;
        this.operList = new ArrayList<>();
    }

    public programBlock(pNodeInterface parentBlock) {
        this.pBlock = parentBlock;
        this.operList = new ArrayList<>();
    }

    
    public boolean addOper(pNodeInterface oper) {
        boolean res = false;
        res = this.operList.add(oper);
        return res;
    }

    public List<pNodeInterface> getOperList() {
        return operList;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String nodeToString(pNodeInterface node, String par) {
        StringBuilder res = new StringBuilder();
        try {
            res.append(par + "Блок = " + blockName + "{\n");
            for (pNodeInterface item : this.operList) {
                log.debug("Type = " + item.getType());
                if (item.getType() == pNodeEnum.OPER) {
                    log.debug("OPER = ");
                    //log.debug(item);
                    res.append((lexTree) item).toString();
                } else {
                    log.debug("BLOCK = ");
                    List ps = ((programBlock) item).getOperList();
                    for (Object p : ps) {
                        //log.debug("\t class = " + p.getClass());
                        //log.debug(p);
                        res.append((lexTree) p).toString();
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return res.toString();
    }

    @Override
    public String toString() {        
        return nodeToString(this, "ROOT\n");
    }

    @Override
    public Iterator<lexTree> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void forEach(Consumer<? super lexTree> action
    ) {
        Iterable.super.forEach(action); //To change body of generated methods, choose Tools | Templates.
    }

    public pNodeInterface getpBlock() {
        return pBlock;
    }

    public void setpBlock(pNodeInterface pBlock) {
        this.pBlock = pBlock;
    }

    @Override
    public pNodeEnum getType() {
        return this.expType;
    }

    @Override
    public void setType(pNodeEnum expType) {
        this.expType = expType;
    }

    @Override
    public long getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(long level) {
        this.level = level;
    }
}
