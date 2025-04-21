package bg.tuvarna.services.converters.impl;

import bg.tuvarna.models.dto.ItemDTO;
import bg.tuvarna.models.entities.Item;
import bg.tuvarna.services.converters.Converter;

public class ItemConverter implements Converter<ItemDTO, Item> {
    @Override
    public ItemDTO convertToDto(Item entity) {
        return new ItemDTO(
                entity.id,
                entity.getNumber(),
                entity.getName(),
                entity.getPrice(),
                entity.getNoAmortizationPrice(),
                entity.getAmortizationPrice(),
                entity.getType(),
                entity.getAcquisitionDate(),
                entity.getExploitationDate(),
                entity.getStatus(),
                entity.getAmortization(),
                entity.getToDate(),
                entity.getDeregistrationDate(),
                entity.getCategory().id
        );
    }

    @Override
    public Item convertToEntity(ItemDTO dto) {
        Item item = new Item();
        return getItem(item, dto);
    }

    @Override
    public Item updateEntity(Item entity, ItemDTO dto) {
        return getItem(entity, dto);
    }

    private Item getItem(Item entity, ItemDTO dto) {
        entity.setNumber(dto.number());
        entity.setName(dto.name());
        entity.setPrice(dto.price());
        entity.setNoAmortizationPrice(dto.noAmortizationPrice());
        entity.setAmortizationPrice(dto.amortizationPrice());
        entity.setType(dto.type());
        entity.setAcquisitionDate(dto.acquisitionDate());
        entity.setExploitationDate(dto.exploitationDate());
        entity.setStatus(dto.status());
        entity.setAmortization(dto.amortization());
        entity.setToDate(dto.toDate());
        return entity;
    }
}
