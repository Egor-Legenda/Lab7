package org.example.server.base;

import org.example.server.Server;
import org.example.server.commandIn.Exit;
import org.example.server.commandIn.Save;
import org.example.server.exceptions.IOException;
import org.example.server.exceptions.NoSuchElementException;
import org.example.server.exceptions.NullPointerException;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;

public class ConsoleReaderThread extends Thread {
    @Override
    public void run() {
        try {
            // Создаем BufferedReader для чтения ввода с консоли
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Читаем ввод с консоли
                String line = reader.readLine().trim();
                if (line != null) {
                    line = line.toLowerCase();
                    // Обрабатываем ввод
                    if (line.equals("exit")) {
                        Exit exit = new Exit();
                        exit.execution("exit", null,null,null,null);
                    } else if (line.equals("save")) {
                        Save save = new Save();
                        save.execution("save", null, null,null,null);
                    } else {
                        Server.logger.info("Команда,которую ввели не поддерживается сервером");
                    }
                }
            }

        }catch (java.io.IOException e) {
            if (e instanceof EOFException) {
                Server.logger.info("Ввод завершен (Ctrl+D)");
            } else if (e.getMessage().contains("Stream closed")) {
                Server.logger.info("Ввод прерван (Ctrl+C)");
            } else {
                Server.logger.info("Ошибка ввода");
            }
            Exit exit =new Exit();
            exit.execution("exit", null,null,null,null);
        } catch (java.lang.NullPointerException E) {
            Server.logger.info("Сервер остановлен");
            Exit exit =new Exit();
            exit.execution("exit", null,null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
            Exit exit =new Exit();
            exit.execution("exit", null,null,null,null);
        }
    }
}