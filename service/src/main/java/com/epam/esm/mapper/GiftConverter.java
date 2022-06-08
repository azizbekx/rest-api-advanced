package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class GiftConverter {
    public static GiftCertificateDto toDto(GiftCertificate gift) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto(
                gift.getId(),
                gift.getName(),
                gift.getDescription(),
                gift.getPrice(),
                gift.getDuration(),
                String.valueOf(gift.getCreateDate()),
                String.valueOf(gift.getLastUpdateTime())
        );
        if (gift.getTags() != null) {
            if (!gift.getTags().isEmpty()) {
                List<TagDto> tagDtos = gift.getTags().stream()
                        .map(TagConverter::toDto)
                        .collect(Collectors.toList());
                giftCertificateDto.setTags(tagDtos);
            }
        }
        return giftCertificateDto;
    }

    public static GiftCertificate toEntity(GiftCertificateDto giftDto) {
        GiftCertificate gift = new GiftCertificate();
        gift.setId(giftDto.getId());
        gift.setName(giftDto.getName());
        gift.setDescription(giftDto.getDescription());
        gift.setPrice(giftDto.getPrice());
        gift.setDuration(giftDto.getDuration());
        if (giftDto.getCreate_date() != null) {
            gift.setCreateDate(LocalDateTime.parse(giftDto.getCreate_date()));
        }
        if (giftDto.getLast_update_date() != null) {
            gift.setLastUpdateTime(LocalDateTime.parse(giftDto.getLast_update_date()));
        }
        if (giftDto.getTags() != null) {
            if (giftDto.getTags().get(0).getName() != null) {
                List<Tag> tags = giftDto.getTags().stream()
                        .map(TagConverter::toEntity)
                        .collect(Collectors.toList());
                gift.setTags(new HashSet<>(tags));
            }
        }
        return gift;
    }

}
