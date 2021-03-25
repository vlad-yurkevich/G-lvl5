package com.stm.lvl5;

import com.stm.lvl5.service.CompareFileService;
import com.stm.lvl5.service.impl.CompareFileServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Lvl5Application implements CommandLineRunner {

    static final String MSG_NO_DIFF = "Различий в файлах не найдено";


    static final Logger myLogger = LogManager.getLogger(CompareFileService.class);
    static final Logger rootLogger = LogManager.getRootLogger();

    @Autowired
    private CompareFileService compareFileService;

    public static void main(String[] args) {
        SpringApplication.run(Lvl5Application.class, args);
    }

    @Override
    public void run(String... args) {

        if (args.length < 2) {
            rootLogger.error("Приложению передано недостаточное количество параметров");
            return;
        }

        myLogger.info("Начинаем обработку данных");
        int iRes = compareFileService.doCompare(args[0], args[1]);
        if (iRes != CompareFileServiceImpl.RES_OK) {
            myLogger.error(compareFileService.getErrByCode(iRes));
        } else {
            myLogger.info("Данные успешно обработаны");
            List<String> diff = compareFileService.getResultCompare();
            if (diff.size() == 0) {
                System.out.println(MSG_NO_DIFF);
            } else {
                System.out.println("Найдены следующие расхождения с оригинальным файлом:");
                for (String d: diff) {
                    System.out.println(d);
                }
            }
        }

    }

}
