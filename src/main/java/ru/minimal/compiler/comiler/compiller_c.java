/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.comiler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import org.apache.log4j.Logger;
import ru.minimal.compiler.interfaces.compilerInterface;
import ru.minimal.compiler.lexer.lexerConst;
import ru.minimal.compiler.lexer.token;
import ru.minimal.compiler.interfaces.pNodeInterface;
import ru.minimal.compiler.interfaces.pNodeInterface.pNodeEnum;

/**
 *
 * @author vasl
 */
public class compiller_c implements compilerInterface{

    static Logger log = Logger.getLogger(compiller_c.class);
    private lexTree tree;
    private lexNode currentNode;
    private programBlock currentProgramBlock;
    private int currentBlockNum = 0;
    // стек для операндв
    private Stack<lexNode> stack = new Stack();
    // стек для программных бллоков
    private Stack<programBlock> blockStack = new Stack();
    // Стек для скобок
    private Stack<String> stackSc = new Stack<>();
    /*
        programStruct   - структура программы. состоит из блоков
        programBlock    - блок операторов объедененных логически. Всегда есть 
                          осноовной блок содержащий все операторы состоит из 
                          списка операторов (синтаксических деревьев разбора оператора)
        operTreeList    - дерево синтаксического разбора оператора
     */

    private programStract ps;

    public compiller_c()   {

    }

    // TODO: Нужно к корню добавить список exp и для каждого оператора делать свое дерево разбора
    @Override
    public programStract genlexTree(List<token> listToken) {
        try {
            log.debug("genlexTree");
            if (listToken.size() > 0) {
                // Проверяем первый символ
                if (listToken.get(0).getSym() == lexerConst.tokenEnum.LBRA) {
                    // Создаем основной бллок и структуру программы
                    this.ps = new programStract();
                    // Устанавливаем текущий блок
                    this.currentProgramBlock = ps.getPblock();
                    //              blockStack.push(currentProgramBlock);
                    // Добавляем "{" в стек
                    this.stackSc.add("{");

                    tree = null;

                    //tree = new lexTree(listToken.get(0));
                    //this.currentNode = this.tree.getRootNode();
                    token currentToken = null;
                    for (int i = 1; i < listToken.size(); i++) {
                        log.debug("symvol = " + listToken.get(i));

                        // обрабатываем новый блок
                        if (listToken.get(i).getSym() == lexerConst.tokenEnum.LBRA) {
                            // Создаем нвый блок
                            log.debug("Создаем нвый блок");
                            this.stackSc.push("{");
                            // Сохранем старый блок в стеке
                            this.blockStack.push(currentProgramBlock);
                            this.currentBlockNum++;
                            // Создаем нвый блок и делаем ег текущим
                            this.currentProgramBlock = this.ps.newPrograBlock(currentProgramBlock, "main_" + currentBlockNum);
                            //this.currentProgramBlock.addOper(this.currentProgramBlock);
                        }

                        // обрабатываем конец нового блока
                        if (listToken.get(i).getSym() == lexerConst.tokenEnum.RBRA) {
                            //this.currentBlockNum ++;
                            this.stackSc.pop();
                            log.debug("обрабатываем конец нового блока");
                            this.currentProgramBlock = blockStack.pop();
                        }

                        // Обрабатывае цифру
                        if (listToken.get(i).getSym() == lexerConst.tokenEnum.NUM) {
                            log.debug("Отрабатываем NUM");
                            lexNode node = new lexNode();
                            node.setnToken(listToken.get(i));
                            stack.push(node);
                            log.debug("stack = " + stack.size());
                        }

                        // Обрабатываем конец операнда ;
                        if (listToken.get(i).getSym() == lexerConst.tokenEnum.SEMICOLON) {
                            log.debug("Отрабатываем ;");

                            // обработка одиночного оператора типа b=7;
                            if (this.stack.size() == 1) {
                                if (this.currentNode.getrNode() == null) {
                                    this.currentNode.setrNode(stack.pop());
                                    this.currentNode.getrNode().setLevel(this.currentNode.getLevel() + 1);
                                } else {
                                    this.currentNode.setlNode(stack.pop());
                                    this.currentNode.getlNode().setLevel(this.currentNode.getLevel() + 1);
                                }
                                this.currentNode = this.tree.getRootNode();
                                this.tree.setType(pNodeEnum.OPER);
                                this.currentProgramBlock.addOper(this.tree);
                                this.tree = null;
                                log.debug("stack = " + stack.size());
                            } else {
                                // Обработка опертора типа b=5+7;
                                this.currentNode.setrNode(stack.pop());
                                this.currentNode.getrNode().setLevel(this.currentNode.getLevel() + 1);
                                //this.currentNode = this.tree.getRootNode();

                                this.currentNode.setlNode(stack.pop());
                                this.currentNode.getlNode().setLevel(this.currentNode.getLevel() + 1);

                                this.currentNode = this.tree.getRootNode();
                                this.currentProgramBlock.addOper(this.tree);
                                this.tree = null;
                                log.debug("stack = " + stack.size());
                            }
                            //currentToken = null;
                        }

                        // Обрабатываем переменную
                        if (listToken.get(i).getSym() == lexerConst.tokenEnum.ID) // Если текущий токен идентификатор
                        {
                            // если нет текущей операции то ддобавяе в стек
                            log.debug("Отрабатываем ID блок : " + this.currentProgramBlock.getBlockName());
                            lexNode node = new lexNode();
                            // вносим переменную вместе с именем блока
                            listToken.get(i).setVal(/*this.currentProgramBlock.getBlockName() + "." + */listToken.get(i).getVal());
                            node.setnToken(listToken.get(i));
                            stack.push(node);
                            log.debug("stack = " + stack.size());
                            //currentToken = listToken.get(i);
                            /* } else {
                        System.out.println("Отрабатываем ID1");
                        //Если есть текущая операция то добавляе узел справа
                        this.currentNode = this.tree.addNode(currentNode, listToken.get(i));
                        System.out.println("stack = " + stack.size());
                    }*/
                        }

                        //Обрабатываем операции
                        switch (listToken.get(i).getSym()) {
                            case EQUAL:
                                log.debug("Отрабатываем =");
                                if (tree == null) {
                                    this.tree = new lexTree();
                                }
                                this.currentNode = this.tree.addNode(null, listToken.get(i));
                                this.currentNode.setlNode(stack.pop());
                                this.currentNode.getlNode().setLevel(this.currentNode.getLevel() + 1);
                                currentToken = listToken.get(i);
                                log.debug("stack = " + stack.size());
                                break;
                            case IF:
                                this.currentNode = this.tree.addNode(currentNode, listToken.get(i));
                                break;
                            case PLUS:
                                this.currentNode = this.tree.addNode(currentNode, listToken.get(i));
                                this.currentNode.setlNode(stack.pop());
                                this.currentNode.getlNode().setLevel(this.currentNode.getLevel() + 1);
                                currentToken = listToken.get(i);
                                log.debug("stack = " + stack.size());
                                break;
                            case MINUS:
                                this.currentNode = this.tree.addNode(currentNode, listToken.get(i));
                                this.currentNode.setlNode(stack.pop());
                                this.currentNode.getlNode().setLevel(this.currentNode.getLevel() + 1);
                                currentToken = listToken.get(i);
                                log.debug("stack = " + stack.size());
                                break;
                            default:
                                //System.out.println("Не операция : " + listToken.get(i));
                                break;
                        }
                    }
                }
            } else {
                log.debug("Ошибка компилции");
            }
        } catch (Exception e) {
            log.error("Ошибка : " + e.getMessage());
        }
        return this.ps;
    }

    private void getNextNode(lexNode parentNode, FileWriter out) throws IOException {
        // Если операци то выполнем генерим код асм
        log.debug("getNextNode: Если операци то выполнем генерим код асм");
        log.debug("ParentNode = " + parentNode);
        if (parentNode.getnToen().getSym() == lexerConst.tokenEnum.PLUS) {
            // обрабатываем "+"
            log.debug("Обрабатываем +");
            getNextNode(parentNode.getlNode(), out);
            getNextNode(parentNode.getrNode(), out);
            log.debug("add;");
            out.write("add;\n");
        }

        if (parentNode.getnToen().getSym() == lexerConst.tokenEnum.MINUS) {
            // обрабатываем "-"
            log.debug("Обрабатываем +");
            getNextNode(parentNode.getlNode(), out);
            getNextNode(parentNode.getrNode(), out);
            log.debug("add;");
            out.write("sub;\n");
        }

        if (parentNode.getnToen().getSym() == lexerConst.tokenEnum.EQUAL) {
            // обрабатываем "="
            //getNextNode(parentNode.getlNode());
            log.debug("брабатываем =");
            getNextNode(parentNode.getrNode(), out);
            log.debug("store " + parentNode.getlNode().getnToen().getVal() + ";");
            out.write("store " + parentNode.getlNode().getnToen().getVal() + ";\n");
        }

        if (parentNode.getnToen().getSym() == lexerConst.tokenEnum.ID) {
            // обрабатываем ID     
            log.debug("обрабатываем ID");
            log.debug("push " + parentNode.getnToen().getVal() + ";");
            out.write("push " + parentNode.getnToen().getVal() + ";\n");
        }

        if (parentNode.getnToen().getSym() == lexerConst.tokenEnum.NUM) {
            // обрабатываем ID     
            //System.out.println("брабатываем NUM");
            log.debug("push " + parentNode.getnToen().getVal() + ";");
            out.write("push " + parentNode.getnToen().getVal() + ";\n");
        }

        if (parentNode.getnToen().getSym() == lexerConst.tokenEnum.RBRA) {
            //    System.out.println("retr;");
            out.write("retr;\n");
        }

        if (parentNode.getnToen().getSym() == lexerConst.tokenEnum.LBRA) {
            // обрабатываем "{"
            //System.out.println("Обрабатываем {");

            if (parentNode.getlNode() != null) {
                getNextNode(parentNode.getlNode(), out);
            }

            if (parentNode.getrNode() != null) {
                getNextNode(parentNode.getrNode(), out);
                log.debug("add;");
            }
        }
    }

    public void genBlokASMText(programBlock block, FileWriter out) throws IOException {
        log.debug("Обработка блока " + block.getBlockName());
        for (pNodeInterface item : block.getOperList()) {
            if (item.getType() == pNodeEnum.OPER) {
                // обрабатываем операцию
                log.debug("Обрабатывается операция");
                log.debug(((lexTree) item).toString());
                getNextNode(((lexTree) item).getRootNode(), out);
            } else {
                //Обрабатываем вложенный блок кода
                // Добавлем метку в ASM код
                log.debug("*******************" + ((programBlock) item).getBlockName() + "********************");
                log.debug("Метка = " + ((programBlock) item).getBlockName());
                out.write(((programBlock) item).getBlockName() + ":\n");
                genBlokASMText(((programBlock) item), out);
            }
        }
        /*if (block.getpBlock() != null) {
            genBlokASMText(block.getpBlock(), out);
        }*/
    }

    /**
     *
     * @param outFile
     * @throws IOException
     */
    @Override
    public void genASText(String outFile) throws IOException {

        //lexNode currentNode = this.tree.getRootNode();
        // обходим дерево
        try (FileWriter wout = new FileWriter(outFile)) {
            //Writer wout;
            /*for (lexTree item : this.ps.getPblock().getOperList()) {
                getNextNode(item.getRootNode(), wout);
            }*/
            genBlokASMText(this.ps.getPblock(), wout);
        }
    }

    private lexNode getNextToken() {
        return null;
    }

    public lexTree getTree() {
        return tree;
    }

    @Override
    public programBlock getPB(programBlock parrentBlock, List<token> listToken) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
