package com.zskpaco.interception.plugin.basic;

import com.zskpaco.interception.plugin.bean.InterceptionModel;

import java.io.File;
import java.io.FileOutputStream;

public class ModuleRunnerWrite {

    private static final String ELEMENT_RUNNER = "$ElementRunner";
    private static final String ELEMENT_FACTORY = "$ElementInstance";
    private static final String ELEMENT_UI = "$ElementRunner$Ui";
    private static final String ELEMENT_ASYNC = "$ElementRunner$Async";

    public static String write(String basePath, InterceptionModel model) {
        String moduleName = model.getModule();
        String root = model.getRoot();
        if (root == null) {
            root = "com/zskpaco/interception/" + moduleName + "/runner";
        } else {
            root = root.replace(".", "/");
        }
        String local = root + "/" + moduleName;
        File file = new File(basePath + root);
        if (!file.exists()) {
            file.mkdirs();
        }

        writeRunnerClass(file.getAbsolutePath(), local, moduleName);
        writeRunnerAsyncClass(file.getAbsolutePath(), local, moduleName);
        writeRunnerUiClass(file.getAbsolutePath(), local, moduleName);
        writeRunnerFactoryClass(file.getAbsolutePath(), local, moduleName, model);

        return local + ELEMENT_RUNNER;
    }

    /**
     * basic element runner
     *
     * @param path
     * @param local
     */
    private static void writeRunnerClass(String path, String local, String moduleName) {
        byte[] bytes = _RunnerBytes.dump(local + ELEMENT_RUNNER, local + ELEMENT_FACTORY,
                local + ELEMENT_UI, local + ELEMENT_ASYNC);
        writeBytes(bytes, path + "/" + moduleName + ELEMENT_RUNNER + ".class");
    }

    private static void writeRunnerAsyncClass(String path, String local, String moduleName) {
        byte[] bytes = _RunnerAsyncBytes.dump(local + ELEMENT_ASYNC, local + ELEMENT_RUNNER);
        writeBytes(bytes, path + "/" + moduleName + ELEMENT_ASYNC + ".class");
    }

    private static void writeRunnerUiClass(String path, String local, String moduleName) {
        byte[] bytes = _RunnerUiBytes.dump(local + ELEMENT_UI, local + ELEMENT_RUNNER);
        writeBytes(bytes, path + "/" + moduleName + ELEMENT_UI + ".class");
    }

    /**
     * 工厂模式
     *
     * @param path
     * @param local
     * @param moduleName
     * @param model
     */
    private static void writeRunnerFactoryClass(String path, String local, String moduleName,
                                                InterceptionModel model) {
        byte[] bytes = _RunnerInstanceFactory.dump(local + ELEMENT_FACTORY, model);
        writeBytes(bytes, path + "/" + moduleName + ELEMENT_FACTORY + ".class");
    }

    public static void writeBytes(byte[] bytes, String path) {
        try {
            FileOutputStream fo = new FileOutputStream(path);
            fo.write(bytes);
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error!Cannot basic to a file");
        }
    }

}
