package com.revisacaminhoes.site.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class UploadService {

    private final Cloudinary cloudinary;

    public UploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, String> uploadImagem(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "produtos",
                            "resource_type", "image",
                            "transformation", new Transformation()
                                    .width(800).height(800).crop("limit")
                    ));

            return Map.of(
                    "url", (String) uploadResult.get("secure_url"),
                    "publicId", (String) uploadResult.get("public_id")
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar imagem: " + e.getMessage());
        }
    }

    public void deletarImagem(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir imagem: " + e.getMessage());
        }
    }
}