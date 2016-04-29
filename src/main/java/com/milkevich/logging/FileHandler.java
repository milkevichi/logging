package com.milkevich.logging;

import java.io.*;

/**
 * Created by aksenov on 27.04.2016.
 */
public class FileHandler extends Handler {

    private File file;
    private LogManager logManager = LogManager.getLogManager();

    public FileHandler() {
        this("logFile.log");
    }

    public FileHandler(String fileName) {
        file = new File(fileName);
        setFormatter(logManager.getFormatterProperty(getClass().getName() + ".formatter", new TextFormatter()));
    }

    @Override
    public void writeMessage(Message msg) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (file.length() < 45) {
            try (BufferedWriter bof = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true))) {

                Formatter formatter = getFormatter();

                bof.write(formatter.getHeader());
                String text = formatter.format(msg);
                bof.write(text);
                bof.write(formatter.getFooter());
                bof.flush();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            try (RandomAccessFile f = new RandomAccessFile(file.getAbsoluteFile(), "rw")) {
                long length = f.length() - 1;
                byte b;
                do {
                    length -= 1;
                    f.seek(length);
                    b = f.readByte();
                } while (b != 10);
                f.setLength(length + 1);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            try (BufferedWriter bof = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true))) {

                Formatter formatter = getFormatter();

                String text = formatter.format(msg);
                bof.write(text);
                bof.write(formatter.getFooter());
                bof.flush();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
