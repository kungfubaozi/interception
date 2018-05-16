package com.zskpaco.interception.plugin

import com.google.gson.Gson
import com.zskpaco.interception.plugin.bean.InterceptionModel
import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleRootPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        Gson gson = new Gson()

        project.task("buildModules").doLast {
            println "buildModules task execute to analysis modules json file"

            String path = System.getProperty("user.dir")
            File file = new File(path + "/.plugin")
            if (!file.exists()) {
                file.mkdirs()
            }
            InterceptionModel model = new InterceptionModel()
            model.module = "root-project"
            file.listFiles().each {
                if (it.file) {
                    if (it.name.startsWith("module-") && it.name.endsWith(".json") && it.name != "module-all.json") {
                        try {
                            println "merge ${it.name}"
                            String jsonStr = Transformer.getFileStr(it)
                            InterceptionModel interceptionModel = gson.fromJson(jsonStr, InterceptionModel.class)
                            model.interceptors.addAll(interceptionModel.interceptors)
                        } catch (FileNotFoundException e) {
                            e.printStackTrace()
                        } catch (IOException e) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            println "merge finished"
            file = new File(path + "/.plugin/module-all.json")
            FileWriter writer
            try {
                writer = new FileWriter(file)
                writer.write(gson.toJson(model))
                println "build to module-all.json file"
            } catch (IOException e) {
                e.printStackTrace()
                println "error"
            } finally {
                if (writer != null) {
                    try {
                        writer.close()
                    } catch (IOException e) {
                        e.printStackTrace()
                        println "error"
                    }
                }
            }
        }

    }
}
