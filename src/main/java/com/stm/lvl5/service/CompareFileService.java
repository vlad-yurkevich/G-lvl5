package com.stm.lvl5.service;

import java.util.List;

/**
 * интерфейс для сравнения содержимого двух файлов построчно
 * @author Владислав Юркевич
 * @version 1.0a
 */
public interface CompareFileService {

    /**
     * Получение текста ошибки по её коду
     * @return String - текст ошибки
     * */
    String getErrByCode(int errCode);
    /**
     * Построчное сравнение содержимого двух файлов
     * @param origFilePath - пусть к исходному файлу
     * @param modFilePath - путь к изменённому файлу
     * @return int - результат операции (0 в случае успеха, отриицательное значение - в случае какой-либо ошибки)
     * */
    int doCompare(String origFilePath, String modFilePath);
    /**
     * Получение результата сравнения
     * @return List<String> - список изменённых строк
     * */
    List<String> getResultCompare();
}
