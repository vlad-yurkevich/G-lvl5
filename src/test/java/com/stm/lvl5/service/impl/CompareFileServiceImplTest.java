package com.stm.lvl5.service.impl;

import com.stm.lvl5.service.CompareFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompareFileServiceImplTest {

    @Autowired
    CompareFileService compareFileService;

    //--Получение текста известной ошибки
    @Test
    void getErrByCodeGood() {
        int iGoodErrCode = CompareFileServiceImpl.RES_FILE_MOD_EMPTY;
        String s = compareFileService.getErrByCode(iGoodErrCode);
        assertNotEquals(s, "Unidentified error");
    }

    //--Получение текста по неизвестной ошибке
    @Test
    void getErrByCodeWrong() {
        int iWrongErrCode = -100500;
        String s = compareFileService.getErrByCode(iWrongErrCode);
        assertEquals(s, "Unidentified error");
    }

    //--Сравнение двух одинаковых файлов. Количество расхождений равно 0
    @Test
    void doCompareCopyFile() {
        int iRes = compareFileService.doCompare("test\\NormalFile.txt", "test\\NormalFileCopy.txt");
        assertEquals(iRes, CompareFileServiceImpl.RES_OK);
        assertEquals(compareFileService.getResultCompare().size(), 0);
    }

    //--Получение результата сравнения двух разных файлов. Количство расхождений 2
    @Test
    void getResultCompareDiff2() {
        compareFileService.doCompare("test\\NormalFile.txt", "test\\NormalFileDiff2.txt");
        assertNotNull(compareFileService.getResultCompare());
        assertEquals(compareFileService.getResultCompare().size(), 2);
    }

    //--Получение результата сравнения, когда один из файлов пуст. Результат равен Null
    @Test
    void getResultCompareEmpty() {
        compareFileService.doCompare("test\\EmptyFile.txt", "test\\NormalFile.txt");
        assertNull(compareFileService.getResultCompare());
    }
}