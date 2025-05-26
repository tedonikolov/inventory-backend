package bg.tuvarna.services.impl;

import bg.tuvarna.services.ItemService;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SchedulerService {
    private final ItemService itemService;

    public SchedulerService(ItemService itemService) {
        this.itemService = itemService;
    }


    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    void performAutomaticScrapping() {
        itemService.changeAmortization();
        itemService.transferItemsToMaterial();
        itemService.performAutomaticScrapping();
    }
}
