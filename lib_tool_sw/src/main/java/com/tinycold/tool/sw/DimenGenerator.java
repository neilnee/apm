package com.tinycold.tool.sw;

import java.io.File;

public class DimenGenerator {

    /**
     * 设计稿尺寸
     */
    private static final int DESIGN_WIDTH = 360;
    /**
     * 设计稿的高度
     */
    private static final int DESIGN_HEIGHT = 640;
    /**
     * 指定生成的目标项目路径
     */
    private static final String TARGET_PROJECT = "app";

    public static void main(String[] args) {
        // 设计稿最小宽度
        @SuppressWarnings("ConstantConditions")
        int smallest = DESIGN_WIDTH > DESIGN_HEIGHT ? DESIGN_HEIGHT : DESIGN_WIDTH;
        DimenTypes[] values = DimenTypes.values();
        File file = new File("");
        String targetPath = file.getAbsolutePath() + File.separator + TARGET_PROJECT +
                File.separator + "src" + File.separator + "main" + File.separator + "res";
        for (DimenTypes value : values) {
            DimenMaker.makeAll(smallest, value, targetPath);
        }

    }

}
