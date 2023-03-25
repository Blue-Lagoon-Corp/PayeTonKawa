package com.bluelagoon.payetonkawa.qrcode.impl;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class QrCodeServiceImplTest {

    private QrCodeServiceImpl qrCodeService;

    @BeforeEach
    void init(){
        qrCodeService = new QrCodeServiceImpl();
    }

    @Test
    void testCreateQR() throws WriterException, IOException {
        assertNotNull(qrCodeService.createQR("test"));
    }
}

