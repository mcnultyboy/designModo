package com.yb.singleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader1 extends ClassLoader{
    private String path;

    // class 的路径"/Users/mac/CommProjects/ClassLoaderDemo/src/com/xuhuawei/classloaderdemo/Log.class"
    public MyClassLoader1(String path){
        this.path = path;
    }

    // 先从parent中查找是否已经加载了class，如果没有加载，则调用findClass进行加载
    // name class 的全称 com.xuhuawei.classloaderdemo.Log
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class log = null;
        // 获取该class文件字节码数组
        byte[] classData = getData();

        if (classData != null) {
            // 将class的字节码数组转换成Class类的实例
            log = defineClass(name, classData, 0, classData.length);
        }
        return log;

    }

    /**
     * 将class文件转化为字节码数组
     * @return
     */
    private byte[] getData() {

        File file = new File(path);
        if (file.exists()){
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            return out.toByteArray();
        }else{
            return null;
        }


    }



}
