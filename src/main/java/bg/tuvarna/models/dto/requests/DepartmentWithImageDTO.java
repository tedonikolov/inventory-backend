package bg.tuvarna.models.dto.requests;

import bg.tuvarna.models.dto.DepartmentDTO;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;

import java.io.File;

public class DepartmentWithImageDTO {
    @FormParam("dto")
    @PartType(MediaType.APPLICATION_JSON)
    private DepartmentDTO dto;
    @FormParam("image")
    @PartType("application/octet-stream")
    private File image;

    public DepartmentDTO getDto() {
        return dto;
    }

    public void setDto(DepartmentDTO dto) {
        this.dto = dto;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
