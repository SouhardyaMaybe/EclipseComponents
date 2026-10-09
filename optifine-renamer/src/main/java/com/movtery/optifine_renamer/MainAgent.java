package com.movtery.optifine_renamer;

import com.movtery.optifine_renamer.optifine.ChangeClassTransformer;

import java.lang.instrument.Instrumentation;

public class MainAgent {
    //自定义的版本名称，可以通过命令行进行传入，并不需要硬编码这个字符串！具体用法查看 README.md
    private static String replacementValue = "OptiFine_Version";

    public static void main(String[] args) {
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        start(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        start(agentArgs, inst);
    }

    private static void start(String agentArgs, Instrumentation inst) {
        if (agentArgs != null && !agentArgs.isEmpty()) {
            replacementValue = agentArgs;
        }

        inst.addTransformer(new ChangeClassTransformer(replacementValue));
    }
}