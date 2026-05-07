package com.disfracesrivera.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ArchivoService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String guardarImagen(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen");
        }

        String tipoContenido = archivo.getContentType();

        if (tipoContenido == null || !tipoContenido.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        try {
            Path carpetaDestino = Paths.get(uploadDir).toAbsolutePath().normalize();

            if (!Files.exists(carpetaDestino)) {
                Files.createDirectories(carpetaDestino);
            }

            String nombreOriginal = archivo.getOriginalFilename();
            String extension = obtenerExtension(nombreOriginal);

            String nombreArchivo = UUID.randomUUID() + extension;

            Path rutaDestino = carpetaDestino.resolve(nombreArchivo);

            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/disfraces/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return ".jpg";
        }

        return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
    }
}