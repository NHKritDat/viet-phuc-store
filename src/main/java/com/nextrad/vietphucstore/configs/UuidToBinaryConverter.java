package com.nextrad.vietphucstore.configs;

import com.nextrad.vietphucstore.utils.IdUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Converter
public class UuidToBinaryConverter implements AttributeConverter<UUID, byte[]> {
    private final IdUtil idUtil;

    @Override
    public byte[] convertToDatabaseColumn(UUID uuid) {
        return uuid == null ? null : idUtil.uuidToBytes(uuid);
    }

    @Override
    public UUID convertToEntityAttribute(byte[] bytes) {
        return bytes == null ? null : idUtil.bytesToUuid(bytes);
    }
}
