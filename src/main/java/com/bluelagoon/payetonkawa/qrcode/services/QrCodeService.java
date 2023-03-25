package com.bluelagoon.payetonkawa.qrcode.services;

import com.google.zxing.WriterException;

import java.io.IOException;


public interface QrCodeService {
     byte[] createQR(String data) throws WriterException, IOException;
}
