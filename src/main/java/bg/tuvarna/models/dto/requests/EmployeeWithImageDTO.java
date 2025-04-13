package bg.tuvarna.models.dto.requests;

import bg.tuvarna.models.dto.EmployeeDTO;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;

import java.io.File;

public class EmployeeWithImageDTO {
    @FormParam("dto")
    @PartType(MediaType.APPLICATION_JSON)
    private EmployeeDTO dto;
    @FormParam("image")
    @PartType("application/octet-stream")
    private File image;

    public EmployeeDTO getDto() {
        return dto;
    }

    public void setDto(EmployeeDTO dto) {
        this.dto = dto;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
