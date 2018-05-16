package com.zskpaco.interception.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.utils.FileUtils
import com.google.common.collect.Sets
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.gson.Gson
import com.zskpaco.interception.plugin.basic.ModuleRunnerWrite
import com.zskpaco.interception.plugin.bean.InterceptionModel
import com.zskpaco.interception.plugin.bean.PluginConfig
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode

class Transformer extends Transform {

    private Project project
    private String runnerOwner

    public static PluginConfig config = new PluginConfig()

    Transformer(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "interception"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT)
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        PluginConfig config = project.extensions.interception
        if (config != null) {
            Transformer.config = config
        }

        Gson gson = new Gson()

        InterceptionModel model = new InterceptionModel()
        String modulePath = System.getProperty("user.dir") + "/.plugin/module-" + project.getName() + ".json"
        String allPath = System.getProperty("user.dir") + "/.plugin/module-all.json"
        File moduleFile = new File(modulePath)
        if (moduleFile.exists()) {
            String str = getFileStr(moduleFile)
            if (!str.isEmpty()) {
                model = gson.fromJson(str, InterceptionModel.class)
            }
        }
        moduleFile = new File(allPath)
        if (!moduleFile.exists()) {
            throw new RuntimeException("Before build project, please execute buildModules task.")
        } else {
            String str = getFileStr(moduleFile)
            if (!str.isEmpty()) {
                InterceptionModel model1 = gson.fromJson(str, InterceptionModel.class)
                model.interceptors = model1.interceptors
            }
        }

        transformInvocation.inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                if (directoryInput.file.isDirectory()) {
                    directoryInput.file.eachFileRecurse { File file ->
                        def name = file.name
                        if (name.endsWith(".class") && !name.startsWith("R\$") &&
                                "R.class" != name && "BuildConfig.class" != name && !name.endsWith("\$ElementRunner\$Ui.class")
                                && !name.endsWith("\$ElementRunner\$Async.class") && !name.endsWith("\$ElementRunner.class") && !name.endsWith("\$ElementInstanceFactory.class")) {

                            ClassReader cr = new ClassReader(file.bytes)
                            ClassNode classNode = new ClassNode()
                            cr.accept(classNode, 0)

                            if (runnerOwner == null && moduleFile.exists()) {
                                runnerOwner = ModuleRunnerWrite.write(file.getAbsolutePath().replace(classNode.name, "").replace(".class", ""), model)
                            }

                            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                            ClassHandler classHandler = new ClassHandler(cw, runnerOwner, file.getAbsolutePath(), classNode, model)
                            cr.accept(classHandler, 8)

                            FileOutputStream fos = new FileOutputStream(
                                    file.parentFile.absolutePath + File.separator + name)
                            fos.write(cw.toByteArray())
                            fos.close()
                        }
                    }
                }

                def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)

            }

            input.jarInputs.each { JarInput jarInput ->
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }

        }
    }

    static String getFileStr(File path) {
        try {
            return getFileStr1(new FileInputStream(path))
        } catch (Throwable e) {
            e.printStackTrace()
        }
        return ""
    }

    static String getFileStr1(InputStream inputStream) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()
        byte[] buffer = new byte[1024]
        int len
        while ((len = inputStream.read(buffer)) != -1) {
            arrayOutputStream.write(buffer, 0, len)
        }
        return arrayOutputStream.toString()
    }
}
