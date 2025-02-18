package de.intranda.counterscript.api;

import java.sql.SQLException;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import de.intranda.counterscript.model.MetadataInformation;
import de.intranda.counterscript.persistence.MetadataInformationManager;
import lombok.extern.log4j.Log4j;

@Log4j
@Path("/counterscript/json")
public class JsonResource {

    @GET
    @Produces("text/json")
    public List<MetadataInformation> getCsv() {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataDate(null, null, false);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @Path("/{startdate}/{enddate}")
    @GET
    @Produces("text/json")
    public List<MetadataInformation> getCsv(@PathParam("startdate") String start, @PathParam("enddate") String end) {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataString(start, end, false);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @Path("/withinactive")
    @GET
    @Produces("text/json")
    public List<MetadataInformation> getCsvWithInactive() {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataDate(null, null, true);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @Path("/withinactive/{startdate}/{enddate}")
    @GET
    @Produces("text/json")
    public List<MetadataInformation> getCsvWithInactive(@PathParam("startdate") String start, @PathParam("enddate") String end) {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataString(start, end, true);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

}
