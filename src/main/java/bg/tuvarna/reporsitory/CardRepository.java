package bg.tuvarna.reporsitory;

import bg.tuvarna.models.entities.Card;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CardRepository implements PanacheRepository<Card> {

}
