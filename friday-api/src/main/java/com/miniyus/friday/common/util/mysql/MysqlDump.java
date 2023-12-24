package com.miniyus.friday.common.util.mysql;

import lombok.Builder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MysqlDump {
    private static class StreamGobbler implements Runnable {
        private InputStream is;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream is, Consumer<String> consumer) {
            this.is = is;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(is)).lines().forEach(consumer);
        }
    }


    @Builder
    public record MysqlDumpParameter(
        String host,
        String port,
        String user,
        String password,
        String database,
        String[] table,
        String path
    ) {
    }

    public static MysqlDumpParameter createParameter(String host, String port, String user,
        String password, String database, String[] table, String path) {
        return MysqlDumpParameter.builder()
            .host(host)
            .port(port)
            .user(user)
            .password(password)
            .database(database)
            .table(table)
            .path(path)
            .build();
    }

    @Builder
    public record MysqlDumpResult(
        String command,
        boolean status,
        String message
    ) {
    }

    public static List<MysqlDumpResult> dump(MysqlDumpParameter parameter) throws IOException {
        File path = new File(parameter.path);
        if (!path.exists()) {
            var paths = Paths.get(parameter.path());
            Files.createDirectories(paths);
        }

        List<String> commands = makeDumpCommand(parameter);
        List<MysqlDumpResult> results = new ArrayList<>();
        var workingDir = System.getProperty("user.dir");
        File commandDir = new File(workingDir + "/" + parameter.path());
        if(!commandDir.exists()) {
            throw new FileNotFoundException("commandDir not exist");
        }

        for (String cmd : commands) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("sh", "-c", cmd)
                    .directory(commandDir);
                Process process = processBuilder.start();

                StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
                Executors.newSingleThreadExecutor().submit(streamGobbler);

                int processComplete = process.waitFor();
                if (processComplete != 0) {
                    File isCreated = new File(parameter.path());
                    if (!isCreated.exists()) {
                        results.add(MysqlDumpResult.builder()
                            .message("failed")
                            .command(cmd)
                            .status(false)
                            .build());
                    }
                }
                results.add(MysqlDumpResult.builder()
                    .message("success")
                    .command(cmd)
                    .status(true)
                    .build());
            } catch (Exception e) {
                results.add(MysqlDumpResult.builder()
                    .message(e.getMessage())
                    .command("failed")
                    .status(false)
                    .build());
            }
        }

        return results;
    }

    public static void restore(MysqlDumpParameter parameter) {
        String command = makeRestoreCommand(parameter);
        // TODO: 복구 명령 작성 필요
    }

    public static List<String> makeDumpCommand(MysqlDumpParameter parameter) {
        List<String> commands = new ArrayList<>();
        for (String table : parameter.table()) {
            StringBuilder builder = new StringBuilder();
            builder.append("mysqldump")
                .append(" --no-tablespaces")
                .append(" -h ").append(parameter.host())
                .append(" -P ").append(parameter.port())
                .append(" -u ").append(parameter.user())
                .append(" -p").append(parameter.password())
                .append(" ").append(parameter.database())
                .append(" ").append(table)
                .append(" > ").append(table).append(".sql");
            commands.add(builder.toString());
        }

        return commands;
    }

    public static String makeRestoreCommand(MysqlDumpParameter parameter) {
        return "mysqldump" +
            " -h " + parameter.host() +
            " -P " + parameter.port() +
            " -u " + parameter.user() +
            " -p" + parameter.password() +
            " " + parameter.database() +
            //            .append(" ").append(parameter.table())
            " < " + parameter.path() + ".sql";
    }
}
