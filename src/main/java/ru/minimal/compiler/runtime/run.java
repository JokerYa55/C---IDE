/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.minimal.compiler.runtime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import ru.minimal.compiler.interfaces.processorInterfaces;

/**
 *
 * @author vasl
 */

/*
FETCH x - положить на стек значение переменной x 
STORE x - сохранить в переменной x значение с вершины стека 
PUSH n - положить число n на вершину стека 
POP - удалить число с вершины стека 
ADD - сложить два числа на вершине стека 
SUB - вычесть два числа на вершине стека 
LT - сравнить два числа с вершины стека (a < b). Результат - 0 или 1 
JZ a - если на вершине стека 0 - перейти к адресу a. 
JNZ a - если на вершине стека не 0 - перейти к адресу a. 
JMP a - перейти к адресу a 
HALT - завершить работу
 */
public class run implements processorInterfaces {

    static Logger log = Logger.getLogger(run.class);
    private BufferedReader fr;
    private Stack<Long> stack = new Stack<>();
    private HashMap<String, Long> varTable = new HashMap();
    // Таблица меток
    private HashMap<String, Integer> labelTable = new HashMap<>();
    private Pattern addPattern;
    private Pattern subPattern;
    private Pattern popPattern;
    private Pattern pushPattern;
    private Pattern storePattern;
    private Pattern fetchPattern;
    private Pattern mulPattern;
    private Pattern lessPattern;
    private Pattern labelPattern;
    private Pattern jzPattern;
    private Pattern jmpPattern;
    private final String fileName;        // имя исполняемого файла
    // Служебные регистры
    private int CS = 0; //Сегмент кода указывает на текущую команду
    // Программа в памяти
    List<String> commandList = new ArrayList();

    private void loadCommand() {
        try {
            // Открываем файл с ASM кодом на исполнение
            this.fr = new BufferedReader(new FileReader(this.fileName));

            String temp = "";
            int i = 0;
            // Читаем команды и загружаем в commandList

            while ((temp = fr.readLine()) != null) {
                log.debug(i + ": temp = " + temp);
                commandList.add(temp);
                if (isLabel(temp)) {
                    labelTable.put(temp, i);
                }
                i++;
            }

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Инициализация шаблонов команд
    private void initPattern() {
        log.debug("initPattern");
        addPattern = Pattern.compile("^add;$");
        subPattern = Pattern.compile("^sub;$");
        popPattern = Pattern.compile("^pop [a-zA-Z]+;$");
        pushPattern = Pattern.compile("^push [0-9a-zA-Z\\.]+;$");
        storePattern = Pattern.compile("^store [0-9a-zA-Z\\._]+;$");
        fetchPattern = Pattern.compile("^fetch [0-9a-zA-Z]+;$");
        mulPattern = Pattern.compile("^mul;$");
        lessPattern = Pattern.compile("^lt;$");
        labelPattern = Pattern.compile("^[a-zA-Z0-9_]+:$");
        jzPattern = Pattern.compile("^jz [a-zA-Z0-9_]+;$");
        jmpPattern = Pattern.compile("^jmp [a-zA-Z0-9_]+;$");
    }

    //
    public run(String fileName) {
        this.fileName = fileName;
        runProc();
    }

    @Override
    public void runProc() {
        try {
            // Инициализируем шаблоны команд для парсинга команд;
            log.debug("runProc()");
            initPattern();
            loadCommand();
            this.CS=0;
            String temp = "";
            while (this.CS < commandList.size()) {
                temp = commandList.get(CS);
                log.debug(this.CS + " : command = " + temp);
                if (isPush(temp)) {
                    log.debug("push");
                    String[] operand = temp.split(" ");
                    stack.push(Long.parseLong(operand[1].substring(0, operand[1].length() - 1)));
                    this.CS++;
                } else if (isFetch(temp)) {// Поучаем значение переменной                        
                    log.debug("fetch");
                    // Проверяем есть ли такая переменна в таблице                    
                    String[] operand = temp.split(" ");
                    String temp1 = operand[1].substring(0, operand[1].length() - 1);
                    long a = 0;
                    if (varTable.containsKey(temp1)) {
                        a = varTable.get(temp1);
                    } else {
                        a = 0;
                        varTable.put(temp1, a);
                    }
                    log.debug("Переменная = " + a);
                    stack.push(a);
                    log.debug(this.getVarTable().toString());
                    this.CS++;
                } else if (isJz(temp)) {
                    Long op1 = stack.pop();
                    //Long op2 = stack.pop();
                    log.debug("jz : " + op1);
                    if (op1 == 0) {
                        // Прыгаем на метку
                        String[] str = temp.split(" ");
                        String labelTemp = str[1].replace(";", ":");
                        this.CS = labelTable.get(labelTemp);
                        log.debug("Переход к команде : " + this.CS + " -> " + commandList.get(CS));
                    } else {
                        this.CS++;
                    }
                } else if (isJmp(temp)) {
                    // Прыгаем на метку
                    String[] str = temp.split(" ");
                    String labelTemp = str[1].replace(";", ":");
                    this.CS = labelTable.get(labelTemp);
                    log.debug("Переход к команде : " + this.CS + " -> " + commandList.get(CS));
                } else if (isAdd(temp)) {
                    Long op1 = stack.pop();
                    Long op2 = stack.pop();
                    log.debug("add : " + op1 + "+" + op2);
                    Long res = op1 + op2;
                    log.debug("res = " + res);
                    stack.push(res);
                    this.CS++;
                } else if (isSub(temp)) {
                    Long op1 = stack.pop();
                    Long op2 = stack.pop();
                    log.debug("sub : " + op2 + "-" + op1);
                    Long res = op2 - op1;
                    log.debug("res = " + res);
                    stack.push(res);
                    this.CS++;
                } else if (isMul(temp)) {
                    Long op1 = stack.pop();
                    Long op2 = stack.pop();
                    log.debug("mul : " + op2 + "*" + op1);
                    Long res = op2 * op1;
                    log.debug("res = " + res);
                    stack.push(res);
                    this.CS++;
                } else if (isStore(temp)) {
                    log.debug("store = " + temp);
                    String[] operand = temp.split(" ");
                    try {
                        varTable.put(operand[1].substring(0, operand[1].length() - 1), new Long(stack.pop()));
                    } catch (Exception e) {
                        varTable.put(operand[1].substring(0, operand[1].length() - 1), new Long(0));
                    }
                    log.debug(this.getVarTable().toString());
                    this.CS++;
                } else // Обработка команды lt;
                if (isLt(temp)) {
                    log.debug("lt = " + temp);
                    Long op1 = stack.pop();
                    Long op2 = stack.pop();
                    log.debug("it : " + op2 + "<" + op1);
                    //stack.push(op2);
                    if (op1 < op2) {
                        this.stack.push(new Long(0));
                    } else {
                        this.stack.push(new Long(1));
                    }
                    log.info(getStack());
                    this.CS++;
                } else {
                    CS++;
                }
                //log.info(getStack());
            }
        } catch (Exception e) {
            log.error("Ошибка: " + e.getMessage());
        }
    }

    private boolean isJz(String command) {
        boolean res = false;
        Matcher m = this.jzPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isJmp(String command) {
        boolean res = false;
        Matcher m = this.jmpPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isLabel(String command) {
        boolean res = false;
        Matcher m = this.labelPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isAdd(String command) {
        boolean res = false;
        Matcher m = this.addPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isSub(String command) {
        boolean res = false;
        Matcher m = this.subPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isLt(String command) {
        boolean res = false;
        Matcher m = this.lessPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isMul(String command) {
        boolean res = false;
        Matcher m = this.mulPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isPop(String command) {
        boolean res = false;
        Matcher m = this.popPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isPush(String command) {
        boolean res = false;
        Matcher m = this.pushPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isFetch(String command) {
        boolean res = false;
        Matcher m = this.fetchPattern.matcher(command);
        res = m.matches();
        return res;
    }

    private boolean isStore(String command) {
        boolean res = false;
        Matcher m = this.storePattern.matcher(command);
        res = m.matches();
        return res;
    }

    // Получение текста программы
    @Override
    public String getProgramText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Получение текущего стека программы
    @Override
    public String getStack() {
        log.debug("getStack()");
        String res = "Состояние стека :\n";
        int i = 0;
        for (Long itemKey : stack) {
            i++;
            res = res + i + " : " + itemKey + "\n";
        }
        return res;
    }

    // Получение таблицы переменных программы
    @Override
    public String getVariable() {
        log.debug("getVariable()");
        String res = "Переменные : \n";
        try {
            for (String itemKey : this.getVarTable().keySet()) {
                res = res + itemKey + "\t= " + this.getVarTable().get(itemKey) + "\n";
            }
            //log.info(res);
        } catch (Exception e) {
            log.error("Ошибка : " + e.getMessage());
        }
        return res;
    }

    public HashMap<String, Long> getVarTable() {
        return varTable;
    }
}
