package bg.tuvarna.services;

import bg.tuvarna.models.PageListing;
import bg.tuvarna.models.dto.ItemDTO;
import bg.tuvarna.models.dto.requests.ItemFilter;
import bg.tuvarna.models.dto.requests.ItemWithImageDTO;
import bg.tuvarna.models.entities.Item;

public interface ItemService {
    void save(ItemWithImageDTO itemDTO);
    Item findItemById(Long id);
    ItemDTO getItemById(Long id);
    PageListing<ItemDTO> getAllItems(ItemFilter filter);
    void changeAmortization();
    void transferItemsToMaterial();
    void performAutomaticScrapping();
}
