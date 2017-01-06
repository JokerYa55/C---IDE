/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.comiler;

import org.apache.log4j.Logger;
import ru.minimal.compiler.interfaces.pNodeInterface;

/**
 *
 * @author vasl
 */
public class programStract {

    static Logger log = Logger.getLogger(programStract.class);
    private programBlock pblock;

    public programStract() {
        this.pblock = new programBlock(null);
        this.pblock.setBlockName("main");
    }

    public programBlock getPblock() {
        return pblock;
    }

    public programBlock newProgramBlock(pNodeInterface parrentBlock, String name) {
        programBlock res = null;
        try {
            log.debug("newPrograBlock");
            res = new programBlock(parrentBlock);
            res.setType(pNodeInterface.pNodeEnum.BLOCK);
            res.setBlockName(name);
            if (parrentBlock == null) {
                res.setLevel(parrentBlock.getLevel() + 1);
            } else {
                res.setLevel(parrentBlock.getLevel() + 1);
            }
            //parrentBlock.setpBlock(res);
            //parrentBlock.addOper(res);
        } catch (Exception e) {
            log.error(e);
        }
        return res;
    }

    @Override
    public String toString() {
        return pblock.toString();
    }

}
