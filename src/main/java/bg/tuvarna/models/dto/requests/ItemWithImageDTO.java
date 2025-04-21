package bg.tuvarna.models.dto.requests;

import bg.tuvarna.models.dto.ItemDTO;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;

import java.io.File;

public class ItemWithImageDTO {
    @FormParam("dto")
    @PartType(MediaType.APPLICATION_JSON)
    private ItemDTO dto;
    @FormParam("image")
    @PartType("application/octet-stream")
    private File image;

    public ItemDTO getDto() {
        return dto;
    }

    public void setDto(ItemDTO dto) {
        this.dto = dto;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
