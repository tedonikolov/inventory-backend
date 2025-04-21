package bg.tuvarna.services;

import bg.tuvarna.models.PageListing;
import bg.tuvarna.models.dto.CardDTO;
import bg.tuvarna.models.dto.requests.AddItemDTO;
import bg.tuvarna.models.dto.requests.CardFilter;
import bg.tuvarna.models.entities.Card;

public interface CardService {
    void addItemToCard(AddItemDTO dto);
    void returnItem(Long cardId);
    PageListing<CardDTO> getAllCards(CardFilter filter);
    Card findCardById(Long id);
    CardDTO getItemById(Long id);
}
