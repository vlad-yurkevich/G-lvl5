package com.stm.lvl5.service.impl;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.stm.lvl5.service.CompareFileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Реализация интерфейса для сравнения содержимого двух файлов построчно
 * @author Владислав Юркевич
 * @version 1.0a
 */
@Service
public class CompareFileServiceImpl implements CompareFileService {

    //--Константы
    private static final int LOAD_ORIG = 1;
    private static final int LOAD_MOD = 2;
    public static final int RES_OK = 0;
    public static final int RES_FILE_ORIG_NOT_FOUND = -1;
    public static final int RES_FILE_MOD_NOT_FOUND = -2;
    public static final int RES_FILE_ORIG_EMPTY = -3;
    public static final int RES_FILE_MOD_EMPTY = -4;
    private static final String DIFF_MASK = "%d: <%s>";

    //--Переменные
    private List<String> origList; //--Список строк из оригинального файла
    private List<String> modList;  //--Список строк из модифицированного файла
    private List<String> resultCompare; //--Результат сравнения

    //--Загрузка данных из текстового файла
    private int loadLines(String filePath, int iMode) {
        try {
            Scanner scanner = new Scanner(new File(filePath));
            if (!scanner.hasNextLine()) {
                if (iMode == LOAD_ORIG) {
                    return RES_FILE_ORIG_EMPTY;
                } else { return RES_FILE_MOD_EMPTY; }
            }
            List<String> tmp = new ArrayList<>();
            while (scanner.hasNextLine()) {
                tmp.add(scanner.nextLine());
            }
            if (iMode == LOAD_ORIG) {
                origList = tmp;
            } else { modList = tmp; }

            //--
            return RES_OK;
        } catch (FileNotFoundException e) {
            if (iMode == LOAD_ORIG) {
                return RES_FILE_ORIG_NOT_FOUND;
            } else {return RES_FILE_MOD_NOT_FOUND; }
        }
    }

    @Override
    public String getErrByCode(int errCode) {

        String sErrText;

        switch (errCode) {
            case RES_FILE_ORIG_NOT_FOUND: {
                sErrText = "Original file not found"; break;
            }
            case RES_FILE_MOD_NOT_FOUND: {
                sErrText = "Modified file not found"; break;
            }
            case RES_FILE_ORIG_EMPTY: {
                sErrText = "Original file is empty. Processing is not possible"; break;
            }
            case RES_FILE_MOD_EMPTY: {
                sErrText = "Modified file is empty. Processing is not possible"; break;
            }
            default: sErrText = "Unidentified error";
        }
        return sErrText;
    }

    @Override
    public int doCompare(String origFilePath, String modFilePath) {

        //--Обнуляем переменные
        origList = null;
        modList = null;
        resultCompare = null;

        //--Читаем оба файла
        int iRes = loadLines(origFilePath, LOAD_ORIG);
        if (iRes != RES_OK) { return iRes; }
        iRes = loadLines(modFilePath, LOAD_MOD);
        if (iRes != RES_OK) { return iRes; }

        //--Обрабатываем
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .mergeOriginalRevised(true)
                .oldTag(f -> "~")
                .newTag(f -> "**")
                .build();
        List<DiffRow> rows = generator.generateDiffRows(origList, modList);
        resultCompare = new ArrayList<>();
        for (int i = 0; i< rows.size(); i++){
            if (!rows.get(i).getTag().equals(DiffRow.Tag.EQUAL)) {
                resultCompare.add(String.format(DIFF_MASK, i + 1, rows.get(i).getNewLine()));
            }
        }
        //--Раз добрались сюда, то всё обработалось, как нужно
        return RES_OK;
    }

    @Override
    public List<String> getResultCompare() {
        return resultCompare;
    }
}
