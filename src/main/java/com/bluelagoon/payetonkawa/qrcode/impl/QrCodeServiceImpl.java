package com.bluelagoon.payetonkawa.qrcode.impl;

import com.bluelagoon.payetonkawa.qrcode.services.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class QrCodeServiceImpl implements QrCodeService {
    @Override
    public byte[] createQR(String data) throws WriterException, IOException {

        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(data.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8),
                BarcodeFormat.QR_CODE, 100, 100);

        var pngOutputStream = new ByteArrayOutputStream();
        var con = new MatrixToImageConfig();

        MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream,con);

        return pngOutputStream.toByteArray();
    }
}
