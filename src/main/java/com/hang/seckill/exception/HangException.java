package com.hang.seckill.exception;


import com.hang.seckill.pojo.result.CodeMsg;

public class HangException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public HangException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

}
